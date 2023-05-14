package com.example.vitality.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vitality.entity.ExerciseEntity;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exerciseentity ORDER BY uid ASC")
    LiveData<List<ExerciseEntity>> getAll();

    @Insert
    void insert(ExerciseEntity exerciseEntity);

    @Delete
    void delete(ExerciseEntity exerciseEntity);

    @Update
    void update(ExerciseEntity exerciseEntity);


}
