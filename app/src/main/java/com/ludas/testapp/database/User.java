package com.ludas.testapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    /*@PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;*/
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;

    public User(String date, String firstName, String lastName) {
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getDate() { return date; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
