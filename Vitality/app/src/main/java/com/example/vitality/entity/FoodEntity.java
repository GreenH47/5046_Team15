package com.example.vitality.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;



}
