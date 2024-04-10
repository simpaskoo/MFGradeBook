package com.example.javaapk;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout;

public class CustomFieldButton{
    public void CreateButton() {
        // Create a Button instance
        //Button button = new Button(context);

        // Set Button attributes programmatically
        /*button.setId(R.id.fieldButton);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                80 // 80dp height
        ));
        button.setText("Btn1");
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.setMargins(20, 0, 20, 0); // Set margins
        button.setLayoutParams(params);

        return button;*/
    }




    /*public CustomFieldButton(CustomFieldButton cntx){
        super();
        initialization();
    }*/

    /*public CustomFieldButton initialization () {
        // Create a new button
        //Button button = customFieldButton.createButton(this);
        CustomFieldButton cfb = new CustomFieldButton(this);

        // Set text for the button
        //cfb.setText("Text");

        // Set layout parameters for the button
        //LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

        //cfb.setBackgroundColor(Color.argb(98,0,238,255));
        //cfb.setBackgroundColor();
       // cfb.setBackgroundColor(/*0xFF5F12E6*/ //0xFFEA421E);
        // Set margins for the button
        //params.setMarginStart(activity.getResources().getDimensionPixelSize(dpToPx(20, activity)));
        //params.setMarginEnd(activity.getResources().getDimensionPixelSize(dpToPx(20, activity)));

        // Apply layout parameters to the button
        //cfb.setLayoutParams(params);

        //return cfb;
    //}


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
