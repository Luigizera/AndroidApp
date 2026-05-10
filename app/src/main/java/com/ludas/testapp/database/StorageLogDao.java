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

    @Query("SELECT * FROM storage_log WHERE id_storage_log IN (:ids)")
    List<StorageLog> loadAllByIds(long[] ids);

    @Query("DELETE FROM storage_log WHERE id_storage_log IN (:id)")
    void deleteById(long id);

    @Query("SELECT * FROM storage_log WHERE id_storage_log = :id")
    StorageLog findById(long id);

    @Insert
    void insertAll(StorageLog... storageLogs);
    @Update
    public void updateStorages(StorageLog... storageLogs);
    @Delete
    void delete(StorageLog storageLogs);
}
