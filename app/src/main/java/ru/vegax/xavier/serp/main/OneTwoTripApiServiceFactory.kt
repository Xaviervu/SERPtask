package ru.vegax.xavier.serp.main

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.vegax.xavier.serp.retrofit2.OneTwoTripApi


object OneTwoTripApiServiceFactory {

    fun createService(): OneTwoTripApi = Retrofit.Builder()
            .baseUrl("https://api.myjson.com/bins/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(OneTwoTripApi::class.java)

}