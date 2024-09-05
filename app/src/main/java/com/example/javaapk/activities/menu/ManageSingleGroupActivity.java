package com.example.javaapk.activities.menu;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import net.anax.appServerClient.client.data.User;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ManageSingleGroupActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;

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
    @SuppressLint({"InlinedApi", "UnspecifiedRegisterReceiverFlag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaactivity_group_info);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivity_groups_info), (v, insets) -> {
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

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getData() != null){
                int[] userIds = result.getData().getIntArrayExtra("selectedUserIds");
                if(userIds != null){
                    ActivityUtilities.runNetworkOperation(() -> {
                        for(int id : userIds){
                            try {
                                Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                                g.requestSetIsInGroup(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), id, true, DataManager.REMOTE_SERVER);
                            } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                        }
                        ActivityUtilities.runOnMainThread(() -> ManageSingleGroupActivity.this.sendBroadcast(new Intent("GROUP_CHANGED")));
                    });
                }
            }
        });

        refresh();

    }

    public void refresh(){

        TextView groupNameView = findViewById(R.id.nazev_skupiny);
        TextView groupAccessCodeView = findViewById(R.id.code);

        ActivityUtilities.runNetworkOperation(() -> {
            try {
                Group g = profile.mfGradeBookHandler.memoryManager.getGroup(groupId);
                String name = g.cachedName;

                ActivityUtilities.runOnMainThread(() -> {
                    groupNameView.setText(name);
                    groupAccessCodeView.setText(getResources().getText(R.string.access_code) + ": " + g.cachedAccessCode);
                });

                HashSet<Integer> members = new HashSet<Integer>();
                members.addAll(g.userIds);

                ArrayList<User> users = new ArrayList<>();
                for (int memberId : members){
                    User u = profile.mfGradeBookHandler.memoryManager.getUser(memberId);
                    users.add(u);
                }

                boolean isAdmin = g.adminUserId == profile.getId();

                ActivityUtilities.runOnMainThread(() -> {
                    LinearLayout usersLayout = findViewById(R.id.group_linearlayout);
                    usersLayout.removeAllViews();

                    int marginTopPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                    for(User user : users){
                        View view = getLayoutInflater().inflate(R.layout.aadynamic_user, usersLayout, false);

                        TextView nameText = view.findViewById(R.id.user_name);
                        nameText.setText(user.cachedName);

                        Button makeAdminButton = view.findViewById(R.id.admin_btn);
                        Button makeTreasurerButton = view.findViewById(R.id.pokladnik_btn);
                        Button kickButton = view.findViewById(R.id.smazat_btn);

                        if(!isAdmin){
                            makeAdminButton.setVisibility(View.GONE);
                            makeTreasurerButton.setVisibility(View.GONE);
                            kickButton.setVisibility(View.GONE);
                            view.findViewById(R.id.user_options).setVisibility(View.GONE);
                        }else{
                            makeAdminButton.setOnClickListener(v -> {
                                ActivityUtilities.runNetworkOperation(() -> {
                                    try {
                                        g.requestSetAdminUserId(profile.mfGradeBookHandler.memoryManager.getClient().getToken(),user.getId() , DataManager.REMOTE_SERVER);
                                        sendBroadcast(new Intent("GROUP_CHANGED"));
                                    } catch (RequestFailedException | HttpErrorStatusException ignored) {}

                                });
                            });
                            makeTreasurerButton.setOnClickListener(v -> {
                                ActivityUtilities.runNetworkOperation(() -> {
                                    try {
                                        g.requestSetTreasurerUserId(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), user.getId(), DataManager.REMOTE_SERVER);
                                        sendBroadcast(new Intent("GROUP_CHANGED"));
                                    } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                                });
                            });
                            kickButton.setOnClickListener(v -> {
                                ActivityUtilities.runNetworkOperation(() -> {
                                    try {
                                        g.requestSetIsInGroup(profile.mfGradeBookHandler.memoryManager.getClient().getToken(),user.getId(), false, DataManager.REMOTE_SERVER);
                                        sendBroadcast(new Intent("GROUP_CHANGED"));
                                    } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                                });
                            });
                        }

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, marginTopPx, 0, 0);
                        view.setLayoutParams(params);
                        usersLayout.addView(view);

                    }

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