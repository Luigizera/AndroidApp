package com.ludas.testapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products", foreignKeys = {@ForeignKey(entity = Category.class,
        parentColumns = "id_category",
        childColumns = "id_category",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
})
public class Product {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "id_category")
    private long id_category;

    public Product(String name, String description, double price, long id_category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.id_category = id_category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getId_category() {
        return id_category;
    }

    public void setId_category(long id_category) {
        this.id_category = id_category;
    }
}
