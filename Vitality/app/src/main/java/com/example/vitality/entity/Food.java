package com.example.vitality.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "foods")
public class Food {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "food_name")
    @NonNull
    public String foodName;

    @ColumnInfo(name = "food_intake")
    @NonNull
    public float foodIntake;

    @ColumnInfo(name = "calories")
    public float calories;

    @ColumnInfo(name = "date")
    @NonNull
    public String date;

    // Constructor
    public Food(@NonNull String foodName, @NonNull float foodIntake, float calories, @NonNull String date) {
        this.foodName = foodName;
        this.foodIntake = foodIntake;
        this.calories = calories;
        this.date = date;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getFoodIntake() {
        return foodIntake;
    }

    public void setFoodIntake(float foodIntake) {
        this.foodIntake = foodIntake;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
