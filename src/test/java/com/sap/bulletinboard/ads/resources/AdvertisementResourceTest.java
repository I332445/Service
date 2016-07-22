package com.sap.bulletinboard.ads.resources;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.junit.Assert.assertThat;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sap.bulletinboard.ads.models.Advertisement;
import com.sap.bulletinboard.ads.server.WebServer;

@Ignore("re-activate after solving exercise 8 part 2")
public class AdvertisementResourceTest {

    private static final String SOME_TITLE = "MyNewAdvertisement";
    private static final String SOME_OTHER_TITLE = "MyOldAdvertisement";
    private static int COUNTER_CREATED_ADS = 0;

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
        Response response = post(SOME_TITLE);
        assertThat(response.getStatus(), is(CREATED.getStatusCode()));

        // retrieve the Java object instance out of the JSON response body
        Advertisement readEntity = response.readEntity(Advertisement.class);
        assertThat(readEntity.getTitle(), is(SOME_TITLE));

        // check that the returned location is correct
        Advertisement readEntityFromLocation = getFromLocation(response.getLocation());
        assertThat(readEntityFromLocation.getTitle(), is(SOME_TITLE));
    }

    @Test
    public void createNullTitle() {
        Response response = post(null);
        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void read_All() {
        post(SOME_TITLE);

        Response response = get("");
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        // check if the number of returned advertisements matches the number of created advertisements
        Advertisement[] readEntities = response.readEntity(Advertisement[].class);
        assertThat(readEntities, is(arrayWithSize(COUNTER_CREATED_ADS)));
    }

    @Test
    public void read_ById_NotFound() {
        Response response = get("4711");
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void read_ById_Negative() {
        Response response = get("-1");
        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void read_ById() {
        String id = createNewAdvertisement();

        Response response = get(id);
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        Advertisement readEntity = response.readEntity(Advertisement.class);
        assertThat(readEntity.getTitle(), is(SOME_TITLE));
    }

    @Test
    public void updateNotFound() {
        Response response = put("4711", new Advertisement());
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void update() {
        Response postResponse = post(SOME_TITLE);

        Advertisement createdEntity = postResponse.readEntity(Advertisement.class);
        createdEntity.setTitle(SOME_OTHER_TITLE);
        String id = getIdFromLocation(postResponse.getLocation());

        Response putResponse = put(id, createdEntity);
        assertThat(putResponse.getStatus(), is(OK.getStatusCode()));
        Advertisement readEntity = putResponse.readEntity(Advertisement.class);

        assertThat(readEntity.getTitle(), is(SOME_OTHER_TITLE));
    }

    @Test
    public void deleteNotFound() {
        Response response = delete("4711");
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void deleteSingle() {
        String id = createNewAdvertisement();

        assertThat(delete(id).getStatus(), is(NO_CONTENT.getStatusCode()));
        assertThat(get(id).getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void deleteAll() {
        String id = createNewAdvertisement();

        assertThat(delete("").getStatus(), is(NO_CONTENT.getStatusCode()));
        assertThat(get(id).getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    private Response post(String title) {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(title);

        // count how many advertisements were created so that this can be checked in the read_All test
        COUNTER_CREATED_ADS += 1;

        // post the advertisement as a JSON entity in the request body
        Entity<Advertisement> entity = Entity.entity(advertisement, MediaType.APPLICATION_JSON);
        return target.request().post(entity);
    }

    private Response delete(String id) {
        return target.path(id).request().delete();
    }

    private Response put(String id, Advertisement createdEntity) {
        return target.path(id).request().put(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
    }

    private String createNewAdvertisement() {
        return getIdFromLocation(post(SOME_TITLE).getLocation());
    }

    private Response get(String path) {
        return target.path(path).request().get();
    }

    private String getIdFromLocation(URI location) {
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private Advertisement getFromLocation(URI location) {
        WebTarget newTarget = server.getWebTarget().path(location.getPath());
        newTarget.register(JacksonJsonProvider.class);

        Response response = newTarget.request().get();
        return response.readEntity(Advertisement.class);
    }
}