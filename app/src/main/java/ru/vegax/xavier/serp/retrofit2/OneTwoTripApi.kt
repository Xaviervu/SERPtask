package ru.vegax.xavier.serp.retrofit2

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

import ru.vegax.xavier.serp.models.Companies
import ru.vegax.xavier.serp.models.FlightList
import ru.vegax.xavier.serp.models.HotelsList

interface OneTwoTripApi {

    @GET("zqxvw")
    fun flightList(): Single<FlightList>

    @GET("12q3ws")
    fun hotelsList(): Single<HotelsList>

    @GET("8d024")
    fun companies(): Single<Companies>
}
