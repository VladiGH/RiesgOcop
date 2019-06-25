package com.sovize.ultracop.views.activities

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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.models.User
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.Document
import com.sovize.ultracop.utilities.ResponseCodes
import com.sovize.ultracop.utilities.system.Permissions

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
                        val newUser = if (task.result?.user?.email?.split('@')?.get(1) == "uca.edu.sv") {
                            User(task.result?.user?.email!!, 2, Permissions.userUca)
                        } else {
                            User(task.result?.user?.email ?: "error", 1, Permissions.user)
                        }
                        newUser.firebaseUser = task.result?.user
                        MasterCrud().insertWithUid(Document.users, newUser) {
                            takeAction()
                        }
                    } else {
                        takeAction()
                    }
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
            updateToken()
        }

    }

    private fun updateToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(AppLogger.login, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                token?.let {
                    Log.d(AppLogger.login, "The generated token is $it")
                    FirebaseAuth.getInstance().currentUser?.apply {
                        FirebaseFirestore.getInstance().collection(Document.users)
                            .document(this.uid).update("token", it)
                    }
                }
            })
    }
}
