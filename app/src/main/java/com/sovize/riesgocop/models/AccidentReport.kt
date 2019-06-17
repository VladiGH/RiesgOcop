package com.sovize.riesgocop.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class AccidentReport (
    @Exclude var id: String = "N/A",
    val location: String = "N/A",
    val personInjuredName: String = "N/A",
    val personInjuredGender: String = "N/A",
    val accidentedPersonType: String = "N/A",
    val description: String = "N/A",
    val SeverityLevel: String = "N/A",
    val placeOfAttention: String = "N/A",
    val ambullance: String = "N/A",
    val pictures: List<String> = listOf("N/A"),
    val date: String = "0"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(location)
        parcel.writeString(personInjuredName)
        parcel.writeString(personInjuredGender)
        parcel.writeString(accidentedPersonType)
        parcel.writeString(description)
        parcel.writeString(SeverityLevel)
        parcel.writeString(placeOfAttention)
        parcel.writeString(ambullance)
        parcel.writeStringList(pictures)
        parcel.writeString(date)
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