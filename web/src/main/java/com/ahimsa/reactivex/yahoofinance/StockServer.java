package com.ahimsa.reactivex.yahoofinance;

import rx.Observable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miguel Reyes
 *         Date: 4/25/15
 *         Time: 9:37 PM
 */
public class StockServer {

    public static Observable<StockInfo> getSimpleFeed(List<String> symbols) {
        List<StockInfo> result = symbols.stream().map(StockInfo::fetch).collect(Collectors.toList());
        return Observable.from(result);
    }

    public static Observable<StockInfo> getFeed(List<String> symbols) {
        return Observable.create(subscriber -> {
            while (!subscriber.isUnsubscribed()) {
                for (String symbol : symbols) {
                    subscriber.onNext(StockInfo.fetch(symbol));
                }
                sleep(1000);
            }
        });
    }

    public static Observable<StockInfo> getFeedFromRequest(List<String> symbols) {
        return Observable.create(subscriber -> {
            subscriber.setProducer(request -> {
                try{
                    int index = (int) request;
                    String symbol = symbols.get(index);
                    subscriber.onNext(StockInfo.fetch(symbol));
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            });
        });
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
