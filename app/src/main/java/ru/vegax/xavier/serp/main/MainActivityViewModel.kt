package ru.vegax.xavier.serp.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Single.zip
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.vegax.xavier.serp.adapters.AdapterData
import ru.vegax.xavier.serp.main.MainActivityViewModel.LoadingStatus.LOADING
import ru.vegax.xavier.serp.main.MainActivityViewModel.LoadingStatus.NOT_LOADING
import ru.vegax.xavier.serp.models.*

internal class MainActivityViewModel : ViewModel() {
    private val TAG = "XavvViewModel"
    private val oneTwoTripApiService by lazy {
        OneTwoTripApiServiceFactory.createService()
    }

    enum class LoadingStatus { LOADING, NOT_LOADING }


    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    //received hotels data
    private val _adapterData = MutableLiveData<ArrayList<AdapterData>>()
    val adapterData: LiveData<ArrayList<AdapterData>> = _adapterData

    //received hotels data
    private val _loadErr = MutableLiveData<Throwable>()
    val loadErr: LiveData<Throwable> = _loadErr


    var detailsModel: DetailsModel? = null

    fun loadData(companyIdFilter: Int) {

        val zip = zip(
                oneTwoTripApiService.flightList(),
                oneTwoTripApiService.hotelsList(),
                oneTwoTripApiService.companies(),
                Function3<FlightList, HotelsList, Companies, DetailsModel>
                { flights: FlightList, hotels: HotelsList, companies: Companies ->
                    DetailsModel(flights.flights,
                            hotels.hotels,
                            companies.companies)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe { _loadingStatus.postValue(LOADING) }
                .doFinally {
                    _loadingStatus.postValue(NOT_LOADING)
                }
                .subscribeBy(
                        onSuccess = {
                            detailsModel = it
                            getHotelResults(companyIdFilter)
                        },
                        onError = { error ->
                            _loadErr.postValue(error)
                            Log.e(TAG, "observable exception: ", error.cause)
                        })
    }

    fun getHotelResults(companyIdFilter: Int) {

        val adapterList = ArrayList<AdapterData>()
        val flightIdToFlightMap = detailsModel?.flights?.map { it.id to it }?.toMap() //map for finding companyId by flightId
        detailsModel?.hotels?.forEach { curHotel ->
            var options = 0
            var lowestPrice = 0
            val flights = ArrayList<Flight>()
            curHotel.flightIds.map { flightIdToFlightMap!![it] } // for every flightId in hotel
                    .filter { (companyIdFilter == NO_FILTER) || (it?.companyId == companyIdFilter) } // filtered flights
                    .sortedBy { it?.price } // sort them
                    .forEach { currFlight ->
                        // for each flight
                        currFlight?.let {
                            // find the lowest price
                            if (options == 0) {
                                lowestPrice = currFlight.price  // the first price should be the lowest
                            }
                            options++ // record number of options
                            flights.add(currFlight)  // add to flight list
                        }
                    }
            if (options > 0) {
                adapterList.add(AdapterData(curHotel, options, lowestPrice, flights)) // add a new element to list
            }
        }
        _adapterData.postValue(adapterList)
    }

    companion object {
        const val NO_FILTER: Int = -1
    }
}
