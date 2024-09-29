package com.example.javaapk.activities.events;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.skolaOnlineScraper.data.assessment.Assessment;
import net.anax.skolaOnlineScraper.data.assessment.AssessmentList;

public class AssessmentsFragment extends Fragment {

    public AssessmentsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assesstments, container, false);

        if(DataManager.getInstance().isProfileSelected() && DataManager.getInstance().getSelectedProfile().hasSkolaOnline()){
            Profile profile = DataManager.getInstance().getSelectedProfile();

            ActivityUtilities.runNetworkOperation(() -> {
                AssessmentList assessments = profile.skolaOnlineHandler.getAssessments();

                ActivityUtilities.runOnMainThread(() -> {
                    LinearLayout layout = view.findViewById(R.id.assessments_linear_layout);
                    layout.removeAllViews();

                    int marginTopPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
                    int marginBottomPx = marginTopPx;

                    for(Assessment assessment : assessments.assessments){

                        View assessmentView = getLayoutInflater().inflate(R.layout.aadynamic_assessments_layout, layout, false);

                        TextView value = assessmentView.findViewById(R.id.assessment_value_textView);
                        TextView subject = assessmentView.findViewById(R.id.assessment_subject_textView);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        params.setMargins(0 , marginTopPx, 0, marginBottomPx);
                        assessmentView.setLayoutParams(params);

                        value.setText(assessment.result);
                        subject.setText(assessment.subject);

                        assessmentView.setVisibility(View.VISIBLE);
                        layout.addView(assessmentView);
                    }
                    layout.requestLayout();

                });
            });
        }
        return view;
    }

}