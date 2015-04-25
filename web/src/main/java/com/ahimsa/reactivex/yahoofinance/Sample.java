package com.ahimsa.reactivex.yahoofinance;

import rx.Observable;
import rx.Subscriber;

import java.util.Arrays;
import java.util.List;

/**
 * @author Miguel Reyes
 *         Date: 4/25/15
 *         Time: 9:39 PM
 */
public class Sample {

    public static void main(String[] args) {
        List<String> symbols = Arrays.asList("AMZN", "GOOG", "ORCL");

        //StockServer.getFeed(symbols).subscribe(System.out::println);

        Observable<StockInfo> feed = StockServer.getFeed(symbols);

        /*feed.skipWhile(stockInfo -> stockInfo.value < 40)
                .take(10)
                .subscribe(System.out::println);*/

        /*ExecutorService executorService = Executors.newFixedThreadPool(100);
        feed.subscribeOn(Schedulers.from(executorService))
                .forEach(System.out::println);*/

        /*feed.onErrorResumeNext(throwable -> {
            System.out.println(throwable);
            return StockServer.getFeed(symbols);
        })*/
        feed.subscribe(new Subscriber<StockInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(StockInfo stockInfo) {
                System.out.println(stockInfo);
                // request(index);
                if (stockInfo.ticker.equals("ORCL") && stockInfo.value > 43) {
                    unsubscribe();
                }
            }
        });
    }
}
