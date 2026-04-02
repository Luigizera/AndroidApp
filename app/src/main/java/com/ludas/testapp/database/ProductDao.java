package com.ludas.testapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE id IN (:ids)")
    List<Product> loadAllByIds(long[] ids);

    @Query("DELETE FROM products WHERE id IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM products WHERE id = :id")
    Product findById(long id);

    @Query("SELECT * FROM products WHERE name LIKE :name")
    List<Product> findByName(String name);

    @Query("SELECT * FROM products WHERE price BETWEEN :min AND :max")
    List<Product> findByPrice(long min, long max);

    @Query("SELECT * FROM products WHERE description LIKE :description")
    List<Product> findByDescription(String description);

    @Query("SELECT * FROM products WHERE id_category = :id_category")
    Product findByCategory(long id_category);

    @Insert
    void insertAll(Product... products);

    @Update
    public void updateProducts(Product... products);
    @Delete
    void delete(Product products);
}
