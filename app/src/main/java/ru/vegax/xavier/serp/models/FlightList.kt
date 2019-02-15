package ru.vegax.xavier.serp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightList {

    @SerializedName("flights")
    @Expose
    var flights: List<Flight>? = null

}