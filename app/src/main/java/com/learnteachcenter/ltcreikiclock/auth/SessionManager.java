package com.learnteachcenter.ltcreikiclock.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.learnteachcenter.ltcreikiclock.ui.reiki.ReikiListActivity;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class SessionManager {
    private static final String TAG = "Reiki";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;

    FirebaseAuth mAuth;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LTCREIKICLOCKPref";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";

    public SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

//        String name = pref.getString(KEY_NAME, null);
//        String email = pref.getString(KEY_EMAIL, null);
//        String token = pref.getString(KEY_TOKEN, null);
//
//        Log.d(TAG, "Name: " + name);
//        Log.d(TAG, "Email: " + email);
//        Log.d(TAG, "Token: " + token);
    }

    public String getToken(){
        String token = pref.getString(KEY_TOKEN, null);
        return token;
    }

    private void setToken(String token){
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String email, String token) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));

        return user;
    }

    /**
     * Quick check for login
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Check login method will check user login status
     * If false, it'll redirect user to Login page
     * Else redirect to Reiki list page
     */
    public void checkLoginAndRedirect(){
        // Get refresh token from Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            setToken(task.getResult().getToken());

                            redirectToMain();
                        }
                    });
        } else {
            redirectToLogin();
        }
    }

    /**
     * Clear session details
     */
    public void logoutUser(){
        editor.clear();
        editor.commit();

        redirectToLogin();
    }

    public void redirectToMain(){
        Intent i = new Intent(mContext, ReikiListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    private void redirectToLogin(){
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}
