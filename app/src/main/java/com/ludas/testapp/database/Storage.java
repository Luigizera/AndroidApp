package com.ludas.testapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "storage", foreignKeys = {@ForeignKey(entity = Product.class,
        parentColumns = "id_product",
        childColumns = "id_product",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
})
public class Storage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_storage")
    private long id_storage;

    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "id_product")
    private long id_product;

    public Storage(int quantity, String location, long id_product) {
        this.quantity = quantity;
        this.location = location;
        this.id_product = id_product;
    }

    public long getId_storage() {
        return id_storage;
    }

    public void setId_storage(long id_storage) {
        this.id_storage = id_storage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getId_product() {
        return id_product;
    }

    public void setId_product(long id_product) {
        this.id_product = id_product;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(id_storage);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return id_storage == storage.id_storage && quantity == storage.quantity && id_product == storage.id_product && Objects.equals(location, storage.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_storage, quantity, location, id_product);
    }
}
