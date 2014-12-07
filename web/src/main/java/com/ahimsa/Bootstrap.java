package com.ahimsa;

import ahimsa.common.mongo.MongoClientProvider;
import com.ahimsa.services.CoffeeService;
import com.ahimsa.services.CoffeeServiceImpl;
import com.ahimsa.services.MyService;
import com.ahimsa.services.MyServiceImpl;
import com.google.inject.AbstractModule;
import com.mongodb.MongoClient;

import javax.inject.Singleton;

/**
 * @author Miguel Reyes
 *         Date: 12/7/14
 *         Time: 6:45 PM
 */
public class Bootstrap extends AbstractModule {

    @Override
    protected void configure() {
        bind(CoffeeService.class).to(CoffeeServiceImpl.class);
        bind(MyService.class).to(MyServiceImpl.class).in(Singleton.class);
        bind(MongoClient.class).toProvider(MongoClientProvider.class);
    }
}
