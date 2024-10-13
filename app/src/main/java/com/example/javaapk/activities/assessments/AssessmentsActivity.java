package com.example.javaapk.activities.assessments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.skolaOnlineScraper.data.assessment.Assessment;
import net.anax.skolaOnlineScraper.data.assessment.AssessmentList;

public class AssessmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!DataManager.getInstance().isProfileSelected()){
            //finish();
            return;
        }

        Profile profile = DataManager.getInstance().getSelectedProfile();
        //SideMenuHelper helper = new SideMenuHelper(findViewById(R.id.sideMenu), profile, this);
        //helper.initiateSideMenu();

        if(!profile.hasSkolaOnline()){
            //finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.assessments_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ActivityUtilities.runNetworkOperation(() -> {
            AssessmentList assessmentList = profile.skolaOnlineHandler.getAssessments();
            ActivityUtilities.runOnMainThread(() -> {
                AssessmentEntryAdapter adapter = new AssessmentEntryAdapter(assessmentList);
                recyclerView.setAdapter(adapter);
            });
        });

    }

    static class AssessmentEntryAdapter extends RecyclerView.Adapter<AssessmentEntryAdapter.ViewHolder> {
        AssessmentList assessmentList;

        public AssessmentEntryAdapter(AssessmentList assessmentList) {
            this.assessmentList = assessmentList;
        }

        @NonNull
        @Override
        public AssessmentEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_entry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AssessmentEntryAdapter.ViewHolder holder, int position) {
            if(position < assessmentList.assessments.length){
                Assessment a = assessmentList.assessments[position];
                holder.dateView.setText(a.date.trim());
                holder.subjectView.setText(a.subject.trim());
                holder.themeView.setText(a.theme.trim());
                holder.weightView.setText(a.theme.trim());
                holder.resultView.setText(a.result.trim());
                holder.verbalAssessmentView.setText(a.verbalAssessment.trim());
            }
        }

        @Override
        public int getItemCount() {
            return assessmentList.assessments.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView dateView;
            TextView subjectView;
            TextView themeView;
            TextView weightView;
            TextView resultView;
            TextView verbalAssessmentView;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                dateView = itemView.findViewById(R.id.text_view_assessment_entry_date);
                subjectView = itemView.findViewById(R.id.text_view_assessment_entry_subject);
                themeView = itemView.findViewById(R.id.text_view_assessment_entry_theme);
                weightView = itemView.findViewById(R.id.text_view_assessment_entry_weight);
                resultView = itemView.findViewById(R.id.text_view_assessment_entry_result);
                verbalAssessmentView = itemView.findViewById(R.id.text_view_assessment_entry_verbal_assessment);
            }
        }
    }

}