package com.sap.bulletinboard.ads.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class DefaultResource {

    @GET
    public String get() {
        return "OK";
    }
}
