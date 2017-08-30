package com.cancizer.springframework.server;

import com.cancizer.springframework.handlers.ProductHandler;
import com.cancizer.springframework.handlers.ProductHandlerImpl;
import com.cancizer.springframework.repositories.ProductRepository;
import com.cancizer.springframework.repositories.ProductRepositoryInMemoryImpl;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

public class Server {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.startTomcatServer("localhost", 8080);
        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    private void startTomcatServer(String host, int port) throws LifecycleException {
        RouterFunction<?> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);
        Tomcat tomcatServer = new Tomcat();
        tomcatServer.setHostname(host);
        tomcatServer.setPort(port);
        Context rootContext = tomcatServer.addContext("", System.getProperty("java.io.tmpdir"));
        ServletHttpHandlerAdapter servlet = new ServletHttpHandlerAdapter(httpHandler);
        Tomcat.addServlet(rootContext, "httpHandlerServlet", servlet);
        rootContext.addServletMapping("/", "httpHandlerServlet");
        tomcatServer.start();
    }

    private RouterFunction<ServerResponse> routingFunction() {
        ProductRepository repository = new ProductRepositoryInMemoryImpl();
        ProductHandler handler = new ProductHandlerImpl(repository);

        return nest(path("/product"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/{id}"), handler::getProductFromRepository)
                                .andRoute(method(HttpMethod.GET), handler::getAllProductsFromRepository)
                ).andRoute(POST("/")
                        .and(contentType(APPLICATION_JSON)), handler::saveProductToRepository));
    }

    // For Netty Server
    public void startReactorServer(String host, int port) {
        RouterFunction route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(host, port);
        server.newHandler(adapter).block();
    }
}
