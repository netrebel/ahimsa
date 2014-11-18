package com.ahimsa;

import com.mongodb.MongoClient;
import com.yammer.dropwizard.lifecycle.Managed;

/**
 * @author Miguel Reyes
 *         Date: 11/17/14
 *         Time: 3:27 PM
 */
public class MongoClientManager implements Managed {

    private final MongoClient mongoClient;

    public MongoClientManager(MongoClient client) {
        this.mongoClient = client;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        mongoClient.close();
    }
}
