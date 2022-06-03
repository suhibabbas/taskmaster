package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Setting extends AppCompatActivity {

    private static final String TAG = Setting.class.getSimpleName();
    private EditText mUserNameEditText;
    public static final String USERNAME = "username";
    public static final String TEAMNAME = "teamName";
    private Spinner spinner;
    private List<String> teamList =new ArrayList<>();
    private Button mBtnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        mUserNameEditText = findViewById(R.id.user_settings);
        mBtnSave = findViewById(R.id.btn_save);

        spinner = findViewById(R.id.teams);

//        Handler handler = new Handler(Looper.getMainLooper(), msg ->{
//            ArrayAdapter<String> adapter =
//                    new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, teamList);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//
//
//            return true;
//
//        });

        mBtnSave.setOnClickListener(view -> {
//            Log.i(TAG,"saved");

            saveUsername();

        });


        Amplify.API.query(ModelQuery.list(Team.class),
                response ->{
            for (Team team: response.getData()
                 ) {
                teamList.add(team.getName());
            }
            Bundle bundle = new Bundle();
            bundle.putString("data","done");

            Message message = new Message();
            message.setData(bundle);
//            handler.sendMessage(message);
        },error -> {
//                    Log.e("MyAmplifyApp", "Query failure", error)
                }
        );


        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.i(TAG,"onTextChanged: the text is :"+ charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.i(TAG, "afterTextChanged: the final text is : " + editable.toString());
                if(!mBtnSave.isEnabled()){
                    mBtnSave.setEnabled(true);
                }

                if(editable.toString().length() == 0){
                    mBtnSave.setEnabled(false);
                }

            }
        });
    }

    private void saveUsername(){

        String username= mUserNameEditText.getText().toString();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        preferencesEditor.putString(USERNAME,username);
        preferencesEditor.putString(TEAMNAME,spinner.getSelectedItem().toString());
        preferencesEditor.apply();


        Toast.makeText(this,username +" :Saved",Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(),MainActivity.class));


    }

}