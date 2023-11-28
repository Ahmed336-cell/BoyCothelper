package com.elm.boycothelper.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_products")

public class ProductModel {
    @PrimaryKey(autoGenerate = true)
    private String id;

    private String productId; // Reference to the product ID



    private String name;
    private String imageUrl;
    private String description;
    private String categoryID;
    private boolean favourite;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductModel(String name, String imageUrl, String description, String categoryID) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.categoryID = categoryID;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
