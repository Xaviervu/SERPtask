package ru.vegax.xavier.serp.adapters

import ru.vegax.xavier.serp.models.Flight
import ru.vegax.xavier.serp.models.Hotel

class AdapterData(hotel: Hotel, options: Int, lowestPrice: Int, flights :ArrayList<Flight>) {
    var hotel: Hotel
        internal set
    var options: Int = 0
        internal set
    var lowestPrice: Int =0
        internal set
    var flights: ArrayList<Flight>
        internal set
    init {
        this.hotel = hotel
        this.options = options
        this.lowestPrice = lowestPrice
        this.flights = flights
    }
}
