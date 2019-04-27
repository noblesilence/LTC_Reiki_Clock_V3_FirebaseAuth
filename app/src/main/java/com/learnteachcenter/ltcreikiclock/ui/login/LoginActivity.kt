package com.learnteachcenter.ltcreikiclock.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.learnteachcenter.ltcreikiclock.R
import com.learnteachcenter.ltcreikiclock.authentication.FirebaseAuthenticationInterface
import androidx.appcompat.app.AppCompatActivity
import com.learnteachcenter.ltcreikiclock.application.ReikiApplication
import javax.inject.Inject

/**
 * A login screen that offers login with Google
 */
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuthenticationInterface

    internal var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (application as ReikiApplication).appComponent.inject(this)

        val googleButton = findViewById<View>(R.id.google_button) as SignInButton
        googleButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {        
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuth!!.createGoogleUser(account!!.idToken!!) { success ->
                    if (success) {
                        Log.d(TAG, "Successfully created user")
                    } else {
                        Log.d(TAG, "Failed to create user")
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }

        }
    }

    companion object {
        private val RC_SIGN_IN = 123
        private val TAG = "Reiki"
    }
}

