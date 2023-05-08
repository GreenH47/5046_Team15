package com.example.vitality.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//
@Entity
//the @Entity annotation is used to mark the diet class as an
// entity that will be mapped to a table in the database.
public class Diet {
    // Primary key for identifying each diet,
    // auto generates the value for each diet in the database
    @PrimaryKey(autoGenerate = true)
    public int uid;

    // specify the column name as "day_number".
    @ColumnInfo(name = "day_number")
     @NonNull
    public int dayNum;

    // specify the column name as "cake_number".
    @ColumnInfo(name = "cake_number")
    @NonNull
    public int cakeNum;

    // specify the column name as "beef_number".
    @ColumnInfo(name = "beef_number")
    @NonNull
    public int beefNum;

    //get the beefNum
    public int getBeefNum() {
        return beefNum;
    }

    //get the cakeNum
    public int getCakeNum() {
        return cakeNum;
    }

    //get the dayNum
    public int getDayNum() {
        return dayNum;
    }

    //Constructor to initialize the diet object
    // with day number, cake number and beef number
    public Diet(@NonNull int dayNum, @NonNull int cakeNum, @NonNull int beefNum) {
        this.dayNum = dayNum;
        this.cakeNum = cakeNum;
        this.beefNum = beefNum;
    }
}
