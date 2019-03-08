/*
 * LTC Reiki Clock (v 2.0) by learnteachcenter.com/clock
 * Developed by Aye Aye Mon
 * 2017-2018
 *
 * ACKNOWLEDGEMENT
 * Special Thanks to Ryan Kay, Darel Bitsy, Davor Maric, Renat Kaitmazov
 * for helping me in this project.
 *
 * DONE
 * 1. Save the data in DB (DONE)
 * 3. Auto focus dialogs (DONE)
 * 4. Change app name (DONE)
 * 5. Add play/pause/stop buttons to ReikiActivity (DONE)
 * 6. Let reordering (DONE)
 * 7. Cascade delete (DONE)
 * 8. Let Reiki info update
 * 9. Let Position info update
 * 10. Add undo in delete
 * 11. After reorder, update serial numbers
 *
 * REFERENCES
 * Let's Build A RecyclerView App | Beginner Android
 * https://www.youtube.com/watch?v=RfTJ2SePYaU&list=PLEVlop6sMHCohK2XryPUqhglysv1Xxakt
 *
 * Let's Build a Room Database App | Room, ViewModel, LiveData, Dagger 2, MVVM Architecture
 * https://www.youtube.com/playlist?list=PLEVlop6sMHCpClb5MoCSChoufFvax9lDO
 *
 * Android Studio Tutorials - Gradle Flavours (https://www.youtube.com/watch?v=7JDEK4wkN5I)
 * https://stackoverflow.com/questions/45376492/room-attempt-to-re-open-an-already-closed-database *
 * http://developer.android.com/training/material/lists-cards.html
 * http://stackoverflow.com/questions/2198410/how-to-change-title-of-activity-in-android
 * http://stackoverflow.com/questions/4233873/how-do-i-get-extra-data-from-intent-on-android
 * http://stackoverflow.com/questions/26466877/how-to-create-context-menu-for-recyclerview
 * http://stackoverflow.com/questions/27945078/onlongitemclick-in-recyclerview
 * http://stackoverflow.com/questions/9853430/refresh-fragment-when-dialogfragment-is-dismissed
 * http://stackoverflow.com/questions/19465049/changing-api-level-android-studio
 * http://stackoverflow.com/questions/35394181/switching-between-view-mode-and-edit-mode-in-android
 * http://stackoverflow.com/questions/24079960/making-a-custom-dialog-undismissable-in-android
 * https://stackoverflow.com/questions/18830205/application-crash-with-android-app-application-cannot-be-cast-to
 * */

package com.learnteachcenter.ltcreikiclock.ui.reiki;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ui.BaseActivity;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

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
        Fabric.with(this, new Crashlytics());

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
