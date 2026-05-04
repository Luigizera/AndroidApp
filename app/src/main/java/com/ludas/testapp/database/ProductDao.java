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

    @Query("SELECT * FROM products WHERE id_product IN (:ids)")
    List<Product> loadAllByIds(long[] ids);

    @Query("DELETE FROM products WHERE id_product IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM products WHERE id_product = :id")
    Product findById(long id);

    @Query("SELECT * FROM products WHERE id_category = :id_category")
    Product findByCategory(long id_category);

    @Insert
    void insertAll(Product... products);

    @Update
    public void updateProducts(Product... products);
    @Delete
    void delete(Product products);
}
