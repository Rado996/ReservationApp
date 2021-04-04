package com.bachelor.reservationapp.classes

import android.os.Parcel
import android.os.Parcelable

class User (val uid: String? = null , val userName: String?  = null, val userSecondName: String? = null , val userPhone: String? = null, val userEmail: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(userName)
        parcel.writeString(userSecondName)
        parcel.writeString(userPhone)
        parcel.writeString(userEmail)
    }

    override fun describeContents(): Int {
        return 0
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