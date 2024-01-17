package com.example.javaapk;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class CustomCardView extends CardView {

    public CustomCardView(Context context) {
        super(context);

        initialize(); // Call the initialization method
    }

    public void initialize() {
        // Step 1: Create the CardView
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.MATCH_PARENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(10)); // Set margin bottom in dp

        this.setLayoutParams(cardParams);
        this.setClickable(true);

        // Step 2: Create the LinearLayout inside the CardView
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(0xFF5F12E6); // Set background color

        linearLayout.setLayoutParams(linearParams);
        this.addView(linearLayout);

        // Step 3: Create the first TextView (textView1) inside the LinearLayout
        TextView textView1 = new TextView(getContext());
        LinearLayout.LayoutParams textView1Params = new LinearLayout.LayoutParams(
                dpToPx(240), // Set width in dp
                dpToPx(55)   // Set height in dp
        );
        textView1Params.weight = 1; // Set layout weight
        textView1.setGravity(Gravity.CENTER_VERTICAL); // Center text vertically
        textView1.setText("Název"); // Set text
        textView1.setTextSize(dpToPx(20)); // Set text size in sp
        textView1.setLayoutParams(textView1Params); // Set layout parameters
        linearLayout.addView(textView1); // Add textView1 to the LinearLayout

        // Step 4: Create the second TextView (textView2) inside the LinearLayout
        TextView textView2 = new TextView(getContext());
        LinearLayout.LayoutParams textView2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dpToPx(55)
        );
        textView2Params.weight = 1; // Set layout weight
        textView2.setGravity(Gravity.CENTER); // Center text horizontally
        textView2.setText("Cena"); // Set text
        textView2.setTextSize(dpToPx(20)); // Set text size in sp
        textView2.setLayoutParams(textView2Params); // Set layout parameters
        linearLayout.addView(textView2); // Add textView2 to the LinearLayout
    }

    // Helper method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
