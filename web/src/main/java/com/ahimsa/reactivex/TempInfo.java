package com.ahimsa.reactivex;

import rx.Observable;
import rx.Observer;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Miguel Reyes
 *         Date: 4/15/15
 *         Time: 4:56 PM
 */
public class TempInfo {

    public static final Random random = new Random();
    public final String town;
    public final int temp;

    public TempInfo(String town, int temp) {
        this.town = town;
        this.temp = temp;
    }

    public static TempInfo fetch(String temp) {
        return new TempInfo(temp, random.nextInt(70) - 20);
    }

    @Override
    public String toString() {
        return town + " : " + temp;
    }

    //Creating Observables with just a single value
    public static Observable<TempInfo> getTemp(String town) {
        return Observable.just(TempInfo.fetch(town));
    }

    //Creating Observables from an Iterable
    public static Observable<TempInfo> getTemps(String... towns) {
        return Observable.from(Stream.of(towns)
                .map(TempInfo::fetch)
                .collect(toList()));
    }

    //Creating Observables from another Observable
    public static Observable<TempInfo> getFeedFromObservable(String town) {
        return Observable.create(subscriber -> {
            while (true) {
                subscriber.onNext(TempInfo.fetch(town));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Combining Observables: Subscribing one Observable to another
    public static Observable<TempInfo> getFeed(String town) {
        return Observable.create(
                subscriber -> Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(i -> subscriber
                                .onNext(TempInfo.fetch(town))));
    }

    //Merging more Observables
    public static Observable<TempInfo> getFeeds(String... towns) {
        return Observable.merge(Arrays.stream(towns)
                .map(TempInfo::getFeed).collect(toList()));
    }

    //Managing errors and completion
    public static Observable<TempInfo> getFeeds(String town) {
        return Observable.create(subscriber ->
                Observable.interval(1, TimeUnit.SECONDS).subscribe(i -> {
                    if (i > 5) {
                        subscriber.onCompleted();
                    }
                    try {
                        subscriber.onNext(TempInfo.fetch(town));
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }));
    }

    public static void main(String[] args) {
        Observable<TempInfo> feed = getTemps("Milano", "Roma", "Napoli");

        feed.subscribe(new Observer<TempInfo>() {
            public void onCompleted() {
                System.out.println("Done!");
            }

            public void onError(Throwable t) {
                System.out.println("Got problem: " + t);
            }

            public void onNext(TempInfo t) {
                System.out.println(t);
            }

        });
    }
}