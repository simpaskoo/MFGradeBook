package com.example.javaapk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainClass extends AppCompatActivity {

    private Button filter_button;
    private Toolbar filter_layout;
    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaudalosti_design);
        System.out.println("ahojky");

        filter_button = (Button) findViewById(R.id.flter_button);
        filter_layout = (Toolbar) findViewById(R.id.filter_udalosti);
        textView = (TextView) findViewById(R.id.textView);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filter_layout.getVisibility() == View.GONE) {
                    // Make the included layout invisible
                    filter_layout.setVisibility(View.VISIBLE);
                } else {
                    // Make the included layout visible
                    filter_layout.setVisibility(View.GONE);
                }
            }
        });
    }

}
