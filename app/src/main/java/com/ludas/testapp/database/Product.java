package com.ludas.testapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_product")
    private long id_product;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "purchase_price")
    private double purchasePrice;

    @ColumnInfo(name = "min_stock_level")
    private int minStockLevel;

    public Product(String name, String description, double price, double purchasePrice, int minStockLevel) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.purchasePrice = purchasePrice;
        this.minStockLevel = minStockLevel;
    }

    public long getId_product() {
        return id_product;
    }

    public void setId_product(long id_product) {
        this.id_product = id_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id_product == product.id_product && Double.compare(price, product.price) == 0 && 
                Double.compare(purchasePrice, product.purchasePrice) == 0 && 
                minStockLevel == product.minStockLevel && 
                Objects.equals(name, product.name) && 
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_product, name, description, price, purchasePrice, minStockLevel);
    }
}
