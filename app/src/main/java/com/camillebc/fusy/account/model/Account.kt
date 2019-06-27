package com.camillebc.fusy.account.model

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.camillebc.fusy.APP_TAG
import com.camillebc.fusy.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

private const val TAG = APP_TAG + "Account"

object Account {
    private var name: String? = null
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
        .requestScopes(Scope(Scopes.DRIVE_FILE))
        .build()
    private var googleAccount: GoogleSignInAccount? = null

    fun googleSignIn(activity: Activity) {
        val signInIntent = GoogleSignIn.getClient(activity, gso).signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // getters
    fun getGoogleAccount(): GoogleSignInAccount? = googleAccount

    fun getName(): String? = name

    // setters
    fun setGoogleAccount(intent: Intent) {
        try {
            googleAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).getResult(ApiException::class.java)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult: failed code = " + e.statusCode)
        }
        name = googleAccount?.displayName
    }
}