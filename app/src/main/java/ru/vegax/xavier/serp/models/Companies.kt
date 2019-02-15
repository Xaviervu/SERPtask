package ru.vegax.xavier.serp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Companies {

    @SerializedName("companies")
    @Expose
    var companies: List<Company>? = null

}