package com.learnteachcenter.ltcreikiclock.ui.position;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learnteachcenter.ltcreikiclock.Constants;
import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ReikiApplication;
import com.learnteachcenter.ltcreikiclock.data.Position;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.ui.helper.ItemTouchHelperAdapter;
import com.learnteachcenter.ltcreikiclock.ui.helper.ItemTouchHelperViewHolder;
import com.learnteachcenter.ltcreikiclock.ui.helper.OnStartDragListener;
import com.learnteachcenter.ltcreikiclock.ui.helper.SimpleItemTouchHelperCallback;
import com.learnteachcenter.ltcreikiclock.ui.reiki.CreateReikiActivity;
import com.learnteachcenter.ltcreikiclock.ui.reiki.ReikiListActivity;
import com.learnteachcenter.ltcreikiclock.ui.BaseActivity;
import com.learnteachcenter.ltcreikiclock.utils.IntentExtraNames;
import com.learnteachcenter.ltcreikiclock.viewmodel.ReikiCollectionViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/*
*
* https://blog.mindorks.com/android-task-and-back-stack-review-5017f2c18196
* https://medium.com/@101/android-toolbar-for-appcompatactivity-671b1d10f354
* https://stackoverflow.com/questions/33320203/toolbar-setnavigationonclicklistener-not-working*
* https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown
* https://stackoverflow.com/questions/25773928/setting-launchmode-singletask-vs-setting-activity-launchmode-singletop
* http://www.helloandroid.com/tutorials/communicating-between-running-activities
* https://stackoverflow.com/questions/11538747/android-imagebutton-how-to-change-icon-size
* https://stackoverflow.com/questions/38340358/how-to-enable-and-disable-drag-and-drop-on-a-recyclerview
* https://stackoverflow.com/questions/31113819/set-fab-icon-color
* https://stackoverflow.com/questions/27484126/adjust-icon-size-of-floating-action-button-fab
* https://stackoverflow.com/questions/43143468/how-to-center-the-elements-in-constraintlayout/43143847
* http://code.hootsuite.com/orientation-changes-on-android/
* https://stackoverflow.com/questions/33454609/detect-start-scroll-and-end-scroll-in-recyclerview
* https://stackoverflow.com/questions/33078939/android-studio-v7-import-errors-for-few-classes-cannot-resolve-symbol
*
*  DONE
*  -----
*  - Change Reiki list title
*  - Set logo
*  - Change color theme
*  - Check and remove extras
*  - Add About page
*  - Debug Reiki reordering
*  - Debug Position reordering
*
* 28 Dec 2017
* This Activity used to use fragment but it caused timer and music to stop
* when screen is off. So, fragment is removed.
* */

public class PositionListActivity extends BaseActivity implements OnStartDragListener {

    // Views
    LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private SimpleItemTouchHelperCallback mItemTouchHelperCallback;
    private LayoutInflater layoutInflater;
    private Toolbar toolbar;
    private TextView prompt;
    FloatingActionButton fabPlayPause;
    FloatingActionButton fabStop;
    FloatingActionButton fabAdd;
    MenuItem editMenuItem;
    LinearLayout adLayout;
    Button btnBuy;

    // Reiki Data
    // TODO: May refactor this into a separate class to decouple App Logic from View
    private Reiki reiki;
    private List<Position> listOfPositions;
    private MediaPlayer bgMusicPlayer;
    private CountDownTimer countdownTimer;
    private long secondsRemaining;
    private int secondsFinishing = 3;
    private int currentPositionID;
    private final String APP_MODE_PLAYING = "playing";
    private final String APP_MODE_PAUSED = "paused";
    private final String APP_MODE_STOPPED = "stopped";
    private String currentAppMode = APP_MODE_STOPPED;

    // ViewModel Factory
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ReikiCollectionViewModel reikiCollectionViewModel;

    /**
     * onCreate method of this Activity
     * @param savedInstanceState Saved data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_position_list);

        ((ReikiApplication) getApplication())
                .getApplicationComponent()
                .inject(this);  // Dependency Injection

        processIntentExtras();  // Process data passed from the previous Activity
        setupView();            // Setup UI
    }

    /**
     * Setup Views
     */
    private void setupView() {
        recyclerView = findViewById(R.id.rec_position_list_activity);
        layoutInflater = this.getLayoutInflater();
        fabAdd = findViewById(R.id.fab_add);      // Create New button
        fabPlayPause = findViewById(R.id.fab_play_pause);  // Play/pause button
        fabStop = findViewById(R.id.fab_stop);             // Stop button
        adLayout = findViewById(R.id.layout_ad);
        toolbar = findViewById(R.id.tlb_position_list_activity);    // Toolbar
        toolbar.setTitle(reiki.getTitle());
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginStart(16);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);

        prompt = findViewById(R.id.lbl_position_prompt);

        // Bind Event Listeners to Views

        // Toolbar, back button pressed
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopReiki();
                Intent intent = new Intent(getApplicationContext(), ReikiListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Stop
        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("DEBUG", "fabStop onClick listener");
                onStopReiki();
            }
        });

        // Play/Pause
        fabPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Reiki is initiated with Bottom bar play button, start at position 0
                Log.wtf("DEBUG", "fabPlayPause onClick listener");
                onPlayOrPauseClick(currentPositionID);
            }
        });

        // Add
        setupAddButtonBasedOnProductFlavor(fabAdd);

        // Ad
        setupAdBasedOnProductFlavor();

        // Setup RecyclerView
        // Set up and subscribe (observe) to the ViewModel
        reikiCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ReikiCollectionViewModel.class);

        reikiCollectionViewModel.getPositions(reiki.getId()).observe(this, new Observer<List<Position>>() {
            @Override
            public void onChanged(@Nullable List<Position> positions) {
                if (PositionListActivity.this.listOfPositions == null) {
                    setPositions(positions);
                } else {
                    updatePositions(positions);
                }
            }
        });
    }

    private void setupAddButtonBasedOnProductFlavor(FloatingActionButton fabAdd){
        if(Constants.type == Constants.Type.PAID) {
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCreatePositionActivity(listOfPositions.size() + 1);
                }
            });
        }
        else {
            fabAdd.setVisibility(View.INVISIBLE);
        }
    }

    // Set up Ad based on product flavor
    private void setupAdBasedOnProductFlavor() {
        if(Constants.type == Constants.Type.PAID) {
            adLayout.setVisibility(View.GONE);
        }
        else {
            adLayout.setVisibility(View.VISIBLE);
            btnBuy = findViewById(R.id.btn_buy);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBuyClick();
                }
            });
        }
    }

    private void onBuyClick() {
        // Go to Full Version page on PlayStore
        Log.wtf("DEBUG", "Buy now clicked.");

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.learnteachcenter.ltcreikiclock.paid"));

        startActivity(intent);
    }

    private void showHideAddButtonBasedOnProductFlavor(FloatingActionButton fabAdd, int visibility) {
        if(Constants.type == Constants.Type.PAID) {
            fabAdd.setVisibility(visibility);
        }
        else {
            fabAdd.setVisibility(View.INVISIBLE);
        }
    }

    public void setPositions(List<Position> listOfPositions) {
        this.listOfPositions = listOfPositions;

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.divider
                )
        );

        recyclerView.addItemDecoration(itemDecoration);

        mItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        mItemTouchHelperCallback.setDraggable(true);

        setVisibilityPlayStopPrompt();
    }

    /**
     * Method to reload Positions into RecyclerView
     * @param newList A list of Positions
     */
    public void updatePositions(List<Position> newList) {
        listOfPositions.clear();
        listOfPositions.addAll(newList);
        adapter.notifyDataSetChanged();
        setVisibilityPlayStopPrompt();
    }

    // Play/Pause button is clicked.
    private void onPlayOrPauseClick(int position) {
        switch(currentAppMode){
            case APP_MODE_PLAYING:
                onPauseReiki();
                break;
            case APP_MODE_STOPPED:
                onPlayReiki(position);
                break;
            case APP_MODE_PAUSED:
            default:
                onResumeReiki();
        }
    }

    private void startReiki(){
        changeViewMode(APP_MODE_PLAYING);
        playBackgroundSound();
        startCurrentPosition(); // Start a new Position (timer), this method will be called every time a new Position starts
    }

    private void onResumeReiki(){
        startReiki();
    }

    /**
     * Method to Start a Reiki session
     * It's called when Play button is clicked.
     */
    private void onPlayReiki(int position) {
        currentPositionID = position;
        Position p = listOfPositions.get(currentPositionID);
        secondsRemaining = p.getDurationSeconds();

        startReiki();
    }

    // Pause button is clicked.
    private void onPauseReiki() {
        pauseCurrentPosition();
    }

    /**
     * Method to stop the current Reiki session.
     * Each of the following cases will call this method.
     * - Reiki session is done.
     * - User clicks Stop button.
     * - User browses to another activity.
     */
    private void onStopReiki(){
        stopCurrentPosition();
    }

    /**
     * Method to set up UI (buttons, dragging, info message) based on current mode of Reiki App.
     */
    private void changeViewMode(String mode) {

        currentAppMode = mode;

        switch (mode) {
            case APP_MODE_PLAYING:
                fabPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
                showHideAddButtonBasedOnProductFlavor(fabAdd, View.INVISIBLE);
                editMenuItem.setVisible(false);
                mItemTouchHelperCallback.setDraggable(false);
                showMessage("Reiki started.");
                break;
            case APP_MODE_PAUSED:
                fabPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                showMessage("Reiki paused.");
                break;
            case APP_MODE_STOPPED:
            default:
                fabPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                showHideAddButtonBasedOnProductFlavor(fabAdd, View.VISIBLE);
                editMenuItem.setVisible(true);
                mItemTouchHelperCallback.setDraggable(true);
                showMessage("Reiki stopped.");
                break;
        }
    }

    private void startCurrentPosition(){

        // Scroll to current Position
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

        if((currentPositionID > lastVisiblePosition && lastVisiblePosition != 0)
                || currentPositionID < firstVisiblePosition)
        {
            scrollToThenStartTimer(currentPositionID);
        }
        else {
            startTimer();
        }
    }

    private void pauseCurrentPosition(){
        stopTimer();
        pauseBackgroundSound();
        setPositionView(currentPositionID,
                        false,
                        -1,
                        ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_24dp),
                        null);
        changeViewMode(APP_MODE_PAUSED);
    }

    private void stopCurrentPosition(){
        if(!(currentAppMode.equals(APP_MODE_STOPPED))) {
            setPositionView(currentPositionID,      // Reset color & duration of last item in the list
                    true,
                    0,
                    ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_24dp),
                    listOfPositions.get(currentPositionID).getDuration());
            stopTimer();                            // Stop Timer
            stopBackgroundSound();                  // Stop background Music
            changeViewMode(APP_MODE_STOPPED);    // Change View Mode
            currentPositionID = 0;
        }
    }

    /**
     * Method to check and scroll to required Position in the RecyclerView
     * @param position Position to scroll to
     *
     */
    private void scrollToThenStartTimer(int position){
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, position);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public final void onScrolled(RecyclerView recyclerView, int dx, int dy){
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                boolean hasEnded = state == RecyclerView.SCROLL_STATE_IDLE;

                // Scrolling is finished.
                if(hasEnded) {
                    startTimer();
                    recyclerView.clearOnScrollListeners();
                }
            }
        });
    }

    private void resetPreviousViewState(int position){
        // Reset background color and image of previous Position item
        setPositionView(position,
                true,
                0,
                ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_24dp), null);
    }

    private void startTimer() {

        resetPreviousViewState(currentPositionID-1);

        setPositionView(currentPositionID,
                true,
                getResources().getColor(R.color.colorHighlight),
                ContextCompat.getDrawable(this, R.drawable.ic_pause_circle_outline_black_24dp), null);

        // start countdown
        countdownTimer = new CountDownTimer(secondsRemaining * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = millisUntilFinished / 1000;
                String durationRemaining = secondsToHHMMSS(secondsRemaining);
                setPositionView(currentPositionID, false, -1, null, durationRemaining);
            }

            @Override
            public void onFinish() {
                playReminderSound();
                Position p = listOfPositions.get(currentPositionID);
                setPositionView(currentPositionID, false, -1, null, p.getDuration());

                //if not at end of list
                if (currentPositionID < listOfPositions.size()-1) {

                    currentPositionID++;

                    p = listOfPositions.get(currentPositionID);
                    secondsRemaining = p.getDurationSeconds();

                    // start a new position (timer)
                    startCurrentPosition();

                } else {

                    // If background music is playing,
                    // At the end, countdown 4 more seconds to smoothly lower background music
                    if (reiki.getPlayMusic()) {

                        // Finishing countdown timer to fade music
                        new CountDownTimer(4000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                if(bgMusicPlayer != null) {
                                    float v = (float) secondsFinishing / 4;
                                    bgMusicPlayer.setVolume(v, v);
                                    secondsFinishing--;
                                }
                            }

                            @Override
                            public void onFinish() {
                                this.cancel();
                                onStopReiki();
                            }
                        }.start();
                    } else {
                        onStopReiki();
                    }
                }
            }
        }.start();
    }

    /**
     * Set View for each Position, which is a RecyclerView item
     * @param position  Position to set view
     * @param color     color to set
     * @param image     play/pause image to set
     * @param duration  duration text to set
     */
    private void setPositionView(int position, boolean changeColor, int color, Drawable image, String duration){

        // Getting a RecyclerView item, which is out of view, will throw NullPointerException.
        // So, use try/catch
        try {
            CustomAdapter.CustomViewHolder holder;
            holder = (CustomAdapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

            if(changeColor) {
                holder.itemView.setBackgroundColor(color);
            }

            if(image != null) {
                holder.playPause.setImageDrawable(image);
            }

            if(duration != null) {
                holder.positionDuration.setText(duration);
            }
        }
        catch(Exception ex){
            Log.wtf("DEBUG", "Exception in setPositionView: " + ex.toString());
        }
    }

    private void setVisibilityPlayStopPrompt(){
        if(listOfPositions.size() > 0) {
            fabPlayPause.setVisibility(View.VISIBLE);
            fabStop.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.VISIBLE);
            prompt.setVisibility(View.INVISIBLE);
        }
        else {
            fabPlayPause.setVisibility(View.INVISIBLE);
            fabStop.setVisibility(View.INVISIBLE);

            recyclerView.setVisibility(View.INVISIBLE);
            prompt.setVisibility(View.VISIBLE);
        }
    }

    public void confirmDelete(final Position tempPosition, final int tempViewHolderPosition) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage(getString(R.string.action_delete_item))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDeleteConfirmed(tempPosition);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        undoSwipeAction(tempPosition, tempViewHolderPosition);
                    }
                })
                .show();
    }

    private void undoSwipeAction(Position tempPosition, int tempViewHolderPosition) {
        listOfPositions.add(tempViewHolderPosition, tempPosition);
        adapter.notifyItemInserted(tempViewHolderPosition);
    }

    private void onDeleteConfirmed(Position tempPosition) {
        reikiCollectionViewModel.deletePosition( tempPosition );
        setVisibilityPlayStopPrompt();
        showMessage(getApplicationContext().getString(R.string.item_deleted));
    }

    /**
     * Method to cancel current countdownTimer
     */
    private void stopTimer() {
        if(countdownTimer != null) {
            countdownTimer.cancel();
        }
    }

    /**
     * Method to play Reminder Sound
     */
    private void playReminderSound() {
        MediaPlayer mpReminderSound = MediaPlayer.create(this.getApplicationContext(), R.raw.reminder_sound);
        mpReminderSound.start();
    }

    /**
     * Method to play Background Sound
     */
    private void playBackgroundSound() {
        if (reiki.getPlayMusic()) {
            try {
                if(bgMusicPlayer == null) {
                    bgMusicPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.background_sound);
                    bgMusicPlayer.setLooping(true);
                }

                bgMusicPlayer.start();
            }
            catch(Exception ex) {
                Log.wtf("DEBUG", "Exception in playBackgroundSound: " + ex.toString());
            }
        }
    }

    /**
     * Method to pause Background Sound
     */
    private void pauseBackgroundSound() {
        if(bgMusicPlayer != null) {
            bgMusicPlayer.pause();
        }
    }

    /**
     * Method to stop Background Sound
     */
    private void stopBackgroundSound() {
        if (bgMusicPlayer != null) {

            if(bgMusicPlayer.isPlaying()) {
                bgMusicPlayer.stop();
            }

            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }
    }

    /**
     * Method to show info message to the user as Toast
     * @param msg Info message
     */
    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to convert seconds into hh:mm:ss string
     * @param totalSec  Total seconds to convert to hh:mm:ss
     * @return          Returns time in hh:mm:ss format
     */
    private String secondsToHHMMSS(long totalSec) {

        long hours = (totalSec / 3600) % 24;
        long minutes = (totalSec / 60) % 60;
        long seconds = totalSec % 60;

        String hhMMSS = (minutes < 10 ? "0" + minutes : minutes) + ":"
                + (seconds < 10 ? "0" + seconds : seconds);

        if (hours > 0) {
            hhMMSS = (hours < 10 ? "0" + hours : hours) + ":"
                    + hhMMSS;
        }

        return hhMMSS;
    }

    /**
     * Method to start Create Position activity
     * @param positionSeqNo Position seq no
     */
    public void startCreatePositionActivity(int positionSeqNo) {

        Activity container = this;
        Intent i = new Intent(container, CreatePositionActivity.class);

        i.putExtra(IntentExtraNames.EXTRA_REIKI_ID, reiki.getId());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_TITLE, reiki.getTitle());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, reiki.getDescription());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, reiki.getPlayMusic());
        i.putExtra(IntentExtraNames.EXTRA_POSITION_SEQ_NO, positionSeqNo);

        startActivity(i);
    }

    /**
     * Method to edit a Position
     * @param positionID        ID
     * @param positionSeqNo     Seq No.
     * @param positionTitle     Title
     * @param positionDuration  Duration
     */
    public void startCreatePositionActivity(String positionID, int positionSeqNo,
                                            String positionTitle, String positionDuration) {

        Activity container = this;
        Intent i = new Intent(container, CreatePositionActivity.class);

        i.putExtra(IntentExtraNames.EXTRA_REIKI_ID, reiki.getId());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_TITLE, reiki.getTitle());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, reiki.getDescription());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, reiki.getPlayMusic());

        i.putExtra(IntentExtraNames.EXTRA_POSITION_ID, positionID);
        i.putExtra(IntentExtraNames.EXTRA_POSITION_SEQ_NO, positionSeqNo);
        i.putExtra(IntentExtraNames.EXTRA_POSITION_TITLE, positionTitle);
        i.putExtra(IntentExtraNames.EXTRA_POSITION_DURATION, positionDuration);

        startActivity(i);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Method to get Intent Extras
     */
    private void processIntentExtras() {
        Intent i = getIntent();

        if (i.hasExtra(IntentExtraNames.EXTRA_REIKI_ID)) {
            String reikiId = i.getStringExtra(IntentExtraNames.EXTRA_REIKI_ID);
            String reikiTitle = i.getStringExtra(IntentExtraNames.EXTRA_REIKI_TITLE);
            String reikiDescription = i.getStringExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION);
            boolean reikiPlayMusic = i.getBooleanExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, true);

            // TODO: update seqNo
            reiki = new Reiki(reikiId, 1, reikiTitle, reikiDescription, reikiPlayMusic);
        }
        else {
            finish();
        }
    }

    /**
     * Method to create menu on the right
     * @param menu Menu on the right
     * @return Result of menu creation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_position_list, menu);

        // Get edit Reiki info button
        editMenuItem = menu.getItem(0);

        return true;
    }

    /**
     * Method to handle actions on the right menu
     * @param item Menu item which was selected.
     * @return Result of selected menu item action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // User wants to edit reiki info
        if(id == R.id.action_edit_reiki_info) {
            // Prepare extras and start edit activity
            Intent i = new Intent(getApplicationContext(), CreateReikiActivity.class);
            i.putExtra(IntentExtraNames.NEW_REIKI_SEQNO, reiki.getSeqNo());
            i.putExtra(IntentExtraNames.EXTRA_REIKI_ID, reiki.getId());
            i.putExtra(IntentExtraNames.EXTRA_REIKI_TITLE, reiki.getTitle());
            i.putExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, reiki.getDescription());
            i.putExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, reiki.getPlayMusic());
            startActivity(i);
            onStopReiki();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to handle back button clicked.
     * Stop Reiki.
     */
    @Override
    public void onBackPressed() {
        onStopReiki();
        finish();
    }

    /*-------------------- RecyclerView Boilerplate ----------------------*/

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>
                                implements ItemTouchHelperAdapter {

        private final OnStartDragListener mDragStartListener;
        private boolean wasSwiped = false;

        private CustomAdapter(OnStartDragListener dragStartListener) {
            mDragStartListener = dragStartListener;
        }

        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_position, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CustomAdapter.CustomViewHolder holder, int position) {
            Position currentPosition = listOfPositions.get(position);

            // Bind to views
            String seqNoStr = String.format("%1$s.",currentPosition.getSeqNo());
            holder.positionSeqNo.setText(seqNoStr);
            holder.positionTitle.setText(currentPosition.getTitle());
            holder.positionDuration.setText(currentPosition.getDuration());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mDragStartListener.onStartDrag(holder);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return listOfPositions.size();
        }

        /*
        * onItemMove
        * */
        @Override
        public boolean onItemMove(int fromIndex, int toIndex) {
            Collections.swap(listOfPositions, fromIndex, toIndex);
            notifyItemMoved(fromIndex, toIndex);

            return true;
        }

        /*
        * onItemDismiss
        * */
        public void onItemDismiss(final int index) {
            // Confirm and Delete in DB
            Position rPosition = listOfPositions.get(index);

            listOfPositions.remove(rPosition);
            adapter.notifyItemRemoved(index);

            wasSwiped = true;

            confirmDelete(rPosition, index);
        }

        class CustomViewHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener, ItemTouchHelperViewHolder {

            //10. now that we've made our layouts, let's bind them
            private TextView positionSeqNo;
            private TextView positionTitle;
            private TextView positionDuration;
            private ViewGroup container;
            private ImageButton playPause;

            private int initialIndex;
            private int finalIndex;

            private CustomViewHolder(View itemView) {
                super(itemView);
                this.positionSeqNo = itemView.findViewById(R.id.lbl_position_seq_no);
                this.positionTitle = itemView.findViewById(R.id.lbl_position_title);
                this.positionDuration = itemView.findViewById(R.id.lbl_position_duration);
                this.playPause = itemView.findViewById(R.id.imb_position_item_play_pause);
                this.playPause.setOnClickListener(this);
                this.container = itemView.findViewById(R.id.root_position_item);
                /*
                We can pass "this" as an Argument, because "this", which refers to the Current
                Instance of type CustomViewHolder currently conforms to (implements) the
                View.OnClickListener interface. I have a Video on my channel which goes into
                Interfaces with Detailed Examples.

                Search "Android WTF: Java Interfaces by Example"
                 */
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int id = v.getId();

                if (id == R.id.root_position_item) {
                    // Start PositionActivity
                    Position position = listOfPositions.get(
                            this.getAdapterPosition()
                    );

                    // If Reiki is not running, proceed to Create/Edit Position
                    if(!currentAppMode.equals(APP_MODE_PLAYING)) {
                        startCreatePositionActivity(
                                position.getId(),
                                position.getSeqNo(),
                                position.getTitle(),
                                position.getDuration()
                        );
                    }
                } else if (id == R.id.imb_position_item_play_pause) {
                    //start at clicked item position
                    onPlayOrPauseClick(this.getAdapterPosition());
                }
            }

            @Override
            public void onItemSelected(int seqNo) {
                initialIndex = seqNo;
                itemView.setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onItemClear(int seqNo) {

                itemView.setBackgroundColor(0);

                // Check for drag or swipe and update DB accordingly.

                if(wasSwiped) {
                    wasSwiped = false;
                }
                else {
                    // Update only the affected rows
                    finalIndex = seqNo;
                    int fromIndex;
                    int toIndex;

                    if(initialIndex < finalIndex) {
                        fromIndex = initialIndex;
                        toIndex = finalIndex;
                    }
                    else {
                        fromIndex = finalIndex;
                        toIndex = initialIndex;
                    }

                    toIndex++;


                    // Update seqNo of affected Reiki items only
                    Position tempPosition;
                    for(int i = fromIndex; i < toIndex; i++) {
                        tempPosition = listOfPositions.get(i);
                        tempPosition.setSeqNo(i+1);
                    }

                    Position[] positionsToUpdate = Arrays.copyOfRange(listOfPositions.toArray(new Position[listOfPositions.size()]), fromIndex, toIndex);

                    // Update DB
                    reikiCollectionViewModel.updatePositions(positionsToUpdate);
                }
            }
        }
    }
}
