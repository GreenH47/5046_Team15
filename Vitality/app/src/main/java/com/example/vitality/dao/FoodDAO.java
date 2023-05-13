package com.example.vitality.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vitality.entity.Diet;
import com.example.vitality.entity.Food;

import java.util.List;

@Dao
public interface FoodDAO {
    // Query to get all the food from the database
    @Query("SELECT * FROM foods ORDER BY id ASC")
    LiveData<List<Food>> getAll();

    // Query to get a food by their ID
    @Query("SELECT * FROM foods WHERE id = :foodId LIMIT 1")
    Food getFoodById(int foodId);

    // Query to get food by their date
    @Query("SELECT * FROM foods WHERE date = :date")
    LiveData<List<Food>> getFoodByDate(String date);

    // query insert a new food into the database
    @Insert
    void insert(Food food);

    // Query to delete a food from the database
    @Delete
    void delete(Food food);

    // Query to delete all the food from the database
    @Query("DELETE FROM foods")
    void deleteAll();




}
