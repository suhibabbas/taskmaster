package com.taskmaster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class taskDetails extends AppCompatActivity {
    private static final String TAG = taskDetails.class.getName();

    URL url;

    private final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);




        String getTaskTitle = getIntent().getStringExtra("Title");
        String getTaskBody = getIntent().getStringExtra("Body");
        String getTaskState = getIntent().getStringExtra("State");
        String imageKey = getIntent().getStringExtra("imageKey");
        String longitude = getIntent().getStringExtra("longitude");
        String latitude = getIntent().getStringExtra("latitude");


        TextView title = findViewById(R.id.Title_taskDetails);
        TextView description = findViewById(R.id.Description_TaskDetails);
        TextView state = findViewById(R.id.State);
        TextView lon = findViewById(R.id.longitude);
        TextView lat = findViewById(R.id.latitude);


        // Sound button
        Button soundBtn = findViewById(R.id.play);
        soundBtn.setOnClickListener(view -> {
            Amplify.Predictions.convertTextToSpeech(
                    description.getText().toString(),
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Conversion failed", error)
            );
        });


        // translate button
        Button translateBtn = findViewById(R.id.translate);
        translateBtn.setOnClickListener(view -> {
            Amplify.Predictions.translateText(description.getText().toString(),
                    result -> {
                        Log.i(TAG, result.getTranslatedText());

                        runOnUiThread(() -> {
                            TextView translateResult = findViewById(R.id.translate_text);
                            translateResult.setText(result.getTranslatedText());
                        });
                    },
                    error -> Log.e(TAG, "Translation failed", error)
            );
        });


//        ImageView imageView = findViewById(R.id.imageView2);



//        imageView.setImageDrawable(LoadImageFromWebOperations("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg"));
        title.setText(getTaskTitle);
        description.setText(getTaskBody );
        state.setText(getTaskState);
        lon.setText(longitude);
        lat.setText(latitude);
//        if(imageKey != null){
//            setImage(imageKey);
//            getUrl(imageKey);
//        }


//        Glide.with(this).load(url).into(imageView);





    }

//    private void setImage(String imageKey) {
//
//        Amplify.Storage.downloadFile(
//                imageKey,
//                new File( getApplicationContext().getFilesDir() + "/" + imageKey),
//                result -> {
//                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
//                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
//
//                    ImageView image = findViewById(R.id.image);
//                    Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+"/"+result.getFile().getName());
//                    image.setImageBitmap(bitmap);
//
//
//                },
//                error -> Log.e(TAG,  "Download Failure", error)
//        );
//    }

    private void getUrl(String imagekey){
        Amplify.Storage.getUrl(
                imagekey,
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                    url = result.getUrl();
                },


                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private void playAudio(InputStream audioData) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = audioData.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }

}