package com.example.firebasepushsdk;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.example.firebasepushsdk.MyFirebaseInstanceIDService.resetInstanceId;

public final class ApplicationFirebase {
    public static Context contextSdk;
    public static int iconPush = R.drawable.ic_launcher_foreground;

    //set activity when click notification
    public static void setContext(Context context, int icon) {
        contextSdk = context;
        iconPush = icon;
        getToken();
    }

    public static void setContext(Context context) {
        contextSdk = context;
        getToken();
    }

    public static void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("TOKEN CURRENT", token);
                    }
                });
    }

    //Create new token
    public static void resetToken() {
        resetInstanceId();
    }

    public static Context getContext() {
        return contextSdk;
    }

    public static int getIconPush() {
        return iconPush;
    }
}
