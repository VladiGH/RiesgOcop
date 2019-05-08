package com.sovize.riesgocop

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.activities.Login
import com.sovize.riesgocop.network.Glider


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) {
            startActivity(Intent(this@MainActivity, Login::class.java))
        }
        setContentView(R.layout.activity_main)
        setUserData()
        findViewById<Button>(R.id.btn_log_out).setOnClickListener{
            FirebaseAuth.getInstance().signOut()
        }
    }

    private fun setUserData(){
        findViewById<TextView>(R.id.tv_user_name).text = user?.displayName?:getString(R.string.user_not_found)
        findViewById<TextView>(R.id.tv_user_email).text = user?.email?:getString(R.string.user_not_found)
        findViewById<TextView>(R.id.tv_user_key).text = user?.uid?:getString(R.string.user_not_found)
        Glider.load(this.baseContext, user?.photoUrl?.toString()?:"", findViewById(R.id.iv_user_picture))
    }
}
