package com.sovize.riesgocop.controlers.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger


class ReportDao {

    private var reportsDB = FirebaseFirestore.getInstance()

    fun insertReport(report: Report, callback: (Boolean) -> Unit) {
        reportsDB.collection("Reports")
            .add(report)
            .addOnSuccessListener { newdoc ->
                Log.d(
                    AppLogger.reportDao,
                    "DocumentSnapshot written with ID: " + newdoc.id
                )
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(AppLogger.reportDao, "Error adding document", e)
                callback(false)
            }
    }


}