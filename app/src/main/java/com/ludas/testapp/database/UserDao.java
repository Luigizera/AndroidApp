package com.ludas.testapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE date IN (:dates)")
    List<User> loadAllByDates(String[] dates);

    @Query("SELECT * FROM users WHERE date IN (:date)")
    User findByDate(String date);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND last_name LIKE :last")
    List<User> findByName(String first, String last);

    @Query("DELETE FROM users WHERE date IN (:date)")
    void deleteByDate(String date);

    @Insert
    void insertAll(User... users);

    @Update
    public void updateUsers(User... users);
    @Delete
    void delete(User user);
}