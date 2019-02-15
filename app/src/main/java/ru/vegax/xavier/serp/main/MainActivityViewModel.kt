package ru.vegax.xavier.serp.main

import android.arch.lifecycle.ViewModel
import ru.vegax.xavier.serp.adapters.AdapterData
import ru.vegax.xavier.serp.models.Companies
import ru.vegax.xavier.serp.models.Flight
import ru.vegax.xavier.serp.models.FlightList
import ru.vegax.xavier.serp.models.HotelsList

import ru.vegax.xavier.serp.repositories.DataRepository

internal class MainActivityViewModel : ViewModel(), OnDataListener {
    private var repo: DataRepository? = null

    internal var hotelsList: HotelsList? = null
    internal var flightList: FlightList? = null
    internal var companies: Companies? = null
    var adapterList = ArrayList<AdapterData>()
    private lateinit var mOnDataListener: OnDataListener

    fun init(onDataListener: OnDataListener) {
        mOnDataListener = onDataListener
        if (repo != null) {
            getData()
            return
        }
        repo = DataRepository.getInstance(this)
        getData()
    }

    fun getData() {
        repo?.loadData()
    }

    override fun onNewData(hotelsList: HotelsList?, flightList: FlightList?, companies: Companies?) {
        this.hotelsList = hotelsList
        this.flightList = flightList
        this.companies = companies
        getAdapterData()

        mOnDataListener.onNewData(this.hotelsList, this.flightList, this.companies)
    }

    override fun onError(error: String) {
        mOnDataListener.onError(error)
    }

    private fun getAdapterData() { // get the lowest price for every hotel

        getFilteredAdapterData(-1)
    }

    fun getFilteredAdapterData(filterId: Int): ArrayList<AdapterData> {
        val hotelsList = this.hotelsList
        val flightList = this.flightList
        adapterList.clear()
        val hotelIterator = hotelsList?.hotels?.listIterator()

        if (hotelIterator != null) {
            for (currHotel in hotelIterator) { // for each hotel
                var theLowestPrice = 0
                var flightNums = 0 // number of flights for the filter
                val flights = ArrayList<Flight>()
                val flightIdIterator = currHotel.flightIds?.listIterator() // flights for selectedHotel
                if (flightIdIterator != null) {
                    for (flightId in flightIdIterator) { // find the lowest price in its flights
                        val currFlight = flightList?.flights?.get(flightId)
                        if (currFlight != null) {
                            val currCompanyId = currFlight.companyId

                            if (filterId < 0 || currCompanyId == filterId) { // filter if needed
                                flights.add(currFlight)
                                val price = currFlight.price 
                                if (price != 0) {
                                    if (theLowestPrice != 0) {
                                        if (price < theLowestPrice) {
                                            theLowestPrice = price
                                        }
                                    } else {
                                        theLowestPrice = price
                                    }
                                }
                                flightNums ++ // has at least
                            }
                        }

                    }
                }
                if (flightNums >0){
                    adapterList.add(AdapterData(hotel = currHotel, lowestPrice = theLowestPrice + currHotel.price, options = flightNums, flights = flights))
                }
            }
        }
        return adapterList
    }

}
