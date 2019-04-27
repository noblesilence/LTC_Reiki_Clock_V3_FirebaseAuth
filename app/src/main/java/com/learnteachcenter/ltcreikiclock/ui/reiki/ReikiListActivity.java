package com.learnteachcenter.ltcreikiclock.ui.reiki;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ui.base.BaseActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

public class ReikiListActivity extends BaseActivity {

    private static final String REIKI_LIST_FRAG = "REIKI_LIST_FRAG";

    // Create menu on the right
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_reiki_list, menu);

        return true;
    }

    // Handle menu actions on the right
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // User wants to edit reiki info
        if(id == R.id.action_app_info) {
            // Display About dialog
            AlertDialog alertDialog = new AlertDialog.Builder(ReikiListActivity.this).create();
            alertDialog.setTitle("About LTC Reiki Clock");
            alertDialog.setMessage("Reiki Clock by LearnTeachCenter.com/clock");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_reiki_list);

        FragmentManager manager = getSupportFragmentManager();

        ReikiListFragment fragment = (ReikiListFragment) manager.findFragmentByTag(REIKI_LIST_FRAG);

        if(fragment == null) {
            fragment = ReikiListFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_activity_reiki_list, REIKI_LIST_FRAG);
    }
//    public void forceCrash(View view) {
//        throw new RuntimeException("This is a crash");
//    }
}
