package com.example.vitality;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Find the text view on the dashboard screen
        TextView textView = findViewById(R.id.textView);

        // Set the text of the text view to "Hello World this is DashboardActivity"
        textView.setText("Hello World this is DashboardActivity");

        // Find the return button on the dashboard screen
        Button returnButton = findViewById(R.id.returnButton);

        // Set an OnClickListener on the return button to finish the activity
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

