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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taskmaster.data.Task;
import com.taskmaster.data.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


//    List<TaskModel> taskData = new ArrayList<>();

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

//        initialiseTaskData();

        List<Task> taskList = TaskDatabase.getInstance(getApplicationContext()).taskDAO().getAll();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        CustomRecyclerViewAdapter  customRecyclerViewAdapter = new CustomRecyclerViewAdapter(taskList,position -> {

            Intent intent = new Intent (getApplicationContext(),taskDetails.class);

            intent.putExtra("Title",taskList.get(position).getTitle());
            intent.putExtra("Body",taskList.get(position).getBody());
            intent.putExtra("State",taskList.get(position).getState());
            startActivity(intent);
        });

        recyclerView.setAdapter(customRecyclerViewAdapter);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        Intent taskDetailsIntent = new Intent(this,taskDetails.class);

        String task = taskSelector.getSelectedItem().toString();
        taskDetailsIntent.putExtra("Title",task );
        taskDetailsIntent.putExtra("Body","Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        taskDetailsIntent.putExtra("State","done");

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

//    public void initialiseTaskData(){
//        taskData.add(new TaskModel("Java","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", TaskModel.State.NEW.toString()));
//        taskData.add(new TaskModel("C#","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.ASSIGNED.toString()));
//        taskData.add(new TaskModel("JS","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.COMPLETE.toString()));
//    }

}