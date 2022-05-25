package com.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taskmaster.data.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

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

    private Spinner taskSelector;
    private TextView mUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: called");

        Button addTask = findViewById(R.id.Add_task);
        Button allTasks = findViewById(R.id.All_Tasks);
        Button btnView = findViewById(R.id.btn_view);
        taskSelector  = findViewById(R.id.select_task);
        mUsername = findViewById(R.id.textView);

        addTask.setOnClickListener(mAddTaskListener);
        allTasks.setOnClickListener(mAllTaskListener);



        btnView.setOnClickListener(view -> {
            Log.i(TAG,"View button clicked");
            goToTaskDetails();
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                goToSetting();
                return true;
            case R.id.action_copyright:
                Toast.makeText(this, "Copyright 2022", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSetting(){
        Intent settingIntent = new Intent(this,Setting.class);
        startActivity(settingIntent);
    }

    private void goToTaskDetails(){
        Intent taskDetailsIntent = new Intent(this,TaskRescylerViewActivity.class);

        String task = taskSelector.getSelectedItem().toString();
        taskDetailsIntent.putExtra("title",task );
        startActivity(taskDetailsIntent);
    }

    private void setUsername(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername.setText(sharedPreferences.getString(Setting.USERNAME,"no username set"));

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
        setUsername();
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