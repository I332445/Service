package com.sap.bulletinboard.ads.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldResource {

    @GET
    @Path("/{name}")
    public String responseMsg(@PathParam("name") String name) {
        return "Welcome: " + name;
    }
}