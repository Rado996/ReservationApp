package com.bachelor.reservation.classes

import android.os.Parcel
import android.os.Parcelable

data class Service(val id: String?,
                   val title: String?,
                   val pictureLink: String?,
                   val description: String?,
                   val duration: String?
              ) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(pictureLink)
        parcel.writeString(description)
        parcel.writeString(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Service> {
        override fun createFromParcel(parcel: Parcel): Service {
            return Service(parcel)
        }

        override fun newArray(size: Int): Array<Service?> {
            return arrayOfNulls(size)
        }
    }
}



