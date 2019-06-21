package com.sovize.riesgocop.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel

@IgnoreExtraProperties
data class User(
    val email: String,
    val rol: Int,
    val permission: CharArray
) : Parcelable {

    @Exclude
    @IgnoredOnParcel
    var firebaseUser: FirebaseUser? = null

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "N/A",
        parcel.readInt(),
        parcel.createCharArray() ?: charArrayOf('x')
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeInt(rol)
        parcel.writeCharArray(permission)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        if (email != other.email) return false
        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}