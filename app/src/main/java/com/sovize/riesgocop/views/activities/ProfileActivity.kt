package com.sovize.riesgocop.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.network.Glider
import com.sovize.riesgocop.models.User
import com.sovize.riesgocop.utilities.AppKey
import com.sovize.riesgocop.utilities.ServerInfo
import com.sovize.riesgocop.utilities.system.Permissions.user

class ProfileActivity : AppCompatActivity() {

    private val cUser: User? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        //val profileInfo = intent?.extras?.getParcelable<User>(AppKey.activity)
        //val profile = User(profileInfo!!.email,profileInfo.rol,profileInfo.permission,profileInfo.picture)

        //bindData(findViewById(R.layout.profile),profile)
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

                Glider.load(
                    cUser?.picture.toString(),
                    findViewById(R.id.user_profile_photo),
                    R.drawable.profile)
                    findViewById<TextView>(R.id.user_profile_name).text = cUser?.email.toString()
            }
        }


    }
/*
    fun bindData(view: View, profile: User){

        val nickname = "${profile.email}"

        view.findViewById<TextView>(R.id.user_profile_name).text = nickname
        if(profile.picture.isNotEmpty()){
            Glider.load("${ServerInfo.baseURL}${profile.picture}",
                findViewById(R.id.user_profile_photo))
        }else{
            Snackbar.make(findViewById(R.id.user_profile_photo),
                resources.getString(R.string.noPics), Snackbar.LENGTH_LONG).show()
            Glide.with(this@ProfileActivity)
                .load(R.drawable.ic_broken_image_black_48dp)
                .into(findViewById(R.id.user_profile_photo))
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener {
        }
    }
}
