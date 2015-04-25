package com.ahimsa.reactivex.yahoofinance;

import yahoofinance.YahooFinance;

/**
 * @author Miguel Reyes
 *         Date: 4/25/15
 *         Time: 9:32 PM
 */
public class StockInfo {

    public final String ticker;
    public final double value;

    public StockInfo(String ticker, double value) {
        this.ticker = ticker;
        this.value = value;
    }

    public static StockInfo fetch(String ticker) {
//        if (Math.random() > 0.95) throw new RuntimeException("Something went wrong");
        return new StockInfo(ticker, YahooFinance.get(ticker).getQuote().getPrice().doubleValue());
    }

    @Override
    public String toString() {
        return "StockInfo{" +
                "ticker='" + ticker + '\'' +
                ", value=" + value +
                '}';
    }
}
