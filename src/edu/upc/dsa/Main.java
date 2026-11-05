package edu.upc.dsa;

import io.swagger.jaxrs.config.BeanConfig;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    public static final String BASE_URI = "http://localhost:8080/dsawm/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("edu.upc.dsa.services");

        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setResourcePackage("edu.upc.dsa.services");
        beanConfig.setBasePath("/dsawm");
        beanConfig.setTitle("DSA Drones REST API");
        beanConfig.setDescription("API REST para la gestión de flota de drones, pilotos y planes de vuelo");
        beanConfig.setScan(true);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        StaticHttpHandler staticHttpHandler = new StaticHttpHandler("public");
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");

        return server;
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        logger.info(String.format("Servidor Jersey iniciado en %s", BASE_URI));
        logger.info("Accede a la documentación Swagger UI en: http://localhost:8080/swagger/");
        logger.info("Pulsa Enter para detener el servidor...");
        System.in.read();
        server.shutdownNow();
        logger.info("Servidor detenido.");
    }
}