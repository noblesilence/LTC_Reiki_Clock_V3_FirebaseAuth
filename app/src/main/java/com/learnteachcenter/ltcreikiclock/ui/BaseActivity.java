package com.learnteachcenter.ltcreikiclock.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Simply convenience Parent class as all Activities will contain a Fragment which is the View,
 * and this way you only write the Code in one place instead of repeating it for each Activity.
 * Created by R_KAY on 8/9/2017.
 */

/**
 * Created by aye2m on 10/14/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static void addFragmentToActivity(FragmentManager fragmentManager,
                                             Fragment fragment,
                                             int frameId,
                                             String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }
}
