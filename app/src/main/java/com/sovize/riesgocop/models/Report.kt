package com.sovize.riesgocop.models

import android.os.Parcel
import android.os.Parcelable

data class Report(
    val id: String = "N/A",
    val title: String = "N/A",
    val danger: Long = 0,
    val description: String = "N/A",
    val location: String = "N/A",
    val pictures: List<String> = listOf("N/A")
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"N/a",
        parcel.readString()?:"N/a",
        parcel.readLong()?:0,
        parcel.readString()?:"N/a",
        parcel.readString()?:"N/a",
        parcel.createStringArrayList()?: listOf("N/a")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeLong(danger)
        parcel.writeString(description)
        parcel.writeString(location)
        parcel.writeStringList(pictures)
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