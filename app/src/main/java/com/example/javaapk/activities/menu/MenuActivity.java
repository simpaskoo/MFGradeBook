package com.example.javaapk.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!DataManager.getInstance().isProfileSelected()){
            Intent intent = new Intent(this, ProfilesActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        Profile profile = DataManager.getInstance().getSelectedProfile();

        NavigationView navigationView = findViewById(R.id.sideMenu);
        SideMenuHelper sideMenuHelper = new SideMenuHelper(navigationView, profile, this);
        sideMenuHelper.initiateSideMenu(false);

    }
}