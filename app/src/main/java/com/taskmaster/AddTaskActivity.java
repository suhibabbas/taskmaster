package com.taskmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
    private Button uploadButton;
    private String imageKey = "" ;
    public static final int REQUEST_CODE = 123;

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
        uploadButton = findViewById(R.id.upload);

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
//                    Log.i("MyAmplifyApp", "Query succ");
                    for(Team team : response.getData()){
                         teamList.add(team);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "Done");
                    Message message = new Message();
                    message.setData(bundle);
//                    handler.sendMessage(message);
                },
                error -> {
                    Log.e("MyAmplifyApp", "Query failure", error);
                }
        );

        uploadButton.setOnClickListener(view -> uploadImage());

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
                    .image(imageKey)
                    .teamTasksId(selectedTeam.getId())
                    .build();
            // Data store save
            Amplify.DataStore.save(task,
                    success -> {
                        Log.i(TAG, "Saved item " + success + " "+ imageKey);

                        //add the Team to the task
                    },
                    error -> {
                        Log.i(TAG, "Could not save item to DataStore", error);
                    });
            // Data store query
            Amplify.DataStore.query(Task.class,
                    tasks ->{
                        while (tasks.hasNext()){
//                            Task taskQuery =tasks.next();
//                            Log.i(TAG,"Task");
//                            Log.i(TAG,"Name: "+ task.getTitle());
                        }

                    },failure -> {
//                        Log.e(TAG, "ERROR => ", failure);
                    }
            );

            // API save to backend
            Amplify.API.mutate(ModelMutation.create(task),
                    success ->{
//                Log.i(TAG,"success =>" + success.getData().getTitle());
                },
                    error ->{
//                Log.i(TAG,"ERROR => "+ error);
            }
            );



            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
    }

    void saveTeam(Team team){

        Amplify.DataStore.save(team,
                success -> {
                    Log.i(TAG, "Saved item " + success);
                } ,
                error -> {
                    Log.i(TAG, "Could not save item to DataStore", error);
                }
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

    public void uploadFile(){
        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e(TAG, "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i(TAG, "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
        );
    }

    public void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            return;
        }

        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);
                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);
                    File file = new File(getApplicationContext().getFilesDir(),"image.jpg");
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.close();


                    Amplify.Storage.uploadFile("image.jpg",
                            file,
                            result -> {

                                imageKey = result.getKey();
                                Log.i(TAG, "Successfully uploaded: " + imageKey);

                            },
                            storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                    );
                }catch (IOException e){
                    e.printStackTrace();
                }
                return;

        }



    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor= parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}