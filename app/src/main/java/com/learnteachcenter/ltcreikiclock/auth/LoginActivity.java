package com.learnteachcenter.ltcreikiclock.auth;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.learnteachcenter.ltcreikiclock.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A login screen that offers login with Google
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Reiki";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void googleBtnUi() {
        // TODO Auto-generated method stub

        SignInButton googleButton = (SignInButton) findViewById(R.id.google_button);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        for (int i = 0; i < googleButton.getChildCount(); i++) {
            View v = googleButton.getChildAt(i);

            if (v instanceof TextView)
            {
                TextView tv = (TextView) v;
                tv.setTextSize(14);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText("My Text");
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setSingleLine(true);
                tv.setPadding(15, 15, 15, 15);

                ViewGroup.LayoutParams params = tv.getLayoutParams();
                params.width = 100;
                params.height = 70;
                tv.setLayoutParams(params);

                return;
            }
        }
    }
}

