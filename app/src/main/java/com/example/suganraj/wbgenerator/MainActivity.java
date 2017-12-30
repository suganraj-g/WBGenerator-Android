package com.example.suganraj.wbgenerator;

import android.app.Application;

import com.firebase.client.Firebase;


public class MainActivity extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        //previous versions of firebase adding firebase to this main activity
        Firebase.setAndroidContext(this);
    }
}
