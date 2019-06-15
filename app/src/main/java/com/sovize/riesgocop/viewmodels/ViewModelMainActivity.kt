package com.sovize.riesgocop.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import com.sovize.riesgocop.controlers.firebase.ReportDao
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger
import kotlinx.coroutines.launch

class ViewModelMainActivity : ViewModel() {

    val reportList = MutableLiveData<MutableList<Report>>()
    private val user = MutableLiveData<FirebaseUser>()
    private val repository = ReportDao()

    fun getReportsData() {
        if (reportList.value.isNullOrEmpty()) {
            Log.d(AppLogger.viewModelMainActivity, "Database loading first time")
            repository.getReports {
                updateResult(it)
            }
        }
    }

    fun getUserData(): LiveData<FirebaseUser> {
        return user
    }

    fun setUserState() {
        user.value = FirebaseAuth.getInstance().currentUser
    }

    private fun updateResult(query: QuerySnapshot?) {
        viewModelScope.launch {
            val newSet = mutableListOf<Report>()
            val oldSet = reportList.value
            query?.documentChanges?.forEach {
                val tempDoc = it.document.toObject(Report::class.java)
                tempDoc.id = it.document.id
                when (it.type) {
                    DocumentChange.Type.ADDED -> {
                        Log.d(AppLogger.viewModelMainActivity, "reporte ingresado $tempDoc")
                        newSet.add(tempDoc)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        Log.d(AppLogger.viewModelMainActivity, "reporte modificado $tempDoc")
                        for (x in 0 until oldSet?.size!!) {
                            if (oldSet[x].id == tempDoc.id) {
                                oldSet[x] = tempDoc
                                break
                            }
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        Log.d(AppLogger.viewModelMainActivity, "reporte borrado $tempDoc")
                        oldSet?.remove(tempDoc)
                    }
                    else -> {
                        Log.e(AppLogger.reportDao, "Unknown report estate")
                    }
                }
            }
            if (oldSet != null) {
                newSet.addAll(oldSet)
            }
            reportList.value = newSet
        }
    }
}