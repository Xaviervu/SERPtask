package ru.vegax.xavier.serp.main

import ru.vegax.xavier.serp.models.Companies
import ru.vegax.xavier.serp.models.FlightList
import ru.vegax.xavier.serp.models.HotelsList

interface OnDataListener {
    fun onNewData(hotelsList: HotelsList?, flightList: FlightList?, companies: Companies?)
    fun onError(error: String)
}