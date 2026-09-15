package com.ludas.testapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "storage_log", foreignKeys = {@ForeignKey(entity = Storage.class,
        parentColumns = "id_storage",
        childColumns = "id_storage",
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE)
})
public class StorageLog {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_storage_log")
    private long id_storage_log;

    @ColumnInfo(name = "id_storage")
    private long id_storage;

    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "date")
    private String date;

    public StorageLog(int quantity, int type, String date, long id_storage) {
        this.quantity = quantity;
        this.type = type;
        this.date = date;
        this.id_storage = id_storage;
    }

    public long getId_storage_log() {
        return id_storage_log;
    }

    public void setId_storage_log(long id_storage_log) {
        this.id_storage_log = id_storage_log;
    }

    public long getId_storage() {
        return id_storage;
    }

    public void setId_storage(long id_storage) {
        this.id_storage = id_storage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.valueOf(id_storage_log);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StorageLog that = (StorageLog) o;
        return id_storage_log == that.id_storage_log && id_storage == that.id_storage && quantity == that.quantity && type == that.type && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_storage_log, id_storage, quantity, type, date);
    }
}
