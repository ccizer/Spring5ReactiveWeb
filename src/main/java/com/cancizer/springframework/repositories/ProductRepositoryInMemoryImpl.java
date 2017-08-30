package com.cancizer.springframework.repositories;

import com.cancizer.springframework.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductRepositoryInMemoryImpl implements ProductRepository {

    private final Map<Integer, Product> productMap = new ConcurrentHashMap<>();

    public ProductRepositoryInMemoryImpl() {
        this.productMap.put(1, new Product(101,
                "Clean Code",
                "Clean Code: A Handbook of Agile Software Craftsmanship",
                "https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882/ref=sr_1_1?ie=UTF8&qid=1504096183&sr=8-1&keywords=clean+code",
                new BigDecimal("33.53")));

        this.productMap.put(2, new Product(102,
                "The Pragmatic Programmer",
                "The Pragmatic Programmer: From Journeyman to Master",
                "https://www.amazon.com/Pragmatic-Programmer-Journeyman-Master/dp/020161622X/ref=pd_sim_14_3?_encoding=UTF8&psc=1&refRID=J1KR7R93QSZ0F7ZQ557V",
                new BigDecimal("33.90")));
    }

    @Override
    public Mono<Product> getProduct(int id) {
        return Mono.justOrEmpty(this.productMap.get(id));
    }

    @Override
    public Flux<Product> getAllProducts() {
        return Flux.fromIterable(this.productMap.values());
    }

    @Override
    public Mono<Void> saveProduct(Mono<Product> productMono) {
        Mono<Product> pMono = productMono.doOnNext( product -> {
            int id = productMap.size() + 1;
            productMap.put(id, product);
            System.out.format("Saved %s with id %d%n", product, id);
        });

        return pMono.thenEmpty(Mono.empty());
    }
}
