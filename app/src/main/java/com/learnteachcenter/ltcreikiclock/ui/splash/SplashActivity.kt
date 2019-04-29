package com.learnteachcenter.ltcreikiclock.ui.splash

import android.content.Intent
import android.os.Bundle

import com.learnteachcenter.ltcreikiclock.R
import com.learnteachcenter.ltcreikiclock.application.ReikiApplication
import com.learnteachcenter.ltcreikiclock.ui.login.LoginActivity
import com.learnteachcenter.ltcreikiclock.authentication.FirebaseAuthenticationInterface
import com.learnteachcenter.ltcreikiclock.ui.base.BaseActivity
import com.learnteachcenter.ltcreikiclock.ui.reiki.ReikiListActivity
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var authentication: FirebaseAuthenticationInterface;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        (application as ReikiApplication).appComponent.inject(this)

        val i: Intent;

        if (authentication.isSignedIn()) {
            i = Intent(applicationContext, ReikiListActivity::class.java)
        } else {
            i = Intent(applicationContext, LoginActivity::class.java)
        }

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    companion object {
        private val TAG = "Reiki"
    }
}
