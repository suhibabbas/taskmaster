package com.taskmaster;

import android.app.Activity;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

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

    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;

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

        //Interstitial_Ad
        Button interstitial_Ad_Btn = findViewById(R.id.Interstitial_Ad);
        interstitial_Ad_Btn.setOnClickListener(view -> {
            // load the interstitial ad
            loadInterstitialAd();
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d(TAG, "The interstitial ad wasn't ready yet.");
            }
        });
        // Rewarded Ad
        Button rewarded_Ad_Btn = findViewById(R.id.rewarded_Ad);
        rewarded_Ad_Btn.setOnClickListener(view -> {
            loadRewardedAd();
            if (mRewardedAd != null) {
                Activity activityContext = MainActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Toast.makeText(activityContext, "the amount => " + rewardAmount, Toast.LENGTH_SHORT).show();
                        Toast.makeText(activityContext, "the type => " + rewardType, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });
        // Banner Ad
        // load ad into the Adview
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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

                    if(cloudData.get(position).getLatitude() != null){
                        intent.putExtra("latitude",cloudData.get(position).getLatitude().toString());
                        intent.putExtra("longitude",cloudData.get(position).getLongitude().toString());
                    }else {
                        intent.putExtra("latitude","no location");
                        intent.putExtra("longitude","no location");
                    }




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

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-1751290226784894~8161446844", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d(TAG, "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d(TAG, "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d(TAG, "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-1751290226784894~8161446844",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });
                    }
                });
    }

//    public void initialiseTaskData(){
//        taskData.add(new TaskModel("Java","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", TaskModel.State.NEW.toString()));
//        taskData.add(new TaskModel("C#","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.ASSIGNED.toString()));
//        taskData.add(new TaskModel("JS","Lorem Ipsum is simply dummy text of the printing and typesetting industry.",TaskModel.State.COMPLETE.toString()));
//    }

}