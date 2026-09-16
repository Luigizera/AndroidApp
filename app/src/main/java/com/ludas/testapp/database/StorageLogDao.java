package com.ludas.testapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StorageLogDao {
    @Query("SELECT * FROM storage_log")
    List<StorageLog> getAll();

    @Query("SELECT * FROM storage_log LIMIT :limit OFFSET :offset")
    List<StorageLog> getPaged(int limit, int offset);

    @Query("SELECT * FROM storage_log WHERE id_storage_log LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<StorageLog> searchByIdPaged(String search, int limit, int offset);

    @Query("SELECT * FROM storage_log WHERE date LIKE '%' || :search || '%' LIMIT :limit OFFSET :offset")
    List<StorageLog> searchByDatePaged(String search, int limit, int offset);

    @Query("SELECT COUNT(*) FROM storage_log")
    int count();

    @Query("SELECT COUNT(*) FROM storage_log WHERE id_storage_log LIKE '%' || :search || '%'")
    int countSearchById(String search);

    @Query("SELECT COUNT(*) FROM storage_log WHERE date LIKE '%' || :search || '%'")
    int countSearchByDate(String search);

    @Query("SELECT * FROM storage_log WHERE id_storage_log IN (:ids)")
    List<StorageLog> loadAllByIds(long[] ids);

    @Query("DELETE FROM storage_log WHERE id_storage_log IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM storage_log WHERE id_storage_log = :id")
    StorageLog findById(long id);

    @Query("SELECT COUNT(*) FROM storage_log WHERE id_storage = :storageId")
    int countByStorageId(long storageId);

    @Query("SELECT sl.date, sl.type, sl.quantity, p.price, p.purchase_price as purchasePrice " +
           "FROM storage_log sl " +
           "INNER JOIN storage s ON sl.id_storage = s.id_storage " +
           "INNER JOIN products p ON s.id_product = p.id_product")
    List<FinancialEntry> getFinancialEntries();

    @Insert
    void insertAll(StorageLog... storageLogs);
    @Update
    public void updateStorages(StorageLog... storageLogs);
    @Delete
    void delete(StorageLog storageLogs);
}
