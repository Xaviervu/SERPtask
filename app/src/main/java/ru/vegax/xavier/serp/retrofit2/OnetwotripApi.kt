package ru.vegax.xavier.serp.retrofit2

import retrofit2.Call
import retrofit2.http.GET
import ru.vegax.xavier.serp.models.Companies
import ru.vegax.xavier.serp.models.FlightList
import ru.vegax.xavier.serp.models.HotelsList

interface OnetwotripApi {

    @get:GET("zqxvw")
    val flightList: Call<FlightList>

    @get:GET("12q3ws")
    val hotelsList: Call<HotelsList>

    @get:GET("8d024")
    val companies: Call<Companies>
}
