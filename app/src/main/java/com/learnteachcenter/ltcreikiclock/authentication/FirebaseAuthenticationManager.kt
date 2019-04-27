package com.learnteachcenter.ltcreikiclock.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GetTokenResult

class FirebaseAuthenticationManager @Inject constructor(private val authenication: FirebaseAuth) : FirebaseAuthenticationInterface {
    override fun createGoogleUser(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        authenication.signInWithCredential(credential)
        .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
            override fun onComplete(@NonNull task: Task<AuthResult>) {
                if (task.isSuccessful()) {
                    val result = task.result
                    onResult(true)
                } else {
                    val exception = task.exception
                    onResult(false)
                }
            }
        })
    }

    override fun getIdToken(): String? {
        var idToken: String? = null

        if(authenication.currentUser != null) {
            authenication.currentUser!!.getIdToken(true)
                    .addOnCompleteListener(OnCompleteListener<GetTokenResult> { task ->
                        if(task.isSuccessful) {
                            idToken = task.result!!.token
                        }
                    })
        }

        return idToken
    }

    override fun getUserId(): String = authenication.currentUser?.uid ?: ""
    override fun getUserName(): String = authenication.currentUser?.displayName ?: ""
    override fun getUserEmail(): String = authenication.currentUser?.email ?: ""

    override fun logOut(onResult: () -> Unit) {
        authenication.signOut()

        onResult()
    }
}