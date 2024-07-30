package com.example.javaapk.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;


//the activity the user uses to select a profile.
public class ProfilesActivity extends AppCompatActivity {

    Button addProfileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //autogenerated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addProfileButton = findViewById(R.id.addProfileButton);
        addProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewProfileAndSelect();
            }
        });

        DataManager dataManager = DataManager.getInstance();
        for(Profile profile : dataManager.profiles){
            addProfileToDisplay(profile);
        }

        //TODO: remove this after debugging:
        addNewProfileAndSelect();

    }

    void selectProfile(int profileIndex){
        if(DataManager.getInstance().profiles.get(profileIndex).isPasswordRemembered()){
            DataManager.getInstance().selectProfile(profileIndex);
            this.finish();
        }else{
            Intent intent = new Intent(this, LoginIntoProfileActivity.class);
            intent.setAction(LoginIntoProfileActivity.ACTION_LOGIN_EXISTING);
            intent.putExtra("profileIndex", profileIndex);
            startActivity(intent);
            finish();
        }
    }
    void addProfileToDisplay(Profile profile){
        //add an icon displaying this profile as available to login as.
        //add a listener for the profile display so that it selects it after clicking on it.
    }
    void addNewProfileAndSelect(){
        Intent intent = new Intent(this, LoginIntoProfileActivity.class);
        intent.setAction(LoginIntoProfileActivity.ACTION_LOGIN_NEW);
        startActivity(intent);
        finish();
    }

}