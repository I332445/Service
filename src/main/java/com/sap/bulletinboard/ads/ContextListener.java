package com.sap.bulletinboard.ads;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

import org.apache.cxf.jaxrs.spring.SpringComponentScanServer;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.sap.bulletinboard.ads.resources.DefaultResource;

public class ContextListener implements ServletContextListener {

    private static final int LOAD_ON_STARTUP = 1; // initialized at initialization time
    private ContextLoaderListener wrappedListener;

    // initialize Spring's application context and register beans
    public ContextListener() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.registerShutdownHook();

        // ensure that Apache CXF uses registered resource classes
        context.register(SpringComponentScanServer.class);
        context.register(DefaultResource.class);

        context.refresh();
        wrappedListener = new ContextLoaderListener(context);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // register Apache CXF as servlet
        ServletRegistration.Dynamic dispatcher = event.getServletContext().addServlet("CXFServlet", CXFServlet.class);
        dispatcher.setLoadOnStartup(LOAD_ON_STARTUP);
        dispatcher.addMapping("/*");

        // necessary to make Apache CXF work with Spring
        // see https://stackoverflow.com/questions/34499352/why-do-i-need-to-set-root-web-application-context-attribute-in-servletcontext/34499567#34499567
        wrappedListener.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        wrappedListener.contextDestroyed(event);
    }
}
