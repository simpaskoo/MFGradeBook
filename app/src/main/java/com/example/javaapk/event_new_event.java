package com.example.javaapk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class event_new_event extends AppCompatActivity {

    public FloatingActionButton AddActionButton;
    public CustomCardView CustomCV;
    public Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        AddActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        AddActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(event_new_event.this, MainActivity.class);
                intent.putExtra("doAddAction",true);
                event_new_event.this.finish();
                startActivity(intent);
            }
        });
    }

    /*Intent intent = new Intent(event_new_event.this,  MainActivity.class);
                intent.putExtra( "doAddAction", true);
    startActivity(intent);
                if (getIntent().getBooleanExtra("doAddAction", false)) {
        AddAction();
    }*/
    /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("doAddAction", false)) {
            AddAction();
        }
    }*/


    /*@SuppressLint("ResourceType")
    public void AddAction(){
        CustomCV = new CustomCardView(AddActionButton.getContext());

        // Add CustomCardView to the layout of the activity
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        CustomCV.setLayoutParams(layoutParams);

        //MainActivity MainActivity = new MainActivity();
        //MainActivity.setCV();
        
        setContentView(CustomCV);
        //MainActivity mainActivity = new MainActivity();
        //MainActivity.setContentView(CustomCardView);
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}