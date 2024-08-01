package com.example.javaapk.activities.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import net.anax.appServerClient.client.data.ID;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.HashSet;

public class ManageSingleGroupActivity extends AppCompatActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("GROUP_CHANGED")){
                refresh();
            }
        };
    };

    int groupId;
    Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_single_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId", -1);

        if(groupId == -1 || !DataManager.getInstance().isProfileSelected()){
            this.finish();
        }
        profile = DataManager.getInstance().getSelectedProfile();

        IntentFilter filter = new IntentFilter("GROUP_CHANGED");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }else{
            registerReceiver(receiver, filter);
        }

        refresh();

    }

    public void refresh(){

        TextView groupNameView = findViewById(R.id.text_view_manage_group_group_name);
        TextView adminNameView = findViewById(R.id.text_view_manage_group_admin_name);
        TextView treasurerNameView = findViewById(R.id.text_view_manage_group_treasurer_name);
        TextView memberCountView = findViewById(R.id.text_view_manage_group_member_count);

        Button adminOptionsButton = findViewById(R.id.button_manage_group_as_admin);

        ActivityUtilities.runNetworkOperation(() -> {
            try {
                Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                String name = g.cachedName;
                ActivityUtilities.runOnMainThread(() -> {groupNameView.setText(name);});

                int adminId = g.adminUserId;

                if(adminId == profile.getId()){
                    ActivityUtilities.runOnMainThread(() -> {
                        adminOptionsButton.setEnabled(true);
                        adminOptionsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ManageSingleGroupActivity.this, GroupAdminOptionsActivity.class);
                                intent.putExtra("groupId", groupId);
                                startActivity(intent);
                            }
                        });
                    });
                }

                int treasurerId = g.treasurerUserId;

                HashSet<Integer> members = new HashSet<Integer>();
                members.addAll(g.userIds);

                int memberCount = members.size();

                ActivityUtilities.runOnMainThread(() -> {memberCountView.setText(getResources().getText(R.string.member_count) + ": " + memberCount);});

                String adminName = getResources().getString(R.string.user_name_unknown);
                String treasurerName = getResources().getString(R.string.user_name_unknown);

                if(adminId != ID.UNKNOWN.id){
                    adminName = profile.mfGradeBookHandler.memoryManager.getUser(adminId).cachedName;
                }

                if(treasurerId == ID.NONE.id){
                    treasurerName = getResources().getString(R.string.user_name_none);
                } else if (treasurerId != ID.UNKNOWN.id) {
                    treasurerName = profile.mfGradeBookHandler.memoryManager.getUser(treasurerId).cachedName;
                }
                final String finalAdminName = adminName;

                String finalTreasurerName = treasurerName;
                ActivityUtilities.runOnMainThread(() -> {
                    adminNameView.setText(getResources().getText(R.string.admin) + ": " + finalAdminName);
                    treasurerNameView.setText(getResources().getText(R.string.treasurer) + ": " + finalTreasurerName);
                });



            } catch (RequestFailedException | HttpErrorStatusException e) {
                finish();
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