
package com.bachelor.reservation.classes

import android.os.Parcel
import android.os.Parcelable

data class Reservation(
        val reservationID: String?,
        val userID: String? = null,
        val service: String?,
        val day: String?,
        val month: String?,
        val year: String?,
        val startHour: String?,
        val startMinute: String?,
        val endHour: String?,
        val endMinute: String?,
        val userNote: String? = null,
        val confirmed: Boolean? = false,
        val finished: Boolean? = false,
        val adminNote: String? = null
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
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(reservationID)
        parcel.writeString(userID)
        parcel.writeString(service)
        parcel.writeString(day)
        parcel.writeString(month)
        parcel.writeString(year)
        parcel.writeString(startHour)
        parcel.writeString(startMinute)
        parcel.writeString(endHour)
        parcel.writeString(endMinute)
        parcel.writeString(userNote)
        parcel.writeValue(confirmed)
        parcel.writeValue(finished)
        parcel.writeString(adminNote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reservation> {
        override fun createFromParcel(parcel: Parcel): Reservation {
            return Reservation(parcel)
        }

        override fun newArray(size: Int): Array<Reservation?> {
            return arrayOfNulls(size)
        }
    }
}