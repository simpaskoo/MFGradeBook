package com.example.javaapk.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.GlobalConstants;
import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;

import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//activity used when we need to ask the user for a password or a username or both in order to login.
public class LoginIntoProfileActivity extends AppCompatActivity {

    public static final String ACTION_LOGIN_EXISTING = GlobalConstants.appQualifier + ".ACTION_LOGIN_EXISTING"; //action to login with a password to an existing profile and select it.
    public static final String ACTION_LOGIN_NEW = GlobalConstants.appQualifier + ".ACTION_LOGIN_NEW"; //action to add a new profile into the list of existing profiles and select it.
    public static final String TAG = LoginIntoProfileActivity.class.getName();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    Button submitButton;
    EditText usernameText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //autogenerated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_into_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        submitButton = findViewById(R.id.submitButton);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);


        Intent intent = getIntent();
        String action = intent.getAction();

        if(action == null){
            Log.w(TAG, "no action provided");
            this.finish();
        }

        Log.d(TAG, ""+action.equals(ACTION_LOGIN_NEW));

        switch (action){
            case ACTION_LOGIN_EXISTING:{
                int profileIndex = intent.getIntExtra("profileIndex", -1);
                if(profileIndex == -1){
                    Log.w(TAG, "provided no index");
                    this.finish();
                }

                String autocompleteUsername = DataManager.getInstance().profiles.get(profileIndex).username;

                usernameText.setText(autocompleteUsername);
                usernameText.setFocusable(false);

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Profile profile = DataManager.getInstance().profiles.get(profileIndex);

                        String password = passwordText.getText().toString();

                        executorService.execute(() -> {
                            try{
                                profile.verifyPassword(password);
                            }catch (RequestFailedException e) {
                                //no internet
                                Log.d(TAG, "could not reach server");
                                return;
                            } catch (HttpErrorStatusException e) {
                                //invalid password
                                Log.d(TAG, "invalid password");
                                return;
                            }

                            profile.addPassword(password);
                            DataManager.getInstance().selectProfile(profileIndex);
                            LoginIntoProfileActivity.this.finish();
                        });

                    }
                });
                break;
            }
            case ACTION_LOGIN_NEW :{

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        executorService.execute(() ->{
                            String username = usernameText.getText().toString();
                            String password = passwordText.getText().toString();
                            Profile profile;
                            try {
                                profile = new Profile(username, password);
                            } catch (RequestFailedException e) {
                                Log.d(TAG, "could not reach server");
                                throw new RuntimeException(e);
                                //return;
                            } catch (HttpErrorStatusException e) {
                                Log.d(TAG, "invalid password");
                                return;
                            }
                            DataManager.getInstance().selectProfile(DataManager.getInstance().addProfile(profile));
                            finish();
                        });
                    }
                });

                //TODO: remove after debugging;
                ;passwordText.setText("password");
                ;submitButton.performClick();

                break;
            }
            default:{
                Log.w(TAG, "provided action does not exist: " + action);
                this.finish();
                break;
            }
        }
    }
}