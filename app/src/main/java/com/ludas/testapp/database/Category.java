package com.ludas.testapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id_category == category.id_category && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_category, name);
    }
}
