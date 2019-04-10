package com.learnteachcenter.ltcreikiclock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.auth.LoginActivity;
import com.learnteachcenter.ltcreikiclock.auth.SessionManager;
import com.learnteachcenter.ltcreikiclock.ui.reiki.ReikiListActivity;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "Reiki";

    private SessionManager mSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check session
        mSession = new SessionManager(getApplicationContext());
        mSession.checkLoginAndRedirect();
    }
}
