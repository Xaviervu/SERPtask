package ru.vegax.xavier.serp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HotelsList {

    @SerializedName("hotels")
    @Expose
    lateinit var hotels: List<Hotel>

}