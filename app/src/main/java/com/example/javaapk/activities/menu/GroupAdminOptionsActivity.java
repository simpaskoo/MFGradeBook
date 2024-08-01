package com.example.javaapk.activities.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
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

public class GroupAdminOptionsActivity extends AppCompatActivity {

    int groupId;
    Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_admin_options);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        groupId = getIntent().getIntExtra("groupId", -1);
        if(groupId == -1 || !DataManager.getInstance().isProfileSelected()){finish();}
        profile = DataManager.getInstance().getSelectedProfile();

        this.refresh();

    }

    public void refresh(){
        TextView groupNameView = findViewById(R.id.text_view_group_name);
        ActivityUtilities.runNetworkOperation(() -> {
            try {
                String name = profile.mfGradeBookHandler.memoryManager.getGroup(groupId).cachedName;
                ActivityUtilities.runOnMainThread(() -> groupNameView.setText(name));

            } catch (RequestFailedException | HttpErrorStatusException ignored) {}
        });

        Button changeNameButton = findViewById(R.id.button_change_group_name);
        Button changeTreasurerButton = findViewById(R.id.button_change_group_treasurer);
        Button changeAdminButton = findViewById(R.id.button_change_group_admin);
        Button rerollAccessCodeButton = findViewById(R.id.button_reroll_group_access_code);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.InputDialogHelper inputDialogHelper = new ActivityUtilities.InputDialogHelper(
                        getResources().getString(R.string.enter_name),
                        getResources().getString(R.string.name),
                        GroupAdminOptionsActivity.this);

                inputDialogHelper.initDialog(
                        (dialog, which) -> {
                            dialog.dismiss();
                            ActivityUtilities.runNetworkOperation(() -> {
                                String newName = inputDialogHelper.input.getText().toString();
                                if(newName.isEmpty()){
                                    //TODO: handle empty name
                                    return;}
                                try {
                                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                                    g.requestSetName(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), newName, DataManager.REMOTE_SERVER);

                                    sendBroadcast(new Intent("GROUP_CHANGED"));
                                    refresh();

                                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                            });
                        }
                );
            }
        });
        changeTreasurerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        finish();
        return super.getOnBackInvokedDispatcher();
    }
}