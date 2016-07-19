package com.sap.bulletinboard.ads;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

import org.apache.cxf.jaxrs.spring.SpringComponentScanServer;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sap.bulletinboard.ads.resources.AdvertisementResource;
import com.sap.bulletinboard.ads.resources.DefaultResource;
import com.sap.bulletinboard.ads.resources.HelloWorldResource;

public class ContextListener implements ServletContextListener {

    private static final int LOAD_ON_STARTUP = 1; // initialized at initialization time
    private ContextLoaderListener wrappedListener;

    // initialize Spring's application context and register beans
    public ContextListener() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        
        context.registerShutdownHook();

        // ensure that Apache CXF uses registered resource classes
        /*context.register(SpringComponentScanServer.class);
        context.register(DefaultResource.class);*/
        
        context.register(getCoreClasses());
        context.register(getServiceBoundaryClasses());
        
        //---add by vicky---
        /*context.register(AdvertisementResource.class);
        context.register(HelloWorldResource.class);  */
        //---add end---

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
    
    // beans that are registered for all executions 
         protected Class<?>[] getCoreClasses() { 
             List<Class<?>> classes = new ArrayList<>(); 
     
    
             // Utilities 
             // ensure that Apache CXF uses registered resource classes 
             classes.add(SpringComponentScanServer.class); 
             // provides JSON conversation to CXF 
             classes.add(JacksonJsonProvider.class); 
     
    
             // Resources 
             classes.add(DefaultResource.class); 
             //classes.add(HelloWorldResource.class);
             classes.add(AdvertisementResource.class); 
     
     
             return classes.toArray(new Class[classes.size()]); 
         } 
     
     
         // beans that are used for outgoing connections etc. and should not be used in local tests 
         protected Class<?>[] getServiceBoundaryClasses() { 
             List<Class<?>> classes = new ArrayList<>(); 
     
     
             // make environment variables available for Spring's @Value annotation 
             classes.add(PropertySourcesPlaceholderConfigurer.class); 
     
     
             return classes.toArray(new Class[classes.size()]); 
         } 
     } 


