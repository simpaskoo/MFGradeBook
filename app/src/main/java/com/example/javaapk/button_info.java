package com.example.javaapk;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

public class button_info extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_info);

        //APPLY SHAPE ON MENU + ADD MENU TO ACTIVITY
        Drawable drawable = getResources().getDrawable(R.drawable.menu_layers);
        getSupportActionBar().setBackgroundDrawable(drawable);
    }

    //ADDS TOP MENU OPTIONS BUTTONS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

}
