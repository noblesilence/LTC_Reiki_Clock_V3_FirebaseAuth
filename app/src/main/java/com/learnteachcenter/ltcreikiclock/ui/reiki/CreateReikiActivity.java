package com.learnteachcenter.ltcreikiclock.ui.reiki;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.ui.base.BaseActivity;
import com.learnteachcenter.ltcreikiclock.utils.IntentExtraNames;

import androidx.fragment.app.FragmentManager;

public class CreateReikiActivity extends BaseActivity {

    private static final String CREATE_REIKI_FRAG = "CREATE_REIKI_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reiki);

        Intent i = getIntent();

        //if seqNo isn't found, we can't create a Reiki with appropriate position in list :(
        if (!i.hasExtra(IntentExtraNames.NEW_REIKI_SEQNO)){
            Toast.makeText(this, R.string.error_no_extra_found, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, ReikiListActivity.class));
        }

        FragmentManager manager = getSupportFragmentManager();

        CreateReikiFragment fragment = (CreateReikiFragment) manager.findFragmentByTag(CREATE_REIKI_FRAG);

        if (fragment == null) {
            fragment = CreateReikiFragment.newInstance();
        }

        int reikiSeqNo = 0;

        fragment.setSeqNo(i.getIntExtra(IntentExtraNames.NEW_REIKI_SEQNO, reikiSeqNo));

        // If user is editing Reiki info, extras will be passed.
        if(i.hasExtra(IntentExtraNames.EXTRA_REIKI_ID)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_SEQ_NO)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_TITLE)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC)) {

            fragment.setReikiInfo(i.getStringExtra(IntentExtraNames.EXTRA_REIKI_ID),
                                    i.getIntExtra(IntentExtraNames.NEW_REIKI_SEQNO, reikiSeqNo),
                                    i.getStringExtra(IntentExtraNames.EXTRA_REIKI_TITLE),
                                    i.getStringExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION),
                    i.getBooleanExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC, true));
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.root_activity_create_reiki,
                CREATE_REIKI_FRAG
        );
    }
}
