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

        // TODO: Remove the code below

        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//
//        if (mAuth.getCurrentUser() != null) {
//            // already signed in
//            FirebaseUser user = mAuth.getCurrentUser();
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            user.getIdToken(true)
//                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<GetTokenResult> task) {
//                            if(task.isSuccessful()) {
//                                String idToken = task.getResult().getToken();
//                            } else {
//
//                            }
//                        }
//                    });
//
//            Log.d(TAG, "[Splash Activity] current user email is " + user.getEmail());
//
//            Intent i = new Intent(this, ReikiListActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        } else {
//            Log.d(TAG, "[Splash Activity] no current user");
//            Intent i = new Intent(this, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }
    }
}
