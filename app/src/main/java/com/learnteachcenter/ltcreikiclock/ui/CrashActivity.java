//https://trivedihardik.wordpress.com/2011/08/20/how-to-avoid-force-close-error-in-android/

package com.learnteachcenter.ltcreikiclock.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.learnteachcenter.ltcreikiclock.utils.ExceptionHandler;
import com.learnteachcenter.ltcreikiclock.R;

public class CrashActivity extends Activity {

    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_crash);

        error = (TextView) findViewById(R.id.message);

        error.setText(getIntent().getStringExtra("error"));

        error.setMovementMethod(new ScrollingMovementMethod());
    }
}
