package com.example.javaapk.activities.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class ManageGroupsActivity extends AppCompatActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("GROUP_CHANGED")){
                refreshGroups(profile);
            }
        }
    };

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_groups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivity_groups), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, new IntentFilter("GROUP_CHANGED"), Context.RECEIVER_NOT_EXPORTED);
        }else{
            registerReceiver(receiver, new IntentFilter("GROUP_CHANGED"));
        }

        if(!DataManager.getInstance().isProfileSelected()){
            ActivityUtilities.askToSelectProfile(this);
        }

        profile = DataManager.getInstance().getSelectedProfile();

        Button addNewGroupButton = findViewById(R.id.new_group_btn);

        addNewGroupButton.setOnClickListener(v -> {
            ActivityUtilities.InputDialogHelper dialogHelper = new ActivityUtilities.InputDialogHelper(getResources().getString(R.string.enter_name), getResources().getString(R.string.name), ManageGroupsActivity.this);
            dialogHelper.initDialog((dialog, which) -> ActivityUtilities.runNetworkOperation(() -> {
                dialog.dismiss();
                try {
                    String name = dialogHelper.input.getText().toString();
                    if(name.isEmpty()){return;}

                    profile.mfGradeBookHandler.createGroup(name);
                    sendBroadcast(new Intent("GROUP_CHANGED"));
                } catch (RequestFailedException | HttpErrorStatusException e) {
                    //TODO: handle unable to create group;
                }
            }));
        });

        ImageButton backk = findViewById(R.id.clear_btn);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        refreshGroups(profile);
    }

    void refreshGroups(Profile profile){
        ActivityUtilities.runNetworkOperation(() -> {
            int[] ids = profile.mfGradeBookHandler.getGroupIds();

            ArrayList<Group> groups = new ArrayList<>();

         //   for (int i = 0; i < groups.size(); i++) {
            //    final int index = i;  // This is necessary to refer to the correct button inside the loop
             //   groups.get(i).setOnClickListener(new View.OnClickListener() {
                   // @Override
                  //  public void onClick(View view) {
                   //     Intent intent = new Intent(ManageGroupsActivity.this, ManageSingleGroupActivity.class);
                   //     //intent.putExtra("groupId", g.id);
                   //     ManageGroupsActivity.this.startActivity(intent);
                   // }
               // });

                /*groups.get(i).findViewById(R.id.group_toolbar).setOnClickListener(v -> {
                    ;;;System.out.println("HERE 354643543,5");
                    Intent intent = new Intent(ManageGroupsActivity.this, ManageSingleGroupActivity.class);
                    intent.putExtra("groupId", g.id);
                    ManageGroupsActivity.this.startActivity(intent);
                });*/
          //  }



            for(int id: ids){
                try {
                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(id);
                    groups.add(g);
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
            }

            ActivityUtilities.runOnMainThread(()->{

                int marginTopPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                int marginBottomPx = marginTopPx;

                ScrollView groupsScrollView = findViewById(R.id.groups_scroll_view);
                LinearLayout groupLinearLayout = findViewById(R.id.group_linearlayout);
                groupLinearLayout.removeAllViews();

                for(Group g : groups){
                    View view = getLayoutInflater().inflate(R.layout.aadynamic_group_layout, groupLinearLayout, false);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, marginTopPx, 0, marginBottomPx);
                    view.setLayoutParams(params);

                    TextView groupName = view.findViewById(R.id.group_name);
                    TextView memberAmount = view.findViewById(R.id.member_amount);


                    groupName.setText(g.cachedName);
                    memberAmount.setText(" - " + g.userIds.size() + " members");

                    view.findViewById(R.id.group_toolbar).setOnClickListener(v -> {
                        ;;;System.out.println("HERE 354643543,5");
                        Intent intent = new Intent(ManageGroupsActivity.this, ManageSingleGroupActivity.class);
                        intent.putExtra("groupId", g.id);
                        ManageGroupsActivity.this.startActivity(intent);
                    });

                    groupLinearLayout.addView(view);
                    //groupsScrollView.addView(groupLinearLayout);
                }

            });
        });
    }
}