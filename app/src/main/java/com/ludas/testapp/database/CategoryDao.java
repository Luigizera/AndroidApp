package com.ludas.testapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id IN (:ids)")
    List<Category> loadAllByIds(long[] ids);

    @Query("DELETE FROM categories WHERE id IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM categories WHERE id = :id")
    Category findById(long id);

    @Query("SELECT * FROM categories WHERE name LIKE :name")
    List<Category> findByName(String name);

    @Query("SELECT * FROM categories WHERE name = :name")
    Category findByNameEquals(String name);

    @Insert
    void insertAll(Category... categories);

    @Update
    public void updateCategories(Category... categories);
    @Delete
    void delete(Category category);
}
