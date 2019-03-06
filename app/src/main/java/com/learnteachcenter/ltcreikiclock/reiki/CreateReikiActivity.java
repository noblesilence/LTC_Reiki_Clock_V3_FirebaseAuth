package com.learnteachcenter.ltcreikiclock.reiki;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.learnteachcenter.ltcreikiclock.R;
import com.learnteachcenter.ltcreikiclock.util.BaseActivity;
import com.learnteachcenter.ltcreikiclock.util.IntentExtraNames;

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

        fragment.setSeqNo(i.getIntExtra(IntentExtraNames.NEW_REIKI_SEQNO, 0));

        // If user is editing Reiki info, extras will be passed.
        if(i.hasExtra(IntentExtraNames.EXTRA_REIKI_ID)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_TITLE)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_DESCRIPTION)
                && i.hasExtra(IntentExtraNames.EXTRA_REIKI_PLAY_MUSIC)) {

            fragment.setReikiInfo(i.getStringExtra(IntentExtraNames.EXTRA_REIKI_ID),
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
