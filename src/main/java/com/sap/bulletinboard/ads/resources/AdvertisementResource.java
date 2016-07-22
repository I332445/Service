package com.sap.bulletinboard.ads.resources;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.sap.bulletinboard.ads.models.Advertisement;
import com.sap.bulletinboard.ads.models.AdvertisementRepository;

@Path(AdvertisementResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class AdvertisementResource {
    @Inject
    private AdvertisementRepository ads;
    public static final String PATH = "api/v1.0/ads";

    //private final Map<Long, Advertisement> ads = new HashMap<>();

    @GET
    public Iterable<Advertisement> advertisements() {
        //return ads.values();
        return ads.findAll();
    }

    @GET
    @Path("{id}")
    public Advertisement advertisementById( @PathParam("id") long id) {
        throwIfNonexisting(id);
        //return ads.get(id);
        return ads.findOne(id);
    }

    // combine @NotNull and @Valid as null is regarded as valid, but not desired in this setting
    @POST
    public Response add( Advertisement advertisement) {
        //use map ads
        /*long id = ads.size();
        ads.put(id, advertisement);
        URI location = UriBuilder.fromPath(PATH).path(String.valueOf(id)).build();
        return Response.created(location).entity(advertisement).build();*/
        
        //use postgres
        long id = ads.count();
        Advertisement savedAdvertisement = ads.save(advertisement);
        URI location = UriBuilder.fromPath(PATH).path(String.valueOf(id)).build();
        return Response.created(location).entity(savedAdvertisement).build();
        
    }

    @DELETE
    @Path("{id}")
    public void deleteSingle(@PathParam("id") long id) {
        throwIfNonexisting(id);
        //ads.remove(id);
        
        ads.delete(id);
    }

    @DELETE
    public void deleteAll() {
        //ads.clear();
        
        ads.deleteAll();
    }

    @PUT
    @Path("{id}")
    public Advertisement update(@PathParam("id") long id, Advertisement updatedAd) {
        throwIfNonexisting(id);
       /* ads.put(id, updatedAd);
        return updatedAd;*/
        
       // ads.
        return null;
    }

    private void throwIfNonexisting(long id) {
        /*if (!ads.containsKey(id)) {
            throw new NotFoundException();
        }*/
        if (!ads.exists(id)) {
            throw new NotFoundException();
        }
    }
}