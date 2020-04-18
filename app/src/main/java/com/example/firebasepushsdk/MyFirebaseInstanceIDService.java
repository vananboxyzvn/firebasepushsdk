package com.example.firebasepushsdk;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class MyFirebaseInstanceIDService {

    public static void resetInstanceId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
