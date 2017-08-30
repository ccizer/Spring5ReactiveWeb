package com.cancizer.springframework.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Product {

    private int productId;
    private String productName;
    private String description;
    private String url;
    private BigDecimal price;

    public Product(@JsonProperty("id") int productId,
                   @JsonProperty("name") String productName,
                   @JsonProperty("description") String description,
                   @JsonProperty("url") String url,
                   @JsonProperty("price") BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.url = url;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", price=" + price +
                '}';
    }
}
