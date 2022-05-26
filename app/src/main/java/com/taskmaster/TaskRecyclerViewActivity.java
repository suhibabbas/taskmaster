package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.taskmaster.data.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerViewActivity extends AppCompatActivity {
    List<TaskModel> taskModelsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_rescyler_view);

//        initialiseTaskData();
//
//        // get the recycler view object
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//
//        // create an adapter -> CustomRecyclerViewAdapter
//
//        CustomRecyclerViewAdapter customRecyclerViewAdapter = new CustomRecyclerViewAdapter(taskModelsList,position -> {
//            startActivity(new Intent(getApplicationContext(),taskDetails.class));
//        });
//
//
//        // set adapter on recycler view
//        recyclerView.setAdapter(customRecyclerViewAdapter);
//
//        // set other important properties
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    private void initialiseTaskData(){
//        taskModelsList.add(new TaskModel("Java","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", TaskModel.State.NEW.toString()));
//        taskModelsList.add(new TaskModel("C#","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.ASSIGNED.toString()));
//        taskModelsList.add(new TaskModel("JS","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.COMPLETE.toString()));
//    }
}