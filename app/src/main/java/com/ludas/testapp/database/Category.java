package com.ludas.testapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_category")
    private long id_category;

    @ColumnInfo(name = "name")
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() { return name; }


    public void setName(String name) {
        this.name = name;
    }

    public long getId_category() {
        return id_category;
    }

    public void setId_category(long id_category) {
        this.id_category = id_category;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
