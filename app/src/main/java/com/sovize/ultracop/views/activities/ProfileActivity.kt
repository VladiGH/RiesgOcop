package com.sovize.ultracop.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.network.Glider

class ProfileActivity : AppCompatActivity() {

    private val user = MutableLiveData<FirebaseUser>()

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
            user.value = it.currentUser
        }
        user.observe(this, Observer<FirebaseUser> { fire ->
            if (fire == null) {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_in)

            } else {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_out)
                Glider.load(
                    fire.photoUrl.toString(),
                    findViewById(R.id.user_profile_photo),
                    R.drawable.profile
                )
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener {
        }
    }
}
