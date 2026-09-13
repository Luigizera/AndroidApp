package com.ludas.testapp.database;

import android.database.Cursor;

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

    @Query("SELECT * FROM categories LIMIT :limit OFFSET :offset")
    List<Category> getPaged(int limit, int offset);

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :search || '%'")
    List<Category> search(String search);

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<Category> searchPaged(String search, int limit, int offset);

    @Query("SELECT COUNT(*) FROM categories")
    int count();

    @Query("SELECT COUNT(*) FROM categories WHERE name LIKE '%' || :search || '%'")
    int countSearch(String search);

    @Query("SELECT * FROM categories WHERE id_category = :id LIMIT :limit OFFSET :offset")
    List<Category> searchByIdPaged(long id, int limit, int offset);

    @Query("SELECT COUNT(*) FROM categories WHERE id_category = :id")
    int countSearchById(long id);

    @Query("DELETE FROM categories WHERE id_category IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM categories WHERE id_category = :id")
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
