package ru.vegax.xavier.serp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Flight() : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("companyId")
    @Expose
    var companyId: Int = 0
    @SerializedName("departure")
    @Expose
    var departure: String? = null
    @SerializedName("arrival")
    @Expose
    var arrival: String? = null
    @SerializedName("price")
    @Expose
    var price: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        companyId = parcel.readInt()
        departure = parcel.readString()
        arrival = parcel.readString()
        price = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(companyId)
        parcel.writeString(departure)
        parcel.writeString(arrival)
        parcel.writeInt(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flight> {
        override fun createFromParcel(parcel: Parcel): Flight {
            return Flight(parcel)
        }

        override fun newArray(size: Int): Array<Flight?> {
            return arrayOfNulls(size)
        }
    }

}