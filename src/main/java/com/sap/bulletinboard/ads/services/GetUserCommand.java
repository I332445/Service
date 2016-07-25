package com.sap.bulletinboard.ads.services;

import javax.inject.Inject;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sap.bulletinboard.ads.services.UserServiceClient.User;

public class GetUserCommand extends HystrixCommand<User> {
    // Hystrix uses a default timeout of 1000 ms, increase in case you run into problems in remote locations
    private static final int DEFAULT_TIMEOUT_MS = 1000;

    private WebTarget webTarget;
    
    private Invocation.Builder requestBuilder;
    
    @Inject
    private Logger logger;

    public GetUserCommand(WebTarget webTarget) {
        super(HystrixCommandGroupKey.Factory.asKey("User"), DEFAULT_TIMEOUT_MS);
        this.webTarget = webTarget;
    }
    
    @Override
    protected User run() throws Exception {
        logger.info("sending request {}", webTarget.getUri());

        //Invocation.Builder requestBuilder = webTarget.request();
        requestBuilder = webTarget.request();
        
        Response response = null;
        try {
            //response = requestBuilder.get();
            response = sendRequest();

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

    // this will be used in exercise 18
    int getTimeoutInMs() {
        return this.properties.executionTimeoutInMilliseconds().get();
    }
    
    protected Response sendRequest(){
        return requestBuilder.get();
    }
}
