package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button submitButton = findViewById(R.id.submit);

        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();

        });
    }


}