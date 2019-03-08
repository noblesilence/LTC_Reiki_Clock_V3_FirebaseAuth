package com.learnteachcenter.ltcreikiclock.ui.position;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ui.BaseActivity;
import com.learnteachcenter.ltcreikiclock.utils.IntentExtraNames;

public class CreatePositionActivity extends BaseActivity {

    private static final String CREATE_POSITION_FRAG = "CREATE_POSITION_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_position);

        Intent i = getIntent();

        if(i.hasExtra(IntentExtraNames.EXTRA_REIKI_ID)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_SEQ_NO)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_ID)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_TITLE)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC)
                && i.hasExtra(IntentExtraNames.EXTRA_POSITION_SEQ_NO)) {

            String reikiId = i.getStringExtra(IntentExtraNames.EXTRA_REIKI_ID);
            int reikiSeqNo = i.getIntExtra(IntentExtraNames.EXTRA_REIKI_SEQ_NO, 0);
            String reikiTitle = i.getStringExtra(IntentExtraNames.EXTRA_REIKI_TITLE);
            String reikiDescription = i.getStringExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION);
            boolean reikiPlayMusic = i.getBooleanExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, true);

            String positionId = i.getStringExtra(IntentExtraNames.EXTRA_POSITION_ID);
            int positionSeqNo = i.getIntExtra(IntentExtraNames.EXTRA_POSITION_SEQ_NO, 1);
            String positionTitle = i.getStringExtra(IntentExtraNames.EXTRA_POSITION_TITLE);
            String positionDuration = i.getStringExtra(IntentExtraNames.EXTRA_POSITION_DURATION);

            FragmentManager manager = getSupportFragmentManager();

            CreatePositionFragment fragment = (CreatePositionFragment) manager.findFragmentByTag(CREATE_POSITION_FRAG);

            if (fragment == null) {
                fragment = CreatePositionFragment.newInstance(reikiId,
                        reikiSeqNo,
                        reikiTitle,
                        reikiDescription,
                        reikiPlayMusic,
                        positionId,
                        positionSeqNo,
                        positionTitle,
                        positionDuration);
            }

            addFragmentToActivity(manager,
                    fragment,
                    R.id.root_activity_create_position,
                    CREATE_POSITION_FRAG
            );
        }
    }
}
