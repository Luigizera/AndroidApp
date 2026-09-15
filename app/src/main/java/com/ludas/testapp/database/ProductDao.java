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

    @Query("SELECT * FROM products LIMIT :limit OFFSET :offset")
    List<Product> getPaged(int limit, int offset);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<Product> searchByNamePaged(String search, int limit, int offset);

    @Query("SELECT * FROM products WHERE id_product LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<Product> searchByIdPaged(String search, int limit, int offset);

    @Query("SELECT COUNT(*) FROM products")
    int count();

    @Query("SELECT COUNT(*) FROM products WHERE name LIKE '%' || :search || '%'")
    int countSearchByName(String search);

    @Query("SELECT COUNT(*) FROM products WHERE id_product LIKE '%' || :search || '%'")
    int countSearchById(String search);

    @Query("SELECT * FROM products WHERE id_product IN (:ids)")
    List<Product> loadAllByIds(long[] ids);

    @Query("DELETE FROM products WHERE id_product IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM products WHERE id_product = :id")
    Product findById(long id);

    @Insert
    long insert(Product product);

    @Insert
    void insertAll(Product... products);

    @Update
    public void updateProducts(Product... products);
    @Delete
    void delete(Product products);
}
