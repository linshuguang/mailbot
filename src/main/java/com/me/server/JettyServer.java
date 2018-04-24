package com.me.server;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

/**
 * Created by kenya on 2017/12/14.
 */
public class JettyServer {
    private final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);



    //private final int PORT = 8093;
    //private final String CONTEXT_PATH = "/";
    private static final String CONFIG_LOCATION_PACKAGE = "com.me.config";
    //private final String MAPPING_URL = "/";
    //private final String WEBAPP_DIRECTORY = "webapp";
    private final String CONTEXT_PATH = "/";
    private final String MAPPING_URL = "/";
    private final String WEBAPP_DIRECTORY = "src/main/webapp";



    public void start(int port) throws Exception {
        LOGGER.debug("Starting server at port {}", port);
        Server server = new Server(port);

        server.setHandler(makeMyHandler());

        server.start();
        LOGGER.info("Server started at port {}", port);
        server.join();
    }

    private ServletContextHandler makeMyHandler() throws IOException {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS); // SESSIONS requerido para JSP
        handler.setErrorHandler(null);

        handler.setResourceBase(new ClassPathResource(WEBAPP_DIRECTORY).getURI().toString());
        handler.setContextPath(CONTEXT_PATH);

        // JSP
        handler.setClassLoader(Thread.currentThread().getContextClassLoader()); // Necesario para cargar JspServlet
        handler.addServlet(JspServlet.class, "*.jsp");

        // Spring
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION_PACKAGE);
        WebApplicationContext webAppContext =  context;

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webAppContext);
        ServletHolder springServletHolder = new ServletHolder("mvc-dispatcher", dispatcherServlet);
        handler.addServlet(springServletHolder, MAPPING_URL);
        handler.addEventListener(new ContextLoaderListener(webAppContext));

        return handler;
    }

    public static void main(String[] args) throws Exception {
        JettyServer jettyServer = new JettyServer();
        jettyServer.start(8096);
    }

}
