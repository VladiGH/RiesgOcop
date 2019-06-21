package com.sovize.riesgocop.views.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.firebase.MasterCrud
import com.sovize.riesgocop.models.User
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.Document
import com.sovize.riesgocop.utilities.ResponseCodes

class Login : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mGoogleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
        findViewById<SignInButton>(R.id.btn_login)?.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, ResponseCodes.login)
        }
        findViewById<TextView>(R.id.policy).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://files.sovize.com/policy/Politicasdeapp.html"))
            startActivity(intent)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ResponseCodes.login) {
            data?.apply {
                fireBaseAuthWithGoogle(handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(this)))
            }
        } else {
            Snackbar.make(findViewById(R.id.btn_login), getString(R.string.loginmsm1), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?): GoogleSignInAccount? =
        try {
            task?.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            Log.e(AppLogger.login, e.statusCode.toString(), e)
            null
        }

    private fun fireBaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        Log.d(AppLogger.login, "${credential.signInMethod} ${credential.provider}")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@Login) { task ->
                if (task.isSuccessful) {
                    if (task.result?.additionalUserInfo?.isNewUser == true) {
                        val newUser = User(task.result?.user?.email ?: "error", 5, charArrayOf('x'))
                        MasterCrud().insert(Document.users, newUser) {
                            Log.d(AppLogger.login, "Si se pudo meter nuevo user?: $it")
                        }
                    }
                    takeAction()
                } else {
                    Snackbar.make(
                        findViewById(R.id.btn_login),
                        "${getString(R.string.loginmsm2)} ${task.exception?.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun takeAction() {
        auth.currentUser.apply {
            val mMail = this?.email?.split('@')
            Log.d(AppLogger.login, mMail?.get(1))
            if (mMail?.get(1) != "uca.edu.sv") {
                AlertDialog.Builder(findViewById<View>(R.id.btn_login).context)
                    .setTitle(getString(R.string.alerttitle))
                    .setMessage(getString(R.string.alertmsm))
                    .setPositiveButton(getString(R.string.continuex)) { dialog, _ ->
                        dialog.dismiss()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }.setNegativeButton(getString(R.string.changeacct)) { dialog, _ ->
                        dialog.dismiss()
                        mGoogleSignInClient.signOut()
                        auth.signOut()
                        val signInIntent = mGoogleSignInClient.signInIntent
                        startActivityForResult(signInIntent, ResponseCodes.login)
                    }.create().show()
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

    }
}
