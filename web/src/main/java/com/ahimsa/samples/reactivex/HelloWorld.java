package com.ahimsa.samples.reactivex;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Miguel Reyes
 *         Date: 4/15/15
 *         Time: 5:28 PM
 */
public class HelloWorld {

    public static void main(String[] args) {
        createObservable();
    }

    private static void createObservable() {
        Observable.create(subscriber -> {
            subscriber.onNext("Hello first");
            subscriber.onNext("Miguel");
            subscriber.onCompleted();
        }).subscribe(System.out::println);

        //
        Observable.just("Hello Just").subscribe(System.out::println);

        //Print stack trace in case of error
        Observable.just("Stack trace error").subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("done"));

        //Add error propagation
        Observable.create(subscriber -> {
            try {
                subscriber.onNext("Hello Propagate");
                subscriber.onNext(new RuntimeException());
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribe(System.out::println);

        //Add concurrency (Not recommended)
        Observable.create(subscriber -> {
            new Thread(() -> {
                try {
                    subscriber.onNext("Hello Concurrent");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }).start();
        }).subscribe(System.out::println);

        //Add concurrency (using scheduler)
        Observable.create(subscriber -> {
            try {
                subscriber.onNext("Hello Scheduler");
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io()).subscribe(System.out::println);

    }

}
