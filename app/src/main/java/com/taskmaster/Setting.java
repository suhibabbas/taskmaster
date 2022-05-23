package com.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class Setting extends AppCompatActivity {

    private static final String TAG = Setting.class.getSimpleName();
    private EditText mUserNameEditText;
    public static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        mUserNameEditText = findViewById(R.id.user_settings);
        Button btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(view -> {
            Log.i(TAG,"saved");

            saveUsername();
        });

        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG,"onTextChanged: the text is :"+ charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "afterTextChanged: the final text is : " + editable.toString());
                if(!btnSave.isEnabled()){
                    btnSave.setEnabled(true);
                }

                if(editable.toString().length() == 0){
                    btnSave.setEnabled(false);
                }

            }
        });
    }

    private void saveUsername(){

        String username= mUserNameEditText.getText().toString();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        preferencesEditor.putString(USERNAME,username);
        preferencesEditor.apply();


        Toast.makeText(this,username +" :Saved",Toast.LENGTH_SHORT).show();
    }

}