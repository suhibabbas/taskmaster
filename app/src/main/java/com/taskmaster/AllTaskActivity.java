package com.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AllTaskActivity extends AppCompatActivity {
    private static final String TAG = "AllTasksActivity";

    private final View.OnClickListener mHomePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startHomePage = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(startHomePage);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task);
//        Log.i(TAG, "onCreate: called");


        Button backButton = findViewById(R.id.button);

        backButton.setOnClickListener(mHomePageListener);

    }
}