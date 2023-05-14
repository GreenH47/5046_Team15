package com.example.vitality.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vitality.dao.DietDAO;
import com.example.vitality.dao.ExerciseDao;
import com.example.vitality.entity.Diet;
import com.example.vitality.entity.ExerciseEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Diet.class, ExerciseEntity.class}, version = 1, exportSchema = false)
public abstract class DietDatabase  extends RoomDatabase {
    // Abstract method to get the Data Access
    // Object interface for the Diet table
    public abstract DietDAO dietDao();
    public abstract ExerciseDao exerciseDao();
    // A static DietDatabase INSTANCE for Singleton database operation
    //singleton pattern to prevent having multiple instances of the database created
    //and opened at the same time
    private static DietDatabase INSTANCE;

    // We create an ExecutorService with a fixed thread pool so we can later run
    // database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // A synchronized method in a multi-threaded environment means that two threads
    // are not allowed to access data at the same time
    public static synchronized DietDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DietDatabase.class, "DietDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
