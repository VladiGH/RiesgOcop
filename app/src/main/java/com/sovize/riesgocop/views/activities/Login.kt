package com.sovize.riesgocop.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.sovize.riesgocop.MainActivity
import com.sovize.riesgocop.R
import com.sovize.riesgocop.utilities.ResponseCodes

class Login : AppCompatActivity() {

    private val tag = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
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
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ResponseCodes.login) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            fireBaseAuthWithGoogle(handleSignInResult(task))
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?): GoogleSignInAccount? =
        try {
            task?.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            Log.e(tag, e.statusCode.toString())
            e.stackTrace.forEach {
                Log.d(tag, it.toString())
            }
            null
        }

    private fun fireBaseAuthWithGoogle(acct: GoogleSignInAccount?)  {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        Log.d(tag, "${credential.signInMethod} ${credential.provider}")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@Login) { task ->
                if(task.isSuccessful){
                    startActivity(Intent(this@Login, MainActivity::class.java))
                } else {
                    Snackbar.make(
                        findViewById(R.id.btn_login),
                        "Error al iniciar sesion ${task.exception} ${task.result}",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
            }
    }
}
