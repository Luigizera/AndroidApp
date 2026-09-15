package com.ludas.testapp.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "product_category_cross_ref",
    primaryKeys = {"id_product", "id_category"},
    foreignKeys = {
        @ForeignKey(
            entity = Product.class,
            parentColumns = "id_product",
            childColumns = "id_product",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Category.class,
            parentColumns = "id_category",
            childColumns = "id_category",
            onDelete = ForeignKey.RESTRICT
        )
    },
    indices = {
        @Index(value = {"id_product"}),
        @Index(value = {"id_category"})
    }
)
public class ProductCategoryCrossRef {
    private long id_product;
    private long id_category;

    public ProductCategoryCrossRef(long id_product, long id_category) {
        this.id_product = id_product;
        this.id_category = id_category;
    }

    public long getId_product() {
        return id_product;
    }

    public void setId_product(long id_product) {
        this.id_product = id_product;
    }

    public long getId_category() {
        return id_category;
    }

    public void setId_category(long id_category) {
        this.id_category = id_category;
    }
}
