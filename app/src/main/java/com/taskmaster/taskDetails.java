package com.taskmaster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class taskDetails extends AppCompatActivity {
    private static final String TAG = taskDetails.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        String getTaskTitle = getIntent().getStringExtra("Title");
        String getTaskBody = getIntent().getStringExtra("Body");
        String getTaskState = getIntent().getStringExtra("State");
        String imageKey = getIntent().getStringExtra("imageKey");

        TextView title = findViewById(R.id.Title_taskDetails);
        TextView description = findViewById(R.id.Description_TaskDetails);
        TextView state = findViewById(R.id.State);




        title.setText(getTaskTitle);
        description.setText(getTaskBody);
        state.setText(getTaskState);
        if(imageKey != null){
            setImage(imageKey);
        }




    }

    private void setImage(String imageKey) {

        Amplify.Storage.downloadFile(
                imageKey,
                new File( getApplicationContext().getFilesDir() + "/" + imageKey),
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());

                    ImageView image = findViewById(R.id.image);
                    Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+"/"+result.getFile().getName());
                    image.setImageBitmap(bitmap);


                },
                error -> Log.e(TAG,  "Download Failure", error)
        );
    }


}