package com.learnteachcenter.ltcreikiclock.authentication

import com.google.android.gms.tasks.Task

interface FirebaseAuthenticationInterface {

    fun signInWithGoogle(idToken: String, onResult: (Boolean) -> Unit)

    fun isSignedIn(): Boolean

    fun getUserId(): String

    fun getUserName(): String

    fun getUserEmail(): String

    fun logOut(onResult: () -> Unit)
}