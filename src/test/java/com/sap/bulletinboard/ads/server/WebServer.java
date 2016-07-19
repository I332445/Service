package com.sap.bulletinboard.ads.server;

import java.io.File;

import javax.servlet.ServletException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.rules.ExternalResource;

// This class starts an embedded Tomcat server and is implemented as a JUnit rule, so that the server can be started
// before each test (@Rule) or before the first test (@ClassRule), and is stopped after each test / after the last test.
// The application will be made available at http://localhost:x/ where x is a random (free) port. A WebTarget object
// pointing to this location can be retrieved using the getWebTarget() method.
public class WebServer extends ExternalResource {

    private static final String BASE_DIRECTORY = "embedded_tomcat"; // contains temporary files
    private static final int USE_FREE_PORT = 0;
    private static final String CONTEXT_PATH = ""; // root (/)
    private Tomcat tomcat;
    private WebTarget webTarget;

    public WebTarget getWebTarget() {
        return webTarget;
    }

    @Override
    protected void before() throws Throwable {
        startEmbeddedTomcat();
        webTarget = createWebTarget();
    }

    @Override
    protected void after() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    private WebTarget createWebTarget() {
        Client client = ClientBuilder.newClient();
        String basePath = "http://localhost:" + tomcat.getConnector().getLocalPort() + CONTEXT_PATH;
        return client.target(basePath);
    }

    private void startEmbeddedTomcat() throws ServletException, LifecycleException {
        String pathToWebXML = new File("src/main/webapp/").getAbsolutePath();

        tomcat = new Tomcat();
        tomcat.setBaseDir(BASE_DIRECTORY);
        tomcat.setPort(USE_FREE_PORT);
        tomcat.addWebapp(CONTEXT_PATH, pathToWebXML);
        tomcat.start();
    }
}

