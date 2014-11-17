package com.demo.resources;

import com.demo.models.Order;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

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
@Produces(MediaType.APPLICATION_JSON)
public class CoffeeShopResource {

    private final DB database;

    public CoffeeShopResource(final DB database) {
        this.database = database;
    }

    @GET
    public Response welcome() {
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/order")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeMyOrder(@PathParam("id") long coffeeShopId, Order order) {

        order.coffeeShopId = coffeeShopId;

        DBCollection orders = database.getCollection("orders");
        JacksonDBCollection<Order, String> collection = JacksonDBCollection.wrap(orders, Order.class, String.class);

        WriteResult<Order, String> result = collection.insert(order);
        if (result == null) {
            return Response.serverError().build();
        }
        order.id = result.getSavedId();
        return Response.created(URI.create(order.id)).entity(order).build();
    }

}
