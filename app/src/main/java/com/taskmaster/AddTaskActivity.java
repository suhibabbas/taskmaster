package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taskmaster.data.Task;

public class AddTaskActivity extends AppCompatActivity {

private Spinner addTaskState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button submitButton = findViewById(R.id.submit);



        submitButton.setOnClickListener(view -> {



            EditText title = findViewById(R.id.add_task_tile);
            String taskTitle = title.getText().toString();

            EditText body = findViewById(R.id.add_task_body);
            String taskBody = body.getText().toString();

            addTaskState = findViewById(R.id.task_state);
            String taskState = addTaskState.getSelectedItem().toString();


            Task task= new Task(taskTitle,taskBody,taskState);

            Long newTaskId = TaskDatabase.getInstance(getApplicationContext()).taskDAO().insertTask(task);

            System.out.println("******************** Task ID = " + newTaskId + " ************************");

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
    }


}