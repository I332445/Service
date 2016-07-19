package com.sap.bulletinboard.ads.resources;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sap.bulletinboard.ads.models.Advertisement;
import com.sap.bulletinboard.ads.server.WebServer;

public class AdvertisementResourceTest {

    private static final String SOME_TITLE = "MyNewAdvertisement";

    private WebTarget target;

    @ClassRule
    public static WebServer server = new WebServer();

    @Before
    public void setUp() throws Exception {
        // provide a WebTarget object that points to the application running on the started Tomcat server
        target = server.getWebTarget().path("api/v1.0/ads");
        // register Jackson for JSON conversion
        target.register(JacksonJsonProvider.class);
    }

    @Test
    public void create() {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(SOME_TITLE);

        // post the advertisement as a JSON entity in the request body
        Entity<Advertisement> entity = Entity.entity(advertisement, MediaType.APPLICATION_JSON);
        Response response = target.request().post(entity);

        assertThat(response.getStatus(), is(CREATED.getStatusCode()));

        // retrieve the Java object instance out of the JSON response body
        Advertisement readEntity = response.readEntity(Advertisement.class);
        assertThat(readEntity.getTitle(), is(SOME_TITLE));
    }

    @Test
    public void read_All() {
        fail("not implemented");
        // TODO create new advertisement using POST, then retrieve all advertisements using GET to /
        // assertThat(response.getStatus(), is(OK.getStatusCode()));
        // assertThat(readEntities[0].getTitle(), is(SOME_TITLE));
    }

    @Test
    public void read_ById_NotFound() {
        fail("not implemented");
        // TODO try to retrieve object with nonexisting ID using GET request to /99
        // assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void read_ById() {
        fail("not implemented");
        // TODO create new advertisement using POST, then retrieve it using GET
        // assertThat(response.getStatus(), is(OK.getStatusCode()));
        // assertThat(readEntity.getTitle(), is(SOME_TITLE));
    }
}

