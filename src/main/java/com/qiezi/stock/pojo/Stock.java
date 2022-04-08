package com.qiezi.stock.pojo;

import lombok.Data;

@Data
public class Stock {
    private String stockCode;
    private String stockName;

    public Stock(String stockCode, String stockName) {
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                '}';
    }
}
