package com.learnteachcenter.ltcreikiclock.ui.reiki;


import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ReikiApplication;
import com.learnteachcenter.ltcreikiclock.auth.LoginActivity;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.ui.helper.ItemTouchHelperAdapter;
import com.learnteachcenter.ltcreikiclock.ui.helper.ItemTouchHelperViewHolder;
import com.learnteachcenter.ltcreikiclock.ui.helper.OnStartDragListener;
import com.learnteachcenter.ltcreikiclock.ui.helper.SimpleItemTouchHelperCallback;
import com.learnteachcenter.ltcreikiclock.ui.position.PositionListActivity;
import com.learnteachcenter.ltcreikiclock.utils.IntentExtraNames;
import com.learnteachcenter.ltcreikiclock.viewmodel.ReikiCollectionViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReikiListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReikiListFragment extends Fragment implements OnStartDragListener {

    private static final String TAG = "Reiki";

    /*
    In order to establish seqNos based on position in Database, new Reikis are created with a
    seqNo equivalent to listOfReikis.size() + 1. We could perhaps also have the repository count
    and return the number of reikis in the Database; but the result should be the same in theory.
     */

    private List<Reiki> listOfReikis;

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CustomAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    SimpleItemTouchHelperCallback mItemTouchHelperCallback;
//    private AdView mAdView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ReikiCollectionViewModel reikiCollectionViewModel;

    public ReikiListFragment() {
    }

    public static ReikiListFragment newInstance() {
        return new ReikiListFragment();
    }

    /*------------------------------- Lifecycle -------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ReikiApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: ");

        // Set up and subscribe (observe) to the ViewModel
        reikiCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ReikiCollectionViewModel.class);

        progressBar.setVisibility(View.VISIBLE);
        loadData();

        reikiCollectionViewModel.reikisResult().observe(this, new Observer<List<Reiki>>() {
            @Override
            public void onChanged(@Nullable List<Reiki> reikis) {
                Log.d(TAG, "reikis: " + reikis);
                //if list is empty, repopulate Database
                if (reikis.size() < 1){
                    reikiCollectionViewModel.populateReikiDatabase();
                }
                if (ReikiListFragment.this.listOfReikis == null) {
                    setReikis(reikis);
                } else {
                    updateReikis(reikis);
                }
            }
        });

        reikiCollectionViewModel.reikisError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s != null) {
                    Log.d(TAG, "Error getting reikis. " + s);
//                    Toast.makeText(this, "There is an error getting the data: " + s,
//                            Toast.LENGTH_LONG).show();
                }
            }
        });

        reikiCollectionViewModel.reikisLoader().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(!aBoolean) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Reiki loading finished.");
                }
            }
        });
    }

    private void loadData(){
        reikiCollectionViewModel.loadReikis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_reiki_list, container, false);

        Toolbar toolbar = v.findViewById(R.id.tlb_reiki_list_activity);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = v.findViewById(R.id.rec_reiki_list_activity);
        progressBar = v.findViewById(R.id.progressBar);
        layoutInflater = getActivity().getLayoutInflater();

        FloatingActionButton fabAdd = v.findViewById(R.id.fab_create_new_reiki);
        setupAddButtonBasedOnProductFlavor(fabAdd);

        Button btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on log out click");
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

//        AdView adView = v.findViewById(R.id.adView);
//        setupAdBasedOnProductFlavor(adView);

        return v;
    }

    private void setupAddButtonBasedOnProductFlavor(FloatingActionButton fab){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateReikiActivity();
            }
        });
    }

//    private void setupAdBasedOnProductFlavor(AdView adView) {
//        if(Constants.type == Constants.Type.PAID) {
//            adView.setVisibility(View.GONE);
//        }
//        else {
//            MobileAds.initialize(getContext(), "ca-app-pub-1046951996868755~3901934187");
//            adView.setVisibility(View.VISIBLE);
//
//            AdRequest adRequest = new AdRequest.Builder().build();
//            adView.loadAd(adRequest);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Start PositionListActivity based on the given Reiki ID
    public void startPositionListActivity(Reiki reiki, View viewRoot) {
        Activity container = getActivity();
        Intent i = new Intent(container, PositionListActivity.class);
        i.putExtra(IntentExtraNames.EXTRA_REIKI_ID, reiki.getId());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_TITLE, reiki.getTitle());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, reiki.getDescription());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, reiki.getPlayMusic());

        startActivity(i);
    }

    // start Create Reiki Activity
    public void startCreateReikiActivity() {
        Intent i = new Intent(getActivity(), CreateReikiActivity.class);
        i.putExtra(IntentExtraNames.NEW_REIKI_SEQNO, listOfReikis.size() + 1);
        startActivity(i);
    }

    public void updateReikis(List<Reiki> newList) {
        listOfReikis.clear();
        listOfReikis.addAll(newList);

        adapter.notifyDataSetChanged();
    }

    public void setReikis(List<Reiki> listOfReikis) {

        this.listOfReikis = listOfReikis;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        R.drawable.divider
                )
        );

        recyclerView.addItemDecoration(
                itemDecoration
        );

        mItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        mItemTouchHelperCallback.setDraggable(true);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void confirmDelete(final Reiki tempReiki, final int tempViewHolderPosition) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Delete")
                .setMessage(getString(R.string.action_delete_item))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDeleteConfirmed(tempReiki);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                        undoSwipeAction(tempReiki, tempViewHolderPosition);
                    }})
                .show();
    }

    private void undoSwipeAction(Reiki tempReiki, int tempViewHolderPosition) {
        listOfReikis.add(tempViewHolderPosition, tempReiki);
        adapter.notifyItemInserted(tempViewHolderPosition);
    }

    private void onDeleteConfirmed(Reiki tempReiki) {
        reikiCollectionViewModel.deleteReiki(
                tempReiki
        );

        Toast.makeText(getContext(), R.string.item_deleted, Toast.LENGTH_SHORT).show();
    }

    /*-------------------- RecyclerView Boilerplate ----------------------*/

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>
            implements ItemTouchHelperAdapter {

        private final OnStartDragListener mDragStartListener;
        private boolean wasSwiped = false;

        private CustomAdapter(OnStartDragListener dragStartListener){
            mDragStartListener = dragStartListener;
        }

        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_reiki, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            Reiki currentReikiItem = listOfReikis.get(position);

            // Bind to views
            holder.reikiTitle.setText(
                    currentReikiItem.getTitle()
            );

            holder.reikiDescription.setText(
                    currentReikiItem.getDescription()
            );

            holder.loading.setVisibility(View.INVISIBLE);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {

                    mDragStartListener.onStartDrag(holder);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return listOfReikis.size();
        }

        /*
        * onItemMove
        * */
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(listOfReikis, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        /*
        * onItemDismiss
        * */
        public void onItemDismiss(final int position) {
            // Confirm and Delete in DB
            Reiki reiki = listOfReikis.get(position);

            listOfReikis.remove(reiki);
            adapter.notifyItemRemoved(position);

            wasSwiped = true;

            confirmDelete(reiki, position);
        }

        class CustomViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener, ItemTouchHelperViewHolder {

            //10. now that we've made our layouts, let's bind them
            private TextView reikiTitle;
            private TextView reikiDescription;
            private ViewGroup container;
            private ProgressBar loading;

            private int initialPosition;
            private int finalPosition;

            private CustomViewHolder(View itemView) {
                super(itemView);

                this.reikiTitle = itemView.findViewById(R.id.lbl_reiki_title);
                this.reikiDescription = itemView.findViewById(R.id.lbl_reiki_description);
                this.loading = itemView.findViewById(R.id.pro_item_reiki);

                this.container = itemView.findViewById(R.id.root_reiki_item);
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
                //getAdapterPosition() get's an Integer based on which the position of the current
                //ViewHolder (this) in the Adapter. This is how we get the correct Data.
                Reiki reiki = listOfReikis.get(
                        this.getAdapterPosition()
                );

                // Start PositionListActivity
                startPositionListActivity(reiki, v);
            }

            @Override
            public void onItemSelected(int seqNo) {
                initialPosition = seqNo;
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
                    finalPosition = seqNo;
                    int fromIndex;
                    int toIndex;

                    if(initialPosition < finalPosition) {
                        fromIndex = initialPosition;
                        toIndex = finalPosition;
                    }
                    else {
                        fromIndex = finalPosition;
                        toIndex = initialPosition;
                    }

                    toIndex++;


                    // Update seqNo of affected Reiki items only
                    Reiki tempReiki;
                    for(int i = fromIndex; i < toIndex; i++) {
                        tempReiki = listOfReikis.get(i);
                        tempReiki.setSeqNo(i);
                    }

                    Reiki[] reikisToUpdate = Arrays.copyOfRange(listOfReikis.toArray(new Reiki[listOfReikis.size()]), fromIndex, toIndex);

                    // Update DB
                    reikiCollectionViewModel.updateReikis(reikisToUpdate);
                }
            }
        }
    }
}
