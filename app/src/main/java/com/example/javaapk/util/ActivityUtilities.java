package com.example.javaapk.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.javaapk.R;
import com.example.javaapk.activities.menu.ProfilesActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ActivityUtilities {
    final static ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public static void askToSelectProfile(Activity parent){
        Intent intent = new Intent(parent, ProfilesActivity.class);
        parent.startActivity(intent);
    }
    public static void runNetworkOperation(Runnable command){
        executorService.execute(command);
    }

    public static void updateTextViewWithRemoteString(TextView textView, Supplier<String> fetchStringFromServer){
            textView.setText(R.string.loading);
        runNetworkOperation(() -> {
            String fetchedString = fetchStringFromServer.get();
            if(fetchedString == null || fetchedString.isEmpty()){
                runOnMainThread(() -> textView.setText(R.string.unavailable));
            }else{
                runOnMainThread(() -> textView.setText(fetchedString));
            }
        });

    }

    public static void runOnMainThread(Runnable command){
        mainThreadHandler.post(command);
    }
    public static class InputDialogHelper {
        View dialogView;
        public EditText input;
        String title;
        String hing;
        Activity caller;
        public InputDialogHelper(String title, String hint, Activity caller) {
            this.title = title;
            this.hing = hint;
            this.caller = caller;

            dialogView = caller.getLayoutInflater().inflate(R.layout.input_text_dialog, null);
            input = dialogView.findViewById(R.id.input_text_dialog_edit_text);
        }

        public void initDialog(DialogInterface.OnClickListener submitListener){
            new AlertDialog.Builder(caller)
                    .setTitle(R.string.enter_name)
                    .setView(dialogView)
                    .setPositiveButton(R.string.submit, submitListener)
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}
