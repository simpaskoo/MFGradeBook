package com.example.javaapk.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.javaapk.R;
import com.example.javaapk.activities.menu.ProfilesActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
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
                textView.setText(R.string.unavailable);
            }else{
                textView.setText(fetchedString);
            }
        });

    }

    public static void runOnMainThread(Runnable command){
        mainThreadHandler.post(command);
    }

}
