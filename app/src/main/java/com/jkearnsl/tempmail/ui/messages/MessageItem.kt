package com.jkearnsl.tempmail.ui.messages
import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class MessageItem(
    val id: Int,
    val subject: String,
    val email: String,
    val date: Date
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(subject)
        parcel.writeString(email)
        parcel.writeLong(date.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageItem> {
        override fun createFromParcel(parcel: Parcel): MessageItem {
            return MessageItem(parcel)
        }

        override fun newArray(size: Int): Array<MessageItem?> {
            return arrayOfNulls(size)
        }
    }
}