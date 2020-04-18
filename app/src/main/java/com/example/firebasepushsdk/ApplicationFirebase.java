package com.example.firebasepushsdk;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.firebasepushsdk.api.ApiServiceManager;
import com.example.firebasepushsdk.api.callback.CallBackInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Map;

import static com.example.firebasepushsdk.MyFirebaseInstanceIDService.resetInstanceId;

public final class ApplicationFirebase {
    public static Context contextSdk = null;
    public static int iconPush = R.drawable.ic_launcher_foreground;
    public static String token = "", NAME_VERSION, PACKAGE_NAME, ANDROID_ID;
    public static Map<String, String> detail;
    public static String content = "";
    public static int CODE_VERSION;
    public static boolean receivedPush = false;
    public static Map<String, String> dataPush = null;

    //set activity when click notification
    public static void init(Context context) {
        contextSdk = context;
        getApp(context);
        getTokenFCM(new Callback() {
            @Override
            public void onSuccess(String token) {
                ApiServiceManager.getInstance().getInfoAppToken(getNameDevice(), ANDROID_ID,
                        getAndroidVersion(), NAME_VERSION, String.valueOf(CODE_VERSION), PACKAGE_NAME, token
                        , new CallBackInfo() {
                            @Override
                            public void onComplete() {
                                Log.d("callapi", "success");
                            }

                            @Override
                            public void onError() {
                                Log.d("callapi", "error");
                            }
                        });
            }
        });
    }

    public static void init(Context context, int icon) {
        init(context);
        iconPush = icon;
    }

    @NonNull
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    @NonNull
    public static String getNameDevice() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }

    public static Map<String, String> getDetail() {
        return detail;
    }

    public static void setDetail(Map<String, String> detail) {
        ApplicationFirebase.detail = detail;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        ApplicationFirebase.content = content;
    }

    public static boolean checkContextDefaultSDK() {
        return contextSdk == null && receivedPush;
    }

    public static Intent viewDefaultSDK(Context context){
        receivedPush = false;
        return new Intent(context, ViewPushActivity.class);
    }

    //enable feature push notification firebase
    public static void enableFeatureNotification(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MyFirebaseMessagingService.class);
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    //disable feature push notification firebase
    public static void disableFeatureNotification(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MyFirebaseMessagingService.class);
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void setTokenFCM(String tokenFCM) {
        token = tokenFCM;
    }

    @NonNull
    public static void getTokenFCM(final Callback callback) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Log.e("TOKEN CURRENT", token);
                        setTokenFCM(token);
                        callback.onSuccess(token);
                    }
                });
    }

    //Create new token
    public static void resetToken() {
        resetInstanceId();
        getTokenFCM(new Callback() {
            @Override
            public void onSuccess(String token) {
                ApiServiceManager.getInstance().updateToken(ANDROID_ID, token, new CallBackInfo() {
                    @Override
                    public void onComplete() {
                        Log.d("callapi", "success");
                    }

                    @Override
                    public void onError() {
                        Log.d("callapi", "error");
                    }
                });
            }
        });
    }

    public static Context getContext() {
        return contextSdk;
    }

    public static int getIconPush() {
        return iconPush;
    }

    public static void getApp(Context context) {
        try {
            ANDROID_ID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            NAME_VERSION = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            CODE_VERSION = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            PACKAGE_NAME = context.getPackageName();
            Log.e("Information app", "versionName: " + NAME_VERSION + "\nversionCode: " + CODE_VERSION +
                    "\nandroid version: " + getAndroidVersion() + "\nname device: " + getNameDevice()
                    + "\npackage_name: " + PACKAGE_NAME + "\nserial_number: " + ANDROID_ID);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void onSuccess(String token);
    }
}
