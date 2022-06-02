package com.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddTaskActivity extends AppCompatActivity {
    private static final String TAG = AddTaskActivity.class.getName();
    private Spinner addTaskState;
    private Spinner spinner;
    private final List<Team> teamList = new ArrayList<>();
    private Team team;
    private EditText title;
    private EditText body;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button submitButton = findViewById(R.id.submit);
        spinner = findViewById(R.id.team);
        title = findViewById(R.id.add_task_tile);
        body = findViewById(R.id.add_task_body);
        addTaskState = findViewById(R.id.task_state);

        Handler handler = new Handler(Looper.getMainLooper(),msg ->{
            Log.i("adapter" , teamList.toString()) ;
            List<String> spinnerList = teamList.stream().map( index -> index.getName()).collect(Collectors.toList());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            return true;
        });

        Amplify.API.query(ModelQuery.list(Team.class),
                response -> {
                    Log.i("MyAmplifyApp", "Query succ");
                    for(Team team : response.getData()){
                         teamList.add(team);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "Done");
                    Message message = new Message();
                    message.setData(bundle);
//                    handler.sendMessage(message);
                },
                error ->Log.e("MyAmplifyApp", "Query failure", error)
        );

        submitButton.setOnClickListener(view -> {

            String taskTitle = title.getText().toString();
            String taskBody = body.getText().toString();
            String taskState = addTaskState.getSelectedItem().toString();
            String teamSelected = spinner.getSelectedItem().toString();

            Team selectedTeam = teamList.get(0);

            for(Team team : teamList){
                if(team.getName().equals(teamSelected)){
                    selectedTeam = team;
                }
            }


            Task task = Task.builder()
                    .title(taskTitle)
                    .description(taskBody)
                    .status(taskState)
                    .teamTasksId(selectedTeam.getId())
                    .build();

            // Data store save
            Amplify.DataStore.save(task,
                    success -> {
                        Log.i(TAG, "Saved item " + success);

                        //add the Team to the task
                    },
                    error ->Log.i(TAG,"Could not save item to DataStore", error));

            // Data store query
            Amplify.DataStore.query(Task.class,
                    tasks ->{
                        while (tasks.hasNext()){
//                            Task taskQuery =tasks.next();
                            Log.i(TAG,"Task");
                            Log.i(TAG,"Name: "+ task.getTitle());
                        }

                    },failure -> Log.e(TAG,"ERROR => ",failure)
            );

            // API save to backend
            Amplify.API.mutate(ModelMutation.create(task),
                    success ->{Log.i(TAG,"success =>" + success.getData().getTitle());},
                    error ->{Log.i(TAG,"ERROR => "+ error);}
            );

            // API query
//        Amplify.API.query(
//                ModelQuery.list(Task.class, Task.TITLE.contains("Build")),
//                response -> {
//                    for (Task task : response.getData()) {
//                        Log.i(TAG, "------------------> " + task.getTitle());
//                    }
//                },
//                error -> Log.e(TAG, "Query failure", error)
//        );

//            // Datastore and API sync
//            Amplify.DataStore.observe(Task.class,
//                    started -> {
//                        Log.i(TAG, "Observation began.");
//                        // TODO: 5/17/22 Update the UI thread with in this call method
//                        // Manipulate your views
//
//                        // call handler
//                    },
//                    change -> Log.i(TAG, change.item().toString()),
//                    failure -> Log.e(TAG, "Observation failed.", failure),
//                    () -> Log.i(TAG, "Observation complete.")
//            );

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
    }

    void saveTeam(Team team){

        Amplify.DataStore.save(team,
                success ->  Log.i(TAG, "Saved item " + success) ,
                error ->Log.i(TAG,"Could not save item to DataStore", error)
        );

        Amplify.API.mutate(
                ModelMutation.create(team),
                success->{
                    Log.i(TAG, "saveTeamInAPI: Team saved");
                },
                fail->{
                    Log.i(TAG, "saveTeamInAPI: failed to save the team");
                });
    }


}