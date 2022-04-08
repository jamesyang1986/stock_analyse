package com.qiezi.stock.pojo;

import lombok.Data;

@Data
public class StockPoint {
    private String stockCode;
    private String stockName;
    private String date;
    private String time;

    private String todayOpenPrice;
    private String yesterdayClosePrice;
    private String currentPrice;
    private String todayMaxPrice;
    private String todayMinPrice;

    @Override
    public String toString() {
        return "Stock{" +
                "stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", todayOpenPrice='" + todayOpenPrice + '\'' +
                ", yesterdayClosePrice='" + yesterdayClosePrice + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", todayMaxPrice='" + todayMaxPrice + '\'' +
                ", todayMinPrice='" + todayMinPrice + '\'' +
                '}';
    }

    public static final class StockBuilder {
        private String stockCode;
        private String stockName;
        private String date;
        private String time;
        private String todayOpenPrice;
        private String yesterdayClosePrice;
        private String currentPrice;
        private String todayMaxPrice;
        private String todayMinPrice;

        private StockBuilder(String stockCode) {
            this.stockCode = stockCode;
        }

        public static StockBuilder builder(String stockCode) {
            return new StockBuilder(stockCode);
        }

        public StockBuilder withStockName(String stockName) {
            this.stockName = stockName;
            return this;
        }

        public StockBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public StockBuilder withTime(String time) {
            this.time = time;
            return this;
        }

        public StockBuilder withTodayOpenPrice(String todayOpenPrice) {
            this.todayOpenPrice = todayOpenPrice;
            return this;
        }

        public StockBuilder withYesterdayClosePrice(String yesterdayClosePrice) {
            this.yesterdayClosePrice = yesterdayClosePrice;
            return this;
        }

        public StockBuilder withCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
            return this;
        }

        public StockBuilder withTodayMaxPrice(String todayMaxPrice) {
            this.todayMaxPrice = todayMaxPrice;
            return this;
        }

        public StockBuilder withTodayMinPrice(String todayMinPrice) {
            this.todayMinPrice = todayMinPrice;
            return this;
        }

        public StockPoint build() {
            StockPoint stock = new StockPoint();
            stock.setStockCode(stockCode);
            stock.setStockName(stockName);
            stock.setDate(date);
            stock.setTime(time);
            stock.setTodayOpenPrice(todayOpenPrice);
            stock.setYesterdayClosePrice(yesterdayClosePrice);
            stock.setCurrentPrice(currentPrice);
            stock.setTodayMaxPrice(todayMaxPrice);
            stock.setTodayMinPrice(todayMinPrice);
            return stock;
        }
    }
}
