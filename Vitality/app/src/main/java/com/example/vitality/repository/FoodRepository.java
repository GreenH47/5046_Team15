package com.example.vitality.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.vitality.dao.FoodDAO;
import com.example.vitality.database.FoodDatabase;
import com.example.vitality.entity.Food;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FoodRepository {
    // The DAO interface for Food table
    private FoodDAO foodDao;

    // get all foods LiveData
    private LiveData<List<Food>> allFoods;

    // constructor that initializes the FoodDatabase instance and the foodDao.
    // allFoods holds a LiveData which holds a list of all the foods.
    public FoodRepository(Application application) {
        // Access the database instance and get the DAO interface for Food table
        FoodDatabase db = FoodDatabase.getInstance(application);
        foodDao = db.foodDao();
        allFoods = foodDao.getAll();
    }

    // Method to get all foods from the database
    public LiveData<List<Food>> getAllFoods() {
        return allFoods;
    }

    // Method to insert food to the database
    public void insert(final Food food) {
        // Perform database write operation asynchronously
        FoodDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call insert method of food DAO to insert food record to database
                foodDao.insert(food);
            }
        });
    }

    // Method to delete all foods from the database
    public void deleteAll() {
        // Perform database write operation asynchronously
        FoodDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call deleteAll method of food DAO to delete all food records from database
                foodDao.deleteAll();
            }
        });
    }

    // Method to delete a food from the database
    public void delete(final Food food) {
        // Perform database write operation asynchronously
        FoodDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // Call delete method of food DAO to delete a food record from database
                foodDao.delete(food);
            }
        });
    }

    // Method to get food by their date
    public LiveData<List<Food>> getFoodByDate(String date) {
        return foodDao.getFoodByDate(date);
    }

    // method to get food between two dates
    public LiveData<List<Food>> getFoodBetweenDates(String startDate, String endDate) {
        return foodDao.getFoodBetweenDates(startDate, endDate);
    }

    // method to get a food by its id asynchronously
    // then return a CompletableFuture object
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Food> findByIDFuture(int id) {
        // return a CompletableFuture object
        return CompletableFuture.supplyAsync(new Supplier<Food>() {
            @Override
            public Food get() {
                // call getFoodById method of food DAO to get a food record from database
                return foodDao.getFoodById(id);
            }
        }, FoodDatabase.databaseWriteExecutor);
    }

    // get food data between two dates


}
