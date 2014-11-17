package com.demo;

import com.demo.resources.CoffeeShopResource;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * @author Miguel Reyes
 *         Date: 11/14/14
 *         Time: 4:07 PM
 */
public class CoffeeShopService extends Service<ServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new CoffeeShopService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.setName("coffee-app");
        AssetsBundle bundle = new AssetsBundle("/html", "/");
        bootstrap.addBundle(bundle);
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {
        MongoClient client = new MongoClient();
        DB database = client.getDB("CoffeeShop");
        environment.addResource(new CoffeeShopResource(database));
        environment.manage(new MongoClientManager(client));
    }
}
