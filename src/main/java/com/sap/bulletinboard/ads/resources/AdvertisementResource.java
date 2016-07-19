package com.sap.bulletinboard.ads.resources;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import javax.ws.rs.core.MediaType;

import com.sap.bulletinboard.ads.models.Advertisement;

@Path(AdvertisementResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class AdvertisementResource {
   
    public static final String PATH = "api/v1.0/ads";   
    private final Map<Long, Advertisement> ads = new HashMap<Long, Advertisement>();

  /*  public AdvertisementResource(){
        Advertisement ad1 = new Advertisement("ad1");
        Advertisement ad2 = new Advertisement("ad2");
        ads.put((long) 1,ad1);
        ads.put((long) 2,ad2);
    }*/
    
    @GET
    public Iterable<Advertisement> getAll() {
       /* Set set = ads.entrySet();
        Iterator iterator = set.iterator();
        return (Iterable<Advertisement>) iterator;*/
        //return null; //TODO
        return ads.values();
    }

    @GET
    @Path("{id}")
    public Advertisement getSingle(@PathParam("id") long id) {
       /* Set set = ads.entrySet();
        Iterator iterator = set.iterator();
        Advertisement ad = null;
        while(iterator.hasNext()){
            if(((Map.Entry) iterator.next()).getKey().equals(id)){
                ad = (Advertisement) ((Map.Entry) iterator.next()).getValue();
                break;
            }
        }
        return ad;*/
        //return null; //TODO
        if(!ads.containsKey(id)){
            throw new NotFoundException();
        }
        return ads.get(id);
    }

    @POST
    public Response create(Advertisement advertisement) {
        long id = ads.size();
        ads.put(id,advertisement);
        URI location = UriBuilder.fromPath("api/v1.0/ads").path(String.valueOf(id)).build();
        return Response.created(location).entity(advertisement).build();
        
    }
}