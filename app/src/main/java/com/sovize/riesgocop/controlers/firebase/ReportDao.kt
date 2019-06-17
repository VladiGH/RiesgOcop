package com.sovize.riesgocop.controlers.firebase

import android.util.Log
import com.google.firebase.firestore.*
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.Document


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