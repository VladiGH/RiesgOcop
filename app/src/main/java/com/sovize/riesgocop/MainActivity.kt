package com.sovize.riesgocop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import android.content.Intent
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

    private val signInCode = 2
    private val tag = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        mGoogleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )
        findViewById<SignInButton>(R.id.btn_login)?.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, signInCode)
        }
    }

    public override fun onStart() {
        super.onStart()
        val account = auth.currentUser
        if (account == null) {
            findViewById<TextView>(R.id.textView).text = "Usuario nullo"
        } else {
            findViewById<TextView>(R.id.textView).text = account.displayName
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == signInCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {
            findViewById<TextView>(R.id.textView).text = "signInResult: failed code = ${e.statusCode}"
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d(tag, "firebaseAuthWithGoogle:" + acct?.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "signInWithCredential:success")
                    val user = auth.currentUser
                    findViewById<TextView>(R.id.textView).text = user?.displayName
                } else {
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                    Snackbar.make(findViewById<TextView>(R.id.textView), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
}
