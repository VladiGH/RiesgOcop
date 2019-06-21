package com.sovize.riesgocop.controlers.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.Document

class MasterCrud {

    private var reportsDB = FirebaseFirestore.getInstance()

    fun <E : Any> insert(collection: String, dataObject: E, callback: (Boolean) -> Unit = {}) {
        reportsDB.collection(collection)
            .add(dataObject)
            .addOnSuccessListener { newDoc ->
                Log.d(AppLogger.reportDao, "DocumentSnapshot: ${dataObject::class.java.simpleName} written with ID: " + newDoc.id)
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(AppLogger.reportDao, "Error adding document", e)
                callback(false)
            }
    }

    fun <E : Any> read(){
        reportsDB.collection(Document.accident).orderBy("date")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(AppLogger.reportDao, "listen:error check network", e)
                } else {
                    //callback(snapshot)
                }
            }
    }
}