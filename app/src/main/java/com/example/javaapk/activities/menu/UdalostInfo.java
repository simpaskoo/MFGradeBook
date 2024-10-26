package com.example.javaapk.activities.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javaapk.R;

public class UdalostInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaudalost_info);

        String taskDescription = getIntent().getStringExtra("task_description");

        Button zaplatilBtn = findViewById(R.id.zaplatil_btn);
        Toolbar toolbar = findViewById(R.id.zaplatil_user_toolbar);
        View vieww2 = findViewById(R.id.vieww2);
        zaplatilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbar.getVisibility() == View.GONE) {
                    vieww2.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    vieww2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                }
            }
        });
    }
}
