package com.sovize.ultracop.controlers.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.Document


class ReportDao {

    private var reportsDB = FirebaseFirestore.getInstance()

    fun insertReport(report: AccidentReport, callback: (Boolean) -> Unit) {
        reportsDB.collection(Document.accident)
            .add(report)
            .addOnSuccessListener { newDoc ->
                Log.d(
                    AppLogger.reportDao,
                    "DocumentSnapshot written with ID: " + newDoc.id
                )
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(AppLogger.reportDao, "Error adding document", e)
                callback(false)
            }
    }

    fun getReports(callback: (QuerySnapshot?) -> Unit) {
        reportsDB.collection(Document.accident).orderBy("date")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(AppLogger.reportDao, "listen:error check network", e)
                } else {
                    callback(snapshot)
                }
            }
    }
}