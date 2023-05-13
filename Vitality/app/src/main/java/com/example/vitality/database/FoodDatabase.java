package com.example.vitality.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.vitality.dao.FoodDAO;
import com.example.vitality.entity.Diet;
import com.example.vitality.entity.Food;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {
    // Abstract method to get the Data Access
    // Object interface for the Diet table
    public abstract FoodDAO foodDao();

    private static FoodDatabase INSTANCE;

    // We create an ExecutorService with a fixed thread pool so we can later run
    // database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 8;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // A synchronized method in a multi-threaded environment means that two threads
    // are not allowed to access data at the same time
    public static synchronized FoodDatabase getInstance(final android.content.Context context) {
        if (INSTANCE == null) {
            INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                            FoodDatabase.class, "FoodDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }


}
