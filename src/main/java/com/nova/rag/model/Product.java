package com.nova.rag.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.ai.document.Document;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.UUID;

@Validated
public class Product {
    @NotNull
    private long id;
    @NotNull
    private long categoryId;
    @NotNull
    @Size(min = 10, max = 250)
    private String name;
    @NotNull
    @Min(0)
    private double price;
    @NotNull
    @Size(min = 10, max = 250)
    private String description;

    public Product() {
    }

    public Product(long id, long categoryId, String name, double price, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Document toDocument() {
        Map<String, Object> metadata = Map.of(
                "id", id,
                "category_id", categoryId,
                "price", price,
                "link", "http://www.cx0222.cn:8080/product/id/" + id
        );
        return new Document(
                new UUID(0, id).toString(),
                name + " " + description,
                metadata
        );
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Product product = (Product) object;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
