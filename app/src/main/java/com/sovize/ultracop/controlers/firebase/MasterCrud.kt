package com.sovize.ultracop.controlers.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.models.User
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.Document

class MasterCrud {

    private var reportsDB = FirebaseFirestore.getInstance()

    /**
     * this method can insert any object in fireBase
     *
     * @param collection this is the name of the collection where to insert the data
     * @param dataObject this is the object of any type *E* to serialise and insert
     * @param callback *Boolean -> Unit* is a call to a lambda function to call with the result of the
     * operation
     */
    fun <E : Any> insert(collection: String, dataObject: E, callback: (Boolean) -> Unit = {}) {
        reportsDB.collection(collection)
            .add(dataObject)
            .addOnSuccessListener { newDoc ->
                Log.d(
                    AppLogger.reportDao,
                    "DocumentSnapshot: ${dataObject::class.java.simpleName} written with ID: " + newDoc.id
                )
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(AppLogger.reportDao, "Error adding document", e)
                callback(false)
            }
    }

    fun insertWithUid(collection: String, dataObject: User, callback: () -> Unit) {
        reportsDB.collection(collection)
            .document(dataObject.firebaseUser!!.uid)
            .set(dataObject)
            .addOnSuccessListener {
                Log.d(
                    AppLogger.reportDao,
                    "DocumentSnapshot: User written with ID: ${dataObject.firebaseUser?.uid}"
                )
                callback()
            }
            .addOnFailureListener { e ->
                Log.e(AppLogger.reportDao, "Error adding document", e)
            }
    }

    fun readAll(collection: String, callback: (QuerySnapshot?) -> Unit = {}) {
        reportsDB.collection(collection).orderBy("date")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(AppLogger.reportDao, "listen:error check network", e)
                } else {
                    callback(snapshot)
                }
            }
    }

    /**
     * @param name of the collection
     * @param key of the field to update
     * @param the value of the specific field that we want to update
     * @return query document snapshot with the items that corresponds to the third param
     */
    fun readWhereEq(collection: String, key: String, equal: String, callback: (QuerySnapshot?) -> Unit = {}) {
        reportsDB.collection(collection)
            .whereEqualTo(key, equal).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result)
                } else {
                    Log.d(AppLogger.reportDao, "listen:error check network", task.exception)
                }
            }
    }

    //this function allow to retrieve a particular sub set os snapshot by the ID of its parent
    fun readUid(collection: String, id: String, callback: (DocumentSnapshot?) -> Unit) {
        reportsDB.collection(collection).document(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(it.result)
                } else {
                    Log.e(AppLogger.master, "Read UI failed with:", it.exception)
                }
            }
    }

    /**
     * @param report is th report to be updated on FireStore
     * @return Task which it is just a closure around an IO operation to FireStore
     * that has attachable listener onFailure and onSuccess
     */
    fun updateReport(report: AccidentReport, state: Int): Task<Void> =
        reportsDB.collection(Document.accident).document(report.id)
            .update("state", state)

}