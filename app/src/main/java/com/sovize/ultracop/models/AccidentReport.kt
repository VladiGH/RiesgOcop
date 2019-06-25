package com.sovize.ultracop.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class AccidentReport (
    @get:Exclude
    var id: String = "N/A",
   // val title: String = "N/A",
    val location: String = "N/A",
    val personInjuredName: String = "N/A",
    val personInjuredGender: String = "N/A",
    val accidentedPersonType: String = "N/A",
    val description: String = "N/A",
    val severityLevel: String = "N/A",
    val placeOfAttention: String = "N/A",
    val ambullance: String = "N/A",
    val pictures: List<String> = listOf("N/A"),
    val date: String = "0",
    val state: Int = 1,
    val user: String = "N/A",
    val gpsCoord: String = "N/A"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?: "N/A",
        //parcel.readString() ?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readString()?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readString()?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.readString() ?: "N/A",
        parcel.createStringArrayList() ?: listOf("N/A"),
        parcel.readString()?: "N/A",
        parcel.readInt(),
        parcel.readString()?: "N/A",
        parcel.readString()?:"N/A"
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
       // parcel.writeString(title)
        parcel.writeString(location)
        parcel.writeString(personInjuredName)
        parcel.writeString(personInjuredGender)
        parcel.writeString(accidentedPersonType)
        parcel.writeString(description)
        parcel.writeString(severityLevel)
        parcel.writeString(placeOfAttention)
        parcel.writeString(ambullance)
        parcel.writeStringList(pictures)
        parcel.writeString(date)
        parcel.writeInt(state)
        parcel.writeString(user)
        parcel.writeString(gpsCoord)
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