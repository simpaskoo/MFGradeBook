package com.example.javaapk.activities.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.Group;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

public class CreateNewGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(!DataManager.getInstance().isProfileSelected()){
            finish();
            return;
        }

        Profile profile = DataManager.getInstance().getSelectedProfile();
        Button submitButton = findViewById(R.id.button_submit_create_group);
        EditText nameText = findViewById(R.id.edit_text_input_group_name);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityUtilities.runNetworkOperation(() -> {
                try {
                    String name = nameText.getText().toString();
                    if(name.isEmpty()){return;}
                    profile.mfGradeBookHandler.createGroup(name);
                    setResult(RESULT_OK);
                    finish();
                } catch (RequestFailedException | HttpErrorStatusException e) {
                    //TODO: handle unable to create group;
                    setResult(RESULT_CANCELED);
                    finish();
                }});
            }});

    }
}