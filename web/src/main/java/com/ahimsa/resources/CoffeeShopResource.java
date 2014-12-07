package com.ahimsa.resources;

import com.ahimsa.models.Order;
import com.ahimsa.services.CoffeeService;
import com.ahimsa.services.MyService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

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
    public Response welcome() {
        return Response.ok(myService.hello()).build();
    }

    @POST
    @Path("/{id}/order")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeMyOrder(@PathParam("id") long coffeeShopId, Order order) {

        order.coffeeShopId = coffeeShopId;

        Order savedOder = coffeeService.insert(order);
        return Response.created(URI.create(savedOder._id)).entity(savedOder).build();
    }

}
