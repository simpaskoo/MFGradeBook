package com.example.javaapk.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.javaapk.R;
import com.example.javaapk.activities.assessments.AssessmentsActivity;
import com.example.javaapk.activities.timetable.TimetableActivity;

public class NavigationFragment extends Fragment {

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        /*FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create an instance of your fragment
        Fragment stickyFragment = new NavigationFragment();  // Replace with your fragment class

        // Add fragment to the container
        fragmentTransaction.add(R.id.navigation_fragment, stickyFragment);
        fragmentTransaction.commit();*/

        /*Fragment overlayFragment = new NavigationFragment(); // Replace with your fragment class
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.add(R.id.overlay_fragment_container, overlayFragment);
        transaction.commit();*/



        ImageButton timeTable = view.findViewById(R.id.time_table_iconn);
        ImageButton events = view.findViewById(R.id.events_iconn);
        ImageButton assessments = view.findViewById(R.id.assessments_iconn);

        timeTable.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TimetableActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            timeTable.setEnabled(false);
            events.setEnabled(true);
            assessments.setEnabled(true);
        });

        events.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EventsActivity.class);
            startActivity(intent);
            events.setEnabled(false);
            timeTable.setEnabled(true);
            assessments.setEnabled(true);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        assessments.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AssessmentsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            assessments.setEnabled(false);
            timeTable.setEnabled(true);
            events.setEnabled(true);
        });

        return view;

    }
}