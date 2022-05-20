package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final View.OnClickListener mAddTaskListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            Intent startSecondActivity = new Intent(getApplicationContext(), AddTaskActivity.class);

            startSecondActivity.putExtra("greeting", "Welcome to the Second activity");
            startSecondActivity.putExtra("description","This is the second window");
            startActivity(startSecondActivity);
        }

    };

    private final View.OnClickListener mAllTaskListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent startAllTaskIntent = new Intent(getApplicationContext(), AllTaskActivity.class);
            startActivity(startAllTaskIntent);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: called");

        Button addTask = findViewById(R.id.Add_task);
        Button allTasks = findViewById(R.id.All_Tasks);

        addTask.setOnClickListener(mAddTaskListener);
        allTasks.setOnClickListener(mAllTaskListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: called");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: called");
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onStop: called");
    }

}