package ru.vegax.xavier.serp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HotelsList {

    @SerializedName("hotels")
    @Expose
    var hotels: List<Hotel>? = null

}