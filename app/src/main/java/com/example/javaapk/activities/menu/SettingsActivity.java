package com.example.javaapk.activities.menu;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;

import net.anax.appServerClient.client.data.MissingDataException;
import net.anax.appServerClient.client.util.StringUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {
    EditText usernameEditText;
    Button setUsernameButton;

    EditText passwordEditText;
    Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        usernameEditText = findViewById(R.id.sko_name_edit_text);
        setUsernameButton = findViewById(R.id.sko_set_username_button);

        passwordEditText = findViewById(R.id.sko_password_edit_text);

        if(!DataManager.getInstance().isProfileSelected()){
            finish();
            return;
        }
        profile = DataManager.getInstance().getSelectedProfile();
        SideMenuHelper sideMenuHelper = new SideMenuHelper(findViewById(R.id.sideMenu), profile, this);
        sideMenuHelper.initiateSideMenu();

        Button testLoadDataButton = findViewById(R.id.test_button_load_data);
        Button testSaveDataButton = findViewById(R.id.test_button_save_data);

        testSaveDataButton.setOnClickListener(v -> {
            try {
                String[] data = StringUtilities.chopUp(3000, DataManager.getInstance().toJson().toJSONString());
                System.out.println("saving data manager, json: ");
                for(String line : data){System.out.println(line);}
                DataManager.getInstance().saveData(this);
            } catch (IOException e) {
                System.out.println("failed to save data");
                e.printStackTrace();
            }
        });

        testLoadDataButton.setOnClickListener(v -> {
            DataManager.loadData(this);
            String[] data = StringUtilities.chopUp(3000,  DataManager.getInstance().toJson().toJSONString());
            System.out.println("data manager json after load: ");
            for(String line : data){System.out.println(line);}
        });




        refreshUsernameEditTextColor();
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(profile.hasSkolaOnline()){
                    profile.skolaOnlineHandler.password = s.toString();
                }
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                refreshUsernameEditTextColor();
            }
        });

        setUsernameButton.setOnClickListener(v -> {
            if(!profile.hasSkolaOnline() || !profile.skolaOnlineHandler.username.equals(usernameEditText.getText().toString())){
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Warning")
                        .setMessage("changing the username used to access SkolaOnline will delete all SkolaOnline offline data saved in this application")
                        .setPositiveButton("change username", (dialog, which) -> {
                            dialog.dismiss();
                            profile.initiateSkolaOnline(usernameEditText.getText().toString(), passwordEditText.getText().toString());

                            refreshUsernameEditTextColor();
                        })
                        .setNegativeButton("go back", (dialog, which) -> {
                            dialog.dismiss();
                            refresh();
                        }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        refresh();
    }

    public void refreshUsernameEditTextColor(){
        if(!profile.hasSkolaOnline() || !profile.skolaOnlineHandler.username.equals(usernameEditText.getText().toString())){
            usernameEditText.setTextColor(ContextCompat.getColor(SettingsActivity.this, android.R.color.holo_blue_light));
            usernameEditText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SettingsActivity.this, android.R.color.holo_blue_light)));
        }else{
            usernameEditText.setTextColor(ContextCompat.getColor(SettingsActivity.this, android.R.color.black));
            usernameEditText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(SettingsActivity.this, android.R.color.black)));
        }
    }
    public void refresh(){
        if(profile.hasSkolaOnline()){
            usernameEditText.setText(profile.skolaOnlineHandler.username);
            passwordEditText.setText(profile.skolaOnlineHandler.password);
        }
        refreshUsernameEditTextColor();
    }

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        finish();
        return super.getOnBackInvokedDispatcher();
    }
}