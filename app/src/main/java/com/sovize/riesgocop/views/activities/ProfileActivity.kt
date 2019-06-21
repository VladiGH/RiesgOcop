package com.sovize.riesgocop.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        findViewById<Button>(R.id.logout).setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this, Login::class.java))
            } else {
                GoogleSignIn.getClient(
                    this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build()
                ).signOut()
                FirebaseAuth.getInstance()?.signOut()
                finish()
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser == null) {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_in)
            } else {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_out)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener {
        }
    }
}
