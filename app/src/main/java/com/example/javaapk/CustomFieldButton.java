package com.example.javaapk;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout;

public class CustomFieldButton {

    public Button createButton (MainActivity activity) {
        // Create a new button
        Button button = new Button(activity);

        // Set text for the button
        button.setText("Text");

        // Set layout parameters for the button
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

        // Set margins for the button
        params.setMarginStart(activity.getResources().getDimensionPixelSize(dpToPx(20, activity)));
        params.setMarginEnd(activity.getResources().getDimensionPixelSize(dpToPx(20, activity)));

        // Apply layout parameters to the button
        button.setLayoutParams(params);

        return button;
    }

    // Helper method to convert dp to pixels
    /*private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }*/

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /*private int spToPx(float sp) {
        float density = getResources().getDisplayMetrics().density;
        //return TypedValue.COMPLEX_UNIT_SP, sp,
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }*/

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
