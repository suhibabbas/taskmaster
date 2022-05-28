package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTaskActivity extends AppCompatActivity {

private Spinner addTaskState;
    private static final String TAG = AddTaskActivity.class.getName();
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


//            Task task= new Task(taskTitle,taskBody,taskState);
//
//            Long newTaskId = TaskDatabase.getInstance(getApplicationContext()).taskDAO().insertTask(task);
//
//            System.out.println("******************** Task ID = " + newTaskId + " ************************");

            Task task =Task.builder()
                    .title(taskTitle
                    ).description(taskBody)
                    .status(taskState)
                    .build();

            Amplify.DataStore.save(
                    task,
                    success -> Log.i(TAG,"Saved item"),
                    error-> Log.i(TAG,"Could not save item to DataStore", error)
            );


            Amplify.DataStore.query(Task.class,
                    tasks ->{
                        while (tasks.hasNext()){
                            Task taskQuery =tasks.next();
                            Log.i(TAG,"Task");
                            Log.i(TAG,"Name: "+ task.getTitle());
                        }

                    },failure -> Log.e(TAG,"ERROR => ",failure)
                    );

            Amplify.API.mutate(ModelMutation.create(task),
                    success ->{Log.i(TAG,"success =>" + success.getData().getTitle());},
                    error ->{Log.i(TAG,"ERROR => "+ error);}
            );

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
    }


}