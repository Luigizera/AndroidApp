package com.ludas.testapp.database;

import android.content.Context;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Database(entities = {User.class, Category.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database-str")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
    }

    public static String getCurrentDate() {
        Date d = Calendar.getInstance().getTime();
        return getDateFormat().format(d);
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
