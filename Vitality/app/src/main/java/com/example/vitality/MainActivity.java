package com.example.vitality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button dietLogButton, workoutLogButton, dashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the three buttons on the home screen
        dietLogButton = findViewById(R.id.dietLogButton);
        workoutLogButton = findViewById(R.id.workoutLogButton);
        dashboardButton = findViewById(R.id.dashboardButton);

        // Set onClickListeners for all three buttons
        dietLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DietLogActivity.class);
                startActivity(intent);
            }
        });

        workoutLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkoutLogActivity.class);
                startActivity(intent);
            }
        });

        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
