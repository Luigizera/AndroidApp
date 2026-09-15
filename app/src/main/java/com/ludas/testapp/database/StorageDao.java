package com.ludas.testapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StorageDao {
    @Query("SELECT * FROM storage")
    List<Storage> getAll();

    @Query("SELECT * FROM storage LIMIT :limit OFFSET :offset")
    List<Storage> getPaged(int limit, int offset);

    @Query("SELECT * FROM storage WHERE location LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<Storage> searchByLocationPaged(String search, int limit, int offset);

    @Query("SELECT * FROM storage WHERE id_storage LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<Storage> searchByIdPaged(String search, int limit, int offset);

    @Query("SELECT COUNT(*) FROM storage")
    int count();

    @Query("SELECT COUNT(*) FROM storage WHERE location LIKE '%' || :search || '%'")
    int countSearchByLocation(String search);

    @Query("SELECT COUNT(*) FROM storage WHERE id_storage LIKE '%' || :search || '%'")
    int countSearchById(String search);

    @Query("SELECT * FROM storage WHERE id_storage IN (:ids)")
    List<Storage> loadAllByIds(long[] ids);

    @Query("DELETE FROM storage WHERE id_storage IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM storage WHERE id_storage = :id")
    Storage findById(long id);

    @Query("SELECT * FROM storage WHERE location LIKE :location")
    List<Storage> findByLocation(String location);

    @Query("SELECT * FROM storage WHERE quantity BETWEEN :min AND :max")
    List<Storage> findByQuantity(long min, long max);

    @Query("SELECT * FROM storage WHERE id_product = :id_product")
    Storage findByProduct(long id_product);

    @Query("SELECT COUNT(*) FROM storage WHERE id_product = :productId")
    int countByProductId(long productId);

    @Query("SELECT * FROM storage WHERE id_product = :id_product AND quantity = :quantity AND location = :location LIMIT 1")
    Storage find(long quantity, String location, long id_product);

    @Insert
    void insertAll(Storage... storages);

    @Update
    public void updateStorages(Storage... storages);
    @Delete
    void delete(Storage storages);
}
