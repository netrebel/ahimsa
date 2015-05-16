package com.ahimsa.resources;

import com.ahimsa.models.Order;
import com.ahimsa.services.CoffeeService;
import com.ahimsa.services.MyService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Miguel Reyes
 *         Date: 11/14/14
 *         Time: 4:09 PM
 */
@Path("/coffeeshop")
public class CoffeeShopResource {

    //    private final DB database;
    @Inject
    private MyService myService;

    @Inject
    private CoffeeService coffeeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String welcome() {
        return myService.hello();
    }

    @POST
    @Path("/{id}/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Order placeMyOrder(@PathParam("id") long coffeeShopId, Order order) {
        order.coffeeShopId = coffeeShopId;
        Order savedOrder = coffeeService.insert(order);
        return savedOrder;
    }

}
