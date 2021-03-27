package com.bachelor.reservation.classes

import android.os.Parcel
import android.os.Parcelable

class UserConversation (val convID: String? = "", val participantOneID: String? = "", val participantTwoID: String? = "", val participantOneName: String? = "", val participantTwoName: String? = "" ) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(convID)
        parcel.writeString(participantOneID)
        parcel.writeString(participantTwoID)
        parcel.writeString(participantOneName)
        parcel.writeString(participantTwoName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserConversation> {
        override fun createFromParcel(parcel: Parcel): UserConversation {
            return UserConversation(parcel)
        }

        override fun newArray(size: Int): Array<UserConversation?> {
            return arrayOfNulls(size)
        }
    }
}