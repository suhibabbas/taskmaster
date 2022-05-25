package com.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taskmaster.data.TaskModel;

public class taskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        String getTaskTitle = getIntent().getStringExtra("Title");
        String getTaskBody = getIntent().getStringExtra("Body");
        String getTaskState = getIntent().getStringExtra("State");

        TextView title = findViewById(R.id.Title_taskDetails);
        TextView description = findViewById(R.id.Description_TaskDetails);
        TextView state = findViewById(R.id.State);



        title.setText(getTaskTitle);
        description.setText(getTaskBody);
        state.setText(getTaskState);




    }


}