package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class taskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        String getTaskTitle = getIntent().getStringExtra("title");

        TextView title = findViewById(R.id.Title_taskDetails);
        TextView description = findViewById(R.id.Description_TaskDetails);
        TextView state = findViewById(R.id.State);

        title.setText(getTaskTitle);
        description.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. ");
        state.setText("done");

    }
}