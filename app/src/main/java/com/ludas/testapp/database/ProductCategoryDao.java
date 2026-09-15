package com.ludas.testapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ProductCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductCategoryCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductCategoryCrossRef> crossRefs);

    @Delete
    void delete(ProductCategoryCrossRef crossRef);

    @Query("DELETE FROM product_category_cross_ref WHERE id_product = :productId")
    void deleteByProductId(long productId);

    @Query("SELECT COUNT(*) FROM product_category_cross_ref WHERE id_category = :categoryId")
    int countProductsForCategory(long categoryId);

    @Query("SELECT * FROM categories INNER JOIN product_category_cross_ref ON categories.id_category = product_category_cross_ref.id_category WHERE product_category_cross_ref.id_product = :productId")
    List<Category> getCategoriesForProduct(long productId);

    @Query("SELECT * FROM products INNER JOIN product_category_cross_ref ON products.id_product = product_category_cross_ref.id_product WHERE product_category_cross_ref.id_category = :categoryId")
    List<Product> getProductsForCategory(long categoryId);
}
