package com.sovize.ultracop.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Report(
    @Exclude  var id: String = "N/A",
    val title: String = "N/A",
    val danger: Long = 0,
    val description: String = "N/A",
    val location: String = "N/A",
    val pictures: List<String> = listOf("N/A"),
    val date: String = "0"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "N/a",
        parcel.readString() ?: "N/a",
        parcel.readLong(),
        parcel.readString() ?: "N/a",
        parcel.readString() ?: "N/a",
        parcel.createStringArrayList() ?: listOf("N/a"),
        parcel.readString() ?: "0"
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeLong(danger)
        parcel.writeString(description)
        parcel.writeString(location)
        parcel.writeStringList(pictures)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Report> {
        override fun createFromParcel(parcel: Parcel): Report {
            return Report(parcel)
        }

        override fun newArray(size: Int): Array<Report?> {
            return arrayOfNulls(size)
        }
    }
}