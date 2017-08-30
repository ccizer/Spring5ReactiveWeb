package com.cancizer.springframework.handlers;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ProductHandler {
    Mono<ServerResponse> getProductFromRepository(ServerRequest request);

    Mono<ServerResponse> saveProductToRepository(ServerRequest request);

    Mono<ServerResponse> getAllProductsFromRepository(ServerRequest request);
}
