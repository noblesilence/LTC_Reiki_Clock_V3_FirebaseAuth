package com.learnteachcenter.ltcreikiclock.authentication

interface FirebaseAuthenticationInterface {

    fun createGoogleUser(idToken: String, onResult: (Boolean) -> Unit)

    fun getIdToken(): String?

    fun getUserId(): String

    fun getUserName(): String

    fun getUserEmail(): String

    fun logOut(onResult: () -> Unit)
}