package com.example.javaapk.activities.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.Group;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if(!DataManager.getInstance().isProfileSelected()){
            ActivityUtilities.askToSelectProfile(this);
        }

        Profile profile = DataManager.getInstance().getSelectedProfile();

        SideMenuHelper menuHelper = new SideMenuHelper(findViewById(R.id.sideMenu), profile, this);
        menuHelper.initiateSideMenu();

        RecyclerView recyclerView = findViewById(R.id.group_list_recycler_view);

        ActivityUtilities.runNetworkOperation(() -> {
            int[] ids = profile.mfGradeBookHandler.getGroupIds();

            ArrayList<Group> groups = new ArrayList<>();
            for(int id: ids){
                try {
                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(id);
                    groups.add(g);
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
            }

            ActivityUtilities.runOnMainThread(()->{
                GroupEntryAdapter adapter = new GroupEntryAdapter(groups);
                recyclerView.setAdapter(adapter);
            });

        });



    }

    static class GroupEntryAdapter extends RecyclerView.Adapter<GroupEntryAdapter.ViewHolder>{
        List<Group> groups;
        GroupEntryAdapter(List<Group> groups){
            this.groups = groups;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_entry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Group g = groups.get(position);
            holder.groupNameView.setText(g.cachedName);
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView groupNameView;
            public ViewHolder(@NonNull View itemView){
                super(itemView);
                groupNameView = itemView.findViewById(R.id.group_entry_name);
            }

        }
    }
}