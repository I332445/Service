package com.sap.bulletinboard.ads.services;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class UserServiceClient {

    private static final String RESOURCE_NAME = "api/v1.0/users";

    // Using Spring's PropertySourcesPlaceholderConfigurer bean, get the content of the USER_ROUTE environment variable
    @Value("${USER_ROUTE}")
    private String userServiceRoute;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public boolean isPremiumUser(String id) {
        /*
         * The Client and Response classes currently do not implement AutoCloseable. This will be added with JAX-RS 2.1.
         * See https://www.jcp.org/en/jsr/detail?id=370
         */
        Client client = null;
        try {
            client = createClient();
            WebTarget webTarget = client.target(userServiceRoute).path(RESOURCE_NAME + "/" + id);

            //User user = sendRequest(webTarget);
            User user = new GetUserCommand(webTarget).execute();

            return user.premiumUser;
        } finally {
            // It is necessary to react on errors by closing the object to avoid leaks
            if (client != null) {
                client.close();
            }
        }
    }

    private User sendRequest(WebTarget webTarget) {
        logger.info("sending request {}", webTarget.getUri());

        Invocation.Builder requestBuilder = webTarget.request();

        Response response = null;
        try {
            response = requestBuilder.get();

            if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
                logger.warn("received HTTP status code: {}", response.getStatus());
                throw new IllegalStateException();
            }
            logger.info("received response, status code: {}", response.getStatus());

            return response.readEntity(User.class);
        } finally {
            // It is necessary to react on errors by closing the object to avoid leaks.
            if (response != null) {
                response.close();
            }
        }
    }

    private Client createClient() {
        Client client = ClientBuilder.newClient();
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        client.register(jsonProvider);
        return client;
    }

    public static class User {
        // public, so that Jackson can access the field
        public boolean premiumUser;
    }
}
