package com.sovize.ultracop.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.controlers.firebase.ReportDao
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.models.User
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.Document
import kotlinx.coroutines.launch

class ViewModelMainActivity : ViewModel() {

    val reportList = MutableLiveData<MutableList<AccidentReport>>()
    private val user = MutableLiveData<User>()
    //Todo change for a MasterCrud
    private val repository = ReportDao()
    private val master = MasterCrud()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser != null) {
                if (user.value == null) {
                    master.readUid(Document.users, it.currentUser!!.uid) { doc ->
                        doc?.toObject(User::class.java).apply {
                            this?.firebaseUser = it.currentUser
                            user.postValue(this)
                        }
                    }
                }
            } else {
                user.postValue(null)
            }
        }
    }

    fun getReportsData() {
        if (reportList.value.isNullOrEmpty()) {
            Log.d(AppLogger.viewModelMainActivity, "Database loading first time")
            repository.getReports {
                updateResult(it)
            }
        }
    }

    fun getUserData(): LiveData<User> {
        return user
    }

    private fun updateResult(query: QuerySnapshot?) {
        viewModelScope.launch {
            val newSet = mutableListOf<AccidentReport>()
            val oldSet = reportList.value

            query?.documentChanges?.forEach {
                val tempDoc = it.document.toObject(AccidentReport::class.java)
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