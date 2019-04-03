package com.learnteachcenter.ltcreikiclock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.auth.LoginActivity;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "Reiki";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // already signed in
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d(TAG, "current user email is " + user.getEmail());
        } else {
            Log.d(TAG, "no current user");
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}