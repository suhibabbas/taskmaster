package com.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


//    List<TaskModel> taskData = new ArrayList<>();

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Task> cloudData= new ArrayList<>();
    private String userId ;



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
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: called");

        mUsername = findViewById(R.id.textView);

        Button addTask = findViewById(R.id.Add_task);
        addTask.setOnClickListener(mAddTaskListener);

        Button allTasks = findViewById(R.id.All_Tasks);
        allTasks.setOnClickListener(mAllTaskListener);

    }

    @Override
    protected void onResume() {

        setUsername();
        cloudData = new ArrayList<>();

        handler = new Handler(Looper.getMainLooper(), message -> {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);

            CustomRecyclerViewAdapter customRecyclerViewAdapter = new CustomRecyclerViewAdapter(cloudData, new CustomRecyclerViewAdapter.CustomClickListener() {
                @Override
                public void onTaskClickListener(int position) {
                    Intent intent = new Intent(getApplicationContext(),taskDetails.class);
                    intent.putExtra("Title",cloudData.get(position).getTitle());
                    intent.putExtra("Body",cloudData.get(position).getDescription());
                    intent.putExtra("State",cloudData.get(position).getStatus());
                    intent.putExtra("imageKey",cloudData.get(position).getImage());



                    startActivity(intent);

                }
            });
            recyclerView.setAdapter(customRecyclerViewAdapter);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            return true;
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = sharedPreferences.getString("teamName","");
        Log.i("teamName" , teamName);
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(teamName)),
                success ->{
                    for(Team team: success.getData()){
//                        Log.i("get team" , team.toString());
                        cloudData = team.getTasks();
                    }
//                    Log.i("myTasks" , cloudData.toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                },
                error -> {
//                    Log.e("MyAmplifyApp", "Query failure", error)
                }
        );

//        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
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
            case R.id.logout:
                logout();
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
//        Log.i(TAG, "onStart: called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Log.i(TAG, "onRestart: called");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.i(TAG, "onPause: called");
    }

    @Override
    protected void onStop() {
//        Log.i(TAG, "onStop: called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.i(TAG, "onStop: called");
    }

    private void authSession(String method) {




        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, "Auth Session => " + method + result.toString()) ;

                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

                    switch(cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:
                        {
                            Log.i("AuthQuickStart", "IdentityId: " + cognitoAuthSession.getIdentityId().getValue());
                            userId = cognitoAuthSession.getIdentityId().getValue();
                            break;
                        }

                        case FAILURE:
                            Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                    }


                },
                error -> Log.e(TAG, error.toString())
        );
    }

    public void logout(){
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }



//    public void initialiseTaskData(){
//        taskData.add(new TaskModel("Java","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", TaskModel.State.NEW.toString()));
//        taskData.add(new TaskModel("C#","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.ASSIGNED.toString()));
//        taskData.add(new TaskModel("JS","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.COMPLETE.toString()));
//    }

}