package ru.vegax.xavier.serp.repositories

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vegax.xavier.serp.main.OnDataListener
import ru.vegax.xavier.serp.models.Companies
import ru.vegax.xavier.serp.models.FlightList
import ru.vegax.xavier.serp.models.HotelsList
import ru.vegax.xavier.serp.retrofit2.OneTwoTripApi

//Singleton pattern
class DataRepository(private val mOnDataListener: OnDataListener) {
//    private var mFlightList: FlightList? = null
//    private var mHotelsList: HotelsList? = null
//    private var mCompanies: Companies? = null
//
//    fun loadData() {
//        val userCall = oneTwoTripApi?.flightList
//        userCall?.enqueue(object : Callback<FlightList> {
//            override fun onResponse(call: Call<FlightList>, response: Response<FlightList>) {
//                if (!response.isSuccessful) {
//                    mOnDataListener.onError("no connection")
//                    return
//                }
//                if (response.body() != null) {
//                    mFlightList = response.body()!!
//                    getHotels() // get the hotelsList data
//                }
//
//            }
//
//            override fun onFailure(call: Call<FlightList>, t: Throwable) {
//                mOnDataListener.onError("no connection")
//            }
//        })
//    }
//
//    fun getHotels() {
//        val userCall = oneTwoTripApi?.hotelsList
//        userCall?.enqueue(object : Callback<HotelsList> {
//            override fun onResponse(call: Call<HotelsList>, response: Response<HotelsList>) {
//                if (!response.isSuccessful) {
//                    mOnDataListener.onError("no connection")
//                    return
//                }
//                if (response.body() != null) {
//                    mHotelsList = response.body()!!
//                    getCompanies() // get mCompanies
//                }
//            }
//
//            override fun onFailure(call: Call<HotelsList>, t: Throwable) {
//                mOnDataListener.onError("no connection")
//            }
//        })
//    }
//
//    fun getCompanies() {
//        val userCall = oneTwoTripApi?.companies
//        userCall?.enqueue(object : Callback<Companies> {
//            override fun onResponse(call: Call<Companies>, response: Response<Companies>) {
//                if (!response.isSuccessful) {
//                    mOnDataListener.onError("no connection")
//                    return
//                }
//                if (response.body() != null) {
//                    mCompanies = response.body()!!
//                    mOnDataListener.onNewData(mHotelsList, mFlightList, mCompanies) // return data callback
//                }
//            }
//
//            override fun onFailure(call: Call<Companies>, t: Throwable) {
//                mOnDataListener.onError("no connection")
//            }
//        })
//    }
//
//    companion object {
//        private var instance: DataRepository? = null
//        private var oneTwoTripApi: OneTwoTripApi? = null
//
//        fun getInstance(onDataListener: OnDataListener): DataRepository {
//
//            if (instance == null) {
//                instance = DataRepository(onDataListener)
//            }
//            val retrofit = Retrofit
//                    .Builder()
//                    .baseUrl("https://api.myjson.com/bins/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            oneTwoTripApi = retrofit.create(OneTwoTripApi::class.java)
//
//            return instance as DataRepository
//        }
//    }
}
