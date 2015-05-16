package com.ahimsa.services;

import ahimsa.mongo.MongoCollection;
import ahimsa.mongo.MongoConnection;
import com.ahimsa.models.Order;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

/**
 * @author Miguel Reyes
 *         Date: 11/19/14
 *         Time: 4:20 PM
 */
@Singleton
public class CoffeeServiceImpl implements CoffeeService {

    private MongoCollection ordersCollection;

    @Inject
    public CoffeeServiceImpl(MongoConnection mongoConnection) {
        ordersCollection = mongoConnection.getDatabase("CoffeeShop").getCollection("orders");
    }

    public Order insert(Order order) {
        String id = newId();
        order._id = id;
        ordersCollection.insertEntity(order);
        return findById(id);
    }

    public Order findById(String id) {
        return ordersCollection.findOneById(id).as(Order.class);
    }

    private String newId() {
        return UUID.randomUUID().toString();
    }

}
