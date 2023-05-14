package com.example.vitality.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exerciseentity")
public class ExerciseEntity {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String type;
    public int duration;
    public String calc;
    public long selTime;//选择时间

    public long createTime;//
}
