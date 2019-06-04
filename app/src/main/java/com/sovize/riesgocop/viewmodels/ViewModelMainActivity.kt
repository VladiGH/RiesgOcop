package com.sovize.riesgocop.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger

class ViewModelMainActivity : ViewModel() {

    val reportList = MutableLiveData<MutableList<Report>>()
    private var reportsDB = FirebaseFirestore.getInstance()

    fun getReportsData() {
        if (reportList.value.isNullOrEmpty()) {
            Log.d(AppLogger.viewModelMainActivity, "Database inroad")
            queryDB()
        }
    }

    private fun queryDB() {
        reportsDB.collection("Reports")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val results = mutableListOf<Report>()
                    for (document in task.result!!) {
                        Log.d(ContentValues.TAG, "id: " + document.id + " => " + document.data)
                        results.add(
                            Report(
                                document.get("id").toString(), document.get("title").toString(),
                                document.get("danger").toString()
                            )
                        )
                        reportList.value = results
                    }

                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                }
            }
    }
}