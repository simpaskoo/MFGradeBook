package com.example.javaapk.activities.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.Group;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.User;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.ArrayList;

public class GroupAdminOptionsActivity extends AppCompatActivity {

    int groupId;
    Profile profile;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("GROUP_CHANGED")){
                refresh();
            }
        }
    };

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter("GROUP_CHANGED"), Context.RECEIVER_NOT_EXPORTED);
        }else{
            registerReceiver(receiver, new IntentFilter("GROUP_CHANGED"));
        }
        this.refresh();
    }

    public void refresh(){
        TextView groupNameView = findViewById(R.id.text_view_group_name);
        RecyclerView recyclerView = findViewById(R.id.member_users_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ActivityUtilities.runNetworkOperation(() -> {
            try {
                Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);

                if(g.adminUserId != profile.getId()){
                    finish();
                    return;
                }

                String name = g.cachedName;
                ActivityUtilities.runOnMainThread(() -> groupNameView.setText(name));

                ArrayList<User> members = new ArrayList<>();
                for(int memberId : g.userIds){
                    try{
                        User u = profile.mfGradeBookHandler.memoryManager.getUser(memberId);
                        members.add(u);
                    } catch (RequestFailedException | HttpErrorStatusException ignored){}
                }
                ActivityUtilities.runOnMainThread(() -> {

                    MemberUserEntryAdapter adapter = new MemberUserEntryAdapter(members, profile, groupId, this::refresh, this);
                    recyclerView.setAdapter(adapter);
                });
            } catch (RequestFailedException | HttpErrorStatusException ignored) {}
        });

        Button changeNameButton = findViewById(R.id.button_change_group_name);
        Button rerollAccessCodeButton = findViewById(R.id.button_reroll_group_access_code);

        rerollAccessCodeButton.setOnClickListener(v -> {
            ActivityUtilities.runNetworkOperation(() -> {
                try {
                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                    g.requestRerollAccessCode(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), DataManager.REMOTE_SERVER);
                    ActivityUtilities.runOnMainThread(() -> sendBroadcast(new Intent("GROUP_CHANGED")));
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
            });
        });

        changeNameButton.setOnClickListener(v -> {
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
                            } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                        });
                    }
            );
        });
    }

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        finish();
        return super.getOnBackInvokedDispatcher();
    }

    static class MemberUserEntryAdapter extends RecyclerView.Adapter<MemberUserEntryAdapter.ViewHolder> {
        ArrayList<User> members;
        Profile profile;
        int groupId;
        Runnable refresh;
        Activity activity;

        public MemberUserEntryAdapter(ArrayList<User> members, Profile profile, int groupId, Runnable refresh, Activity activity) {
            this.members = members;
            this.profile = profile;
            this.groupId = groupId;
            this.refresh = refresh;
            this.activity = activity;
        }

        @NonNull
        @Override
        public MemberUserEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_entry_admin_options, parent, false);
            return new MemberUserEntryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MemberUserEntryAdapter.ViewHolder holder, int position) {
            User member = members.get(position);
            holder.userId = member.getId();
            holder.userName.setText(member.cachedName);
            holder.itemView.setOnClickListener(v -> {
                new AlertDialog.Builder(activity)
                        .setTitle(member.cachedName)
                        .setPositiveButton(R.string.kick_user, (dialog, which) -> {
                            dialog.dismiss();
                            ActivityUtilities.runNetworkOperation(() -> {
                                try {
                                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                                    g.requestSetIsInGroup(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), holder.userId, false, DataManager.REMOTE_SERVER);
                                    activity.sendBroadcast(new Intent("GROUP_CHANGED"));
                                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                            });

                        })
                        .setNeutralButton(R.string.make_admin, (dialog, which) -> {
                            dialog.dismiss();
                            ActivityUtilities.runNetworkOperation(() -> {
                                try {
                                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                                    g.requestSetAdminUserId(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), holder.userId, DataManager.REMOTE_SERVER);
                                    activity.sendBroadcast(new Intent("GROUP_CHANGED"));
                                } catch (RequestFailedException | HttpErrorStatusException ignored) {}

                            });
                        })
                        .setNegativeButton(R.string.make_treasurer, (dialog, which) -> {
                            dialog.dismiss();
                            ActivityUtilities.runNetworkOperation(() -> {
                                try {
                                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                                    g.requestSetTreasurerUserId(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), holder.userId, DataManager.REMOTE_SERVER);
                                    activity.sendBroadcast(new Intent("GROUP_CHANGED"));
                                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                            });

                        }).show();
            });

        }

        @Override
        public int getItemCount() {
            return members.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView userName;
            View itemView;
            int userId;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.user_name_text_view);
                this.itemView = itemView;
            }
        }
    }
}