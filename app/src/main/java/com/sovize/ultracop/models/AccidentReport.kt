package com.sovize.ultracop.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Model of the Accident Report
 */
@IgnoreExtraProperties
data class AccidentReport(
    @get:Exclude
    var id: String = "N/A",
    val location: String = "N/A",
    val personInjuredName: String = "N/A",
    val personInjuredGender: Int = 0,
    val personType: Int = 0,
    val description: String = "N/A",
    val severityLevel: Int = 0,
    val placeOfAttention: Int = 0,
    val ambulance: Int = 0,
    val pictures: List<String> = listOf("N/A"),
    val date: String = "0",
    val state: Int = 0,
    val user: String = "N/A",
    val longitude: Double = 0.toDouble(),
    val latitude: Double = 0.toDouble()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "N/A",
        //parcel.readString() ?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "N/A",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createStringArrayList() ?: listOf("N/A"),
        parcel.readString() ?: "N/A",
        parcel.readInt(),
        parcel.readString() ?: "N/A",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(location)
        parcel.writeString(personInjuredName)
        parcel.writeInt(personInjuredGender)
        parcel.writeInt(personType)
        parcel.writeString(description)
        parcel.writeInt(severityLevel)
        parcel.writeInt(placeOfAttention)
        parcel.writeInt(ambulance)
        parcel.writeStringList(pictures)
        parcel.writeString(date)
        parcel.writeInt(state)
        parcel.writeString(user)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccidentReport> {
        override fun createFromParcel(parcel: Parcel): AccidentReport {
            return AccidentReport(parcel)
        }

        override fun newArray(size: Int): Array<AccidentReport?> {
            return arrayOfNulls(size)
        }
    }
}