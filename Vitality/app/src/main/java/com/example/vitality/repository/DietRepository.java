package com.example.vitality.repository;
import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.vitality.dao.DietDAO;
import com.example.vitality.database.DietDatabase;
import com.example.vitality.entity.Diet;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DietRepository {
    // The DAO interface for Diet table
    private DietDAO dietDao;

    //get all diets LiveData
    private LiveData<List<Diet>> allDiets;

    //constructor that initializes the DietDatabase instance and the dietDao.
    // allDiets holds a LiveData which holds a list of all the diets.
    public DietRepository(Application application) {
        // Access the database instance and get the DAO interface for Diet table
        DietDatabase db = DietDatabase.getInstance(application);
        dietDao = db.dietDao();
        allDiets = dietDao.getAll();
    }

    // Method to get all diets from the database
    public LiveData<List<Diet>> getAllDiets() {
        return allDiets;
    }

    // Method to insert diet to the database
    public void insert(final Diet diet) {
        // Perform database write operation asynchronously
        DietDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call insert method of diet DAO to insert diet record to database
                dietDao.insert(diet);
            }
        });
    }

    // Method to delete all diets from the database
    public void deleteAll() {
        // Perform database write operation asynchronously
        DietDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call deleteAll method of diet DAO to delete all diet records from database
                dietDao.deleteAll();
            }
        });
    }

    // Method to delete a diet from the database
    public void delete(final Diet diet) {
        // Perform database write operation asynchronously
        DietDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call delete method of diet DAO to delete a diet record from database
                dietDao.delete(diet);
            }
        });
    }

    // Method to update a diet in the database
    public void updateDiet(final Diet diet) {
        // Perform database write operation asynchronously
        DietDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call update method of diet DAO to update a diet record in database
                dietDao.updateDiet(diet);
            }
        });
    }

    // Method to get a Diet by ID asynchronously, returns a completable future.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Diet> findByIDFuture(final int dietId) {
        // Get customer from database asynchronously
        // CustomerDatabase.databaseWriteExecutor is the executor to run asynchronous operations on database
        // Supplier.get() method is called asynchronously to get customer by ID from database
        return CompletableFuture.supplyAsync(new Supplier<Diet>() {
            @Override
            public Diet get() {
                // Call findByID method of customer DAO to get a specific customer record by their ID from database
                return dietDao.findByID(dietId);
            }
        }, DietDatabase.databaseWriteExecutor);
    }

}
