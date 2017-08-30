package com.cancizer.springframework.client;

import com.cancizer.springframework.domain.Product;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

public class ReactiveWebClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private ExchangeFunction exchange = ExchangeFunctions.create(new ReactorClientHttpConnector());

    public static void main(String[] args) {
        ReactiveWebClient client = new ReactiveWebClient();
        client.createProduct();
        client.retrieveAllProduct();
    }

    private void createProduct() {
        URI uri = URI.create(String.format("http://%s:%d/product", HOST, PORT));
        Product shirt = new Product(103,
                "Design Patterns",
                "Design Patterns: Elements of Reusable Object-Oriented Software",
                "https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612/ref=pd_sim_14_5?_encoding=UTF8&psc=1&refRID=4N5JCH4TDP7478V1TKMA",
                new BigDecimal("38.99"));
        ClientRequest request = ClientRequest.method(HttpMethod.POST, uri)
                .body(BodyInserters.fromObject(shirt)).build();
        Mono<ClientResponse> response = exchange.exchange(request);
        System.out.println(response.block().statusCode());
    }

    private void retrieveAllProduct() {
        URI uri = URI.create(String.format("http://%s:%d/product", HOST, PORT));
        ClientRequest request = ClientRequest.method(HttpMethod.GET, uri).build();
        Flux<Product> productList = exchange.exchange(request)
                .flatMapMany(response -> response.bodyToFlux(Product.class));
        Mono<List<Product>> productListMono = productList.collectList();
        System.out.println(productListMono.block());
    }
}
