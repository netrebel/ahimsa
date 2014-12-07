package com.ahimsa.services;

import com.ahimsa.models.Order;

/**
 * @author Miguel Reyes
 *         Date: 12/7/14
 *         Time: 7:17 PM
 */
public interface CoffeeService {

    public Order insert(Order order);
    public Order findById(String id);

}
