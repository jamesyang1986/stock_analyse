package com.qiezi.stock.crawler;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.qiezi.stock.pojo.StockPoint;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SinaStockDataCrawler {
    public static WebClient webClient = null;
    public static final String SINA_STOCK_URL_PREFIX = "http://hq.sinajs.cn/list=";

    static {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setTimeout(120 * 1000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
    }

    public String downloadStockData(String stockCode) {
        HtmlPage htmlPage;
        URL urlTabBet = null;
        String URL = SINA_STOCK_URL_PREFIX + stockCode;
        WebRequest webRequest;
        try {
            urlTabBet = new URL(URL);
            webRequest = new WebRequest(urlTabBet, com.gargoylesoftware.htmlunit.HttpMethod.POST);
            Map<String, String> additionalHeaders = new HashMap<String, String>();
            additionalHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
            additionalHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
            additionalHeaders.put("Accept", "*/*");
            additionalHeaders.put("referer", "http://finance.sina.com.cn");
            webRequest.setAdditionalHeaders(additionalHeaders);
            htmlPage = webClient.getPage(webRequest);
            String contentAsString = htmlPage.getWebResponse().getContentAsString();
            return contentAsString;
        } catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StockPoint parseStockData(String stockCode, String stockStr) {
        String[] dataArray = stockStr.split("\"");
        if (dataArray != null && dataArray.length >= 2) {
            String[] datas = dataArray[1].split(",");
            String stockName = datas[0];
            String date = datas[datas.length - 3];
            String time = datas[datas.length - 2];
            String todayOpenPrice = datas[1];
            String yesterdayClosePrice = datas[2];
            String currentPrice = datas[3];
            String todayMaxPrice = datas[4];
            String todayMinPrice = datas[5];

            StockPoint stock = StockPoint.StockBuilder.builder(stockCode)
                    .withDate(date)
                    .withTime(time)
                    .withStockName(stockName)
                    .withTodayOpenPrice(todayOpenPrice)
                    .withYesterdayClosePrice(yesterdayClosePrice)
                    .withCurrentPrice(currentPrice)
                    .withTodayMinPrice(todayMinPrice)
                    .withTodayMaxPrice(todayMaxPrice)
                    .build();
            return stock;
        }
        return null;
    }


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.DAY_OF_WEEK);
        calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("year:" + calendar.get(Calendar.YEAR) + "day:" + calendar.get(Calendar.DAY_OF_WEEK)
                + "hour:" + calendar.get(Calendar.HOUR_OF_DAY));

        String stockCode = "sh600031";

        SinaStockDataCrawler crawler = new SinaStockDataCrawler();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (ifMarketOpen()) {
                    runJob();
                }
            }

            private boolean ifMarketOpen() {
                Calendar calendar = Calendar.getInstance();
                calendar.get(Calendar.YEAR);
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                if (day > Calendar.SUNDAY && day < Calendar.SATURDAY
                        && ((hour == 9 && minute >= 30) || hour == 10 || (hour == 11 && minute <= 30))
                        && (hour >= 13 && hour < 15)) {
                    return true;
                }
                return false;
            }

            private void runJob() {
                long startTime = System.currentTimeMillis();
                System.out.println("start to download stock info....");
                String stockStr = crawler.downloadStockData(stockCode);
                StockPoint stock = parseStockData(stockCode, stockStr);
                System.out.println("==========================");
                System.out.println(stock.toString());
                long endTime = System.currentTimeMillis();
                System.out.println("----------------it cost:" + (endTime - startTime));
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

}
