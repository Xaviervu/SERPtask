package ru.vegax.xavier.serp.models


data class DetailsModel(
        val flights: List<Flight>,
        val hotels: List<Hotel>,
        val companies: List<Company>)