package com.example.javaapk.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.GlobalConstants;
import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;

//activity used when we need to ask the user for a password or a username or both in order to login.
public class LoginIntoProfileActivity extends AppCompatActivity {

    public static final String ACTION_LOGIN_EXISTING = GlobalConstants.appQualifier + ".ACTION_LOGIN_EXISTING"; //action to login with a password to an existing profile and select it.
    public static final String ACTION_LOGIN_NEW = GlobalConstants.appQualifier + ".ACTION_LOGIN_NEW"; //action to add a new profile into the list of existing profiles and select it.
    public static final String TAG = LoginIntoProfileActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //autogenerated
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_into_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String action = intent.getAction();

        if(action == null){
            Log.w(TAG, "no action provided");
            this.finish();
        }


        switch (action){
            case ACTION_LOGIN_EXISTING:{
                int profileIndex = intent.getIntExtra("profileIndex", -1);
                if(profileIndex == -1){
                    Log.w(TAG, "provided no index");
                    this.finish();
                }

                String autocompleteUsername = DataManager.getInstance().profiles.get(profileIndex).username;

                //autocomplete the username and make it uneditable by the user;
                //create a listener for the submit button adds the password to the profile and selects it;

                this.finish();
            }
            case ACTION_LOGIN_NEW:{
                //create a listener for the submit button that will:
                    // 1) create the profile
                    // 2) add it to the list of existing profiles
                    // 3) select it

                this.finish();
            }
            default:{
                Log.w(TAG, "provided action does not exist");
                this.finish();
            }
        }
    }
}