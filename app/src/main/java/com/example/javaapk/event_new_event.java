package com.example.javaapk;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.content.Intent.parseIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class event_new_event extends AppCompatActivity {

    public FloatingActionButton AddActionButton;
    //public int n;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        AddActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        AddActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.getInstance().Buttonbutton();
                onBackPressed();

            }
        });

    }

    /*public int btnNmbr() {
        if(n < n++) {
            n++;
        }


    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}