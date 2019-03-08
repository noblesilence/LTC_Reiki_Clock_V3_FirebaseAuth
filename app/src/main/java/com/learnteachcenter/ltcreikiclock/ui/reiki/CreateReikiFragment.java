package com.learnteachcenter.ltcreikiclock.ui.reiki;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ReikiApplication;
import com.learnteachcenter.ltcreikiclock.data.Reiki;
import com.learnteachcenter.ltcreikiclock.ui.position.PositionListActivity;
import com.learnteachcenter.ltcreikiclock.utils.IntentExtraNames;
import com.learnteachcenter.ltcreikiclock.viewmodel.NewReikiViewModel;

import java.util.UUID;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class CreateReikiFragment extends Fragment {

    private ImageButton back;
    private ImageButton done;
    private EditText titleInput;
    private EditText descriptionInput;
    private CheckBox playMusicCheckbox;
    private int seqNo;

    private Reiki mReiki;

    private boolean editMode = false;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private NewReikiViewModel newReikiViewModel;

    public CreateReikiFragment() {
        // Required empty public constructor
    }

    public void setSeqNo(int seqNo){
        this.seqNo = seqNo;
    }

    public void setReikiInfo(String id, int seqNo, String title, String description, boolean playMusic) {
        this.mReiki = new Reiki(id, seqNo, title, description, playMusic);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((ReikiApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    public static CreateReikiFragment newInstance() { return new CreateReikiFragment(); }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newReikiViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewReikiViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_reiki, container, false);

        // Back button
        back = v.findViewById(R.id.imb_create_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().finish(); // go back to previous activity
            }
        });

        titleInput = v.findViewById(R.id.edt_reiki_title);
        descriptionInput = v.findViewById(R.id.edt_reiki_description);
        playMusicCheckbox = v.findViewById(R.id.ckb_reiki_play_music);

        if(mReiki != null) {

            if(mReiki.getTitle() != null){
                titleInput.setText(mReiki.getTitle());

                // Change fragment title to Edit Reiki
                TextView titleFragment = v.findViewById(R.id.lbl_reiki_toolbar_title);
                titleFragment.setText(R.string.reiki_edit);

                editMode = true;
            }

            if(mReiki.getDescription() != null) {
                descriptionInput.setText(mReiki.getDescription());
            }

            playMusicCheckbox.setChecked(mReiki.getPlayMusic());
        }

        // Done button
        done = v.findViewById(R.id.imb_create_done);
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();

                // Check if Title is empty.
                if (title.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter title", Toast.LENGTH_SHORT).show();

                } else if (description.isEmpty()) { // Check if Description is empty.
                    Toast.makeText(getActivity(), "Enter description", Toast.LENGTH_SHORT).show();

                } else {
                    Reiki reiki = new Reiki(
                            UUID.randomUUID().toString(),
                            seqNo,
                            titleInput.getText().toString(),
                            descriptionInput.getText().toString(),
                            playMusicCheckbox.isChecked()
                    );

                    if(editMode) {
                        reiki.setId(mReiki.getId());
                        newReikiViewModel.updateReikiInDatabase(reiki);
                    }
                    else {
                        newReikiViewModel.addReikiToDatabase(reiki);
                    }

                    startPositionListActivity(reiki);
                }
            }
        });

        return v;
    }

    private void startPositionListActivity(Reiki reiki) {
        getActivity().finish();

        Intent i = new Intent(getActivity(), PositionListActivity.class);
        i.putExtra(IntentExtraNames.EXTRA_REIKI_ID, reiki.getId());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_TITLE, reiki.getTitle());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION, reiki.getDescription());
        i.putExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, reiki.getPlayMusic());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
