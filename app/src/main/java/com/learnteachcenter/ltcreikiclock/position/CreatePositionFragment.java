package com.learnteachcenter.ltcreikiclock.position;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ReikiApplication;
import com.learnteachcenter.ltcreikiclock.data.Position;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.util.IntentExtraNames;
import com.learnteachcenter.ltcreikiclock.viewmodel.NewReikiViewModel;

import javax.inject.Inject;


public class CreatePositionFragment extends Fragment {
    private Reiki mReiki;
    private Position mPosition;

    private ImageButton back;
    private ImageButton done;
    private EditText titleInput;
    private EditText durationInput;

    private boolean editMode = false;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private NewReikiViewModel newReikiViewModel;

    public CreatePositionFragment() { }

    public static CreatePositionFragment newInstance(String reikiId, String reikiTitle, String reikiDescription, boolean reikiPlayMusic,
                                                     String positionId, int seqNo, String positionTitle, String positionDuration ) {
        CreatePositionFragment fragment = new CreatePositionFragment();
        Bundle args = new Bundle();

        // Put Reiki info
        args.putString(IntentExtraNames.EXTRA_REIKI_ID, reikiId);
        args.putString(IntentExtraNames.EXTRA_REIKI_TITLE, reikiTitle);
        args.putString(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, reikiDescription);
        args.putBoolean(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, reikiPlayMusic);

        // Put Position info
        args.putString(IntentExtraNames.EXTRA_POSITION_ID, positionId);
        args.putInt(IntentExtraNames.EXTRA_POSITION_SEQ_NO, seqNo);
        args.putString(IntentExtraNames.EXTRA_POSITION_TITLE, positionTitle);
        args.putString(IntentExtraNames.EXTRA_POSITION_DURATION, positionDuration);

        fragment.setArguments(args);
        return fragment;
    }

    /*------------------------------- Lifecycle -------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((ReikiApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        Bundle args = getArguments();

        this.mReiki = new Reiki(args.getString(IntentExtraNames.EXTRA_REIKI_ID),
                                args.getString(IntentExtraNames.EXTRA_REIKI_TITLE),
                                args.getString(IntentExtraNames.EXTRA_REIKI_DESCRIPTION),
                                args.getBoolean(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC));

        this.mPosition = new Position(mReiki.getId(),
                args.getString(IntentExtraNames.EXTRA_POSITION_ID),
                args.getInt(IntentExtraNames.EXTRA_POSITION_SEQ_NO),
                args.getString(IntentExtraNames.EXTRA_POSITION_TITLE),
                args.getString(IntentExtraNames.EXTRA_POSITION_DURATION));
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newReikiViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewReikiViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_position, container, false);

        // Back button
        back = v.findViewById(R.id.imb_create_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startPositionListActivity();
            }
        });

        titleInput = v.findViewById(R.id.edt_position_title);
        durationInput = v.findViewById(R.id.edt_position_duration);

        // Set fragment title
        TextView titleFragment = v.findViewById(R.id.lbl_position_toolbar_title);

        if(mPosition.getTitle() != null && !mPosition.getTitle().equals("")) {
            titleInput.setText(mPosition.getTitle());
            titleFragment.setText(R.string.position_edit);
            editMode = true;
        }
        else {
            titleFragment.setText(R.string.position_create);
        }

        if(mPosition.getDuration() != null && !mPosition.getDuration().equals("")) {
            durationInput.setText(mPosition.getDuration());
        }

        done = v.findViewById(R.id.imb_create_done);
        done.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String lvTitle = titleInput.getText().toString();
                String lvDuration = durationInput.getText().toString();

                if(lvTitle.isEmpty()){
                    titleInput.requestFocus();
                    titleInput.setError("Fill");
                    return;
                }

                if(lvDuration.isEmpty()) {
                    durationInput.requestFocus();
                    durationInput.setError("Fill");
                    return;
                }

                Position position = new Position(
                        mReiki.getId(),
                        mPosition.getSeqNo(),
                        lvTitle,
                        lvDuration
                );

                if(editMode) {
                    position.setId(mPosition.getId());
                    newReikiViewModel.updatePositionInDatabase(position);
                }
                else {
                    newReikiViewModel.addPositionToDatabase(position);
                }

                startPositionListActivity();
            }
        });

        return v;
    }

    private void startPositionListActivity() {
        getActivity().finish();

        Intent i = new Intent(getActivity(), PositionListActivity.class);
        i.putExtra(IntentExtraNames.EXTRA_REIKI_ID, mReiki.getId());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_TITLE, mReiki.getTitle());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, mReiki.getDescription());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, mReiki.getPlayMusic());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
