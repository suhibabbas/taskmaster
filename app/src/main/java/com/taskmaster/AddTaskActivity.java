package com.taskmaster;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.geo.maplibre.view.AmplifyMapView;
import com.amplifyframework.geo.models.MapStyle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;


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

    FusedLocationProviderClient mFusedLocationClient;

    private int PERMISSION_ID = 44;
    private double latitude;
    private double longitude;
    private AmplifyMapView amplifyMapView;
    List<MapStyle> styles;

    private final LocationCallback mLocationCallback =new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            amplifyMapView.getMapView().getStyle((map, style) ->{
                LatLng spaceNeedle = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                amplifyMapView.getMapView().symbolManager.create(new SymbolOptions().withIconImage("place").withLatLng(spaceNeedle));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(spaceNeedle,16.0));

            });
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

//        if(type.startsWith("image/")){
//            handleSendImage(intent);
//        }


        Button submitButton = findViewById(R.id.submit);
        spinner = findViewById(R.id.team);
        title = findViewById(R.id.add_task_tile);
        body = findViewById(R.id.add_task_body);
        addTaskState = findViewById(R.id.task_state);
        uploadButton = findViewById(R.id.upload);
        amplifyMapView = findViewById(R.id.mapView);





        Handler handler = new Handler(Looper.getMainLooper(),msg ->{
            Log.i("adapter" , teamList.toString()) ;
            List<String> spinnerList = teamList.stream().map( index -> index.getName()).collect(Collectors.toList());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            return true;
        });


        amplifyMapView.setOnPlaceSelectListener((place, symbol) -> {
            // place is an instance of AmazonLocationPlace
            // symbol is an instance of Symbol from MapLibre
            Log.i("MyAmplifyApp", "The selected place is " + place.getLabel());
            Log.i("MyAmplifyApp", "It is located at " + place.getCoordinates());
        });

        Amplify.Geo.getAvailableMaps(result -> {
                    for (final MapStyle style : result) {
                        Log.i("MyAmplifyApp", style.toString());
                        styles.add(style);
                    }

                    amplifyMapView.getMapView().setStyle(styles.get(0), style -> {
                        Log.i("MyAmplifyApp", "Finished loading " + styles.get(0).getStyle());
                    });
                }, error -> Log.e("MyAmplifyApp", "Failed to get available maps.", error));


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Amplify.API.query(ModelQuery.list(Team.class), response -> {
//                    Log.i("MyAmplifyApp", "Query succ");
                    for(Team team : response.getData()){
                         teamList.add(team);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("data", "Done");
                    Message message = new Message();
                    message.setData(bundle);
//                    handler.sendMessage(message);
                }, error -> {
                    Log.e("MyAmplifyApp", "Query failure", error);
                });




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
                    .latitude(latitude)
                    .longitude(longitude)
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

        getLastLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
//        if (checkPermissions()) {
//
//            // check if location is enabled
//            if (isLocationEnabled()) {
//
//                // getting last
//                // location from
//                // FusedLocationClient
//                // object
//                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        Location location = task.getResult();
//                        if (location == null) {
//                            requestNewLocationData();
//                        } else {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//
//                            amplifyMapView.getMapView().getStyle((map, style) -> {
//                                LatLng spaceNeedle = new LatLng(latitude, longitude);
//                                amplifyMapView.getMapView().symbolManager.create(
//                                        new SymbolOptions()
//                                                .withIconImage("place")
//                                                .withLatLng(spaceNeedle)
//                                );
//                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(spaceNeedle, 16.0));
//                            });
//                        }
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            // if permissions aren't available,
//            // request for permissions
//            requestPermissions();
//        }
        if(checkPermissions()){
            // check if location is enabled
            if(isLocationEnabled()){
                // getting last
                // location from
                // FusedLocationClient
                // object

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                       Location location = task.getResult();
                       if(location == null){
                           requestNewLocationData();
                       }else {
                           latitude = location.getLatitude();
                           longitude = location.getLongitude();

                           amplifyMapView.getMapView().getStyle((map, style)->{
                               LatLng spaceNeedle = new LatLng(latitude, longitude);
                               amplifyMapView.getMapView().symbolManager.create(
                                       new SymbolOptions().withIconImage("place")
                                               .withLatLng(spaceNeedle)
                               );
                               map.animateCamera(CameraUpdateFactory.newLatLngZoom(spaceNeedle, 16.0));
                           });
                       }
                    }
                });
            }else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }

    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
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

    private void handleSendImage(Intent intent){
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            uploadSelectedPhoto(imageUri);
        }
    }

    private void uploadSelectedPhoto(Uri currentUri) {
        Bitmap bitmap = null;
        // Do stuff with the photo/video URI.
        Log.i(TAG, "onActivityResult: the uri is => " + currentUri);
        try {
            bitmap = getBitmapFromUri(currentUri);
            File file = new File(getApplicationContext().getFilesDir(), "image.jpg");
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor= parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}