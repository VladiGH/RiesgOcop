package com.sovize.riesgocop.controlers.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.Document

class Pusher : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.d(AppLogger.messenger, "new message received ${p0?.notification?.body}")
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        val token = p0 ?: ""
        FirebaseAuth.getInstance().currentUser?.apply {
            Log.d(AppLogger.messenger, "new token created for app ${token}")
            FirebaseFirestore.getInstance().collection(Document.users)
                .document(this.uid).update("token", token)
        }

    }

}