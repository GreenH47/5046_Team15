package com.example.vitality.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vitality.entity.Diet;

import java.util.List;

@Dao
public interface DietDAO {
    // Query to get all the diet from the database
    @Query("SELECT * FROM diet ORDER BY day_number ASC")
    LiveData<List<Diet>> getAll();

    // Query to get a diet by their ID
    @Query("SELECT * FROM diet WHERE uid = :dietId LIMIT 1")
    Diet findByID(int dietId);

    // Query to insert a new diet into the database
    @Insert
    void insert(Diet diet);

    // Query to delete a diet from the database
    @Delete
    void delete(Diet diet);

    // Query to update an existing diet in the database
    @Update
    void updateDiet(Diet diet);

    // Query to delete all the diet from the database
    @Query("DELETE FROM diet")
    void deleteAll();




}
