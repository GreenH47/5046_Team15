package com.example.vitality;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;

public class MyWorker extends Worker {
    public MyWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Your code here
        // You can write your Room data to Firebase here
        // Remember to print the method name and timestamp
        Log.d("MyWorker", "doWork: " + new Date().toString());

        return Result.success();
    }
}
