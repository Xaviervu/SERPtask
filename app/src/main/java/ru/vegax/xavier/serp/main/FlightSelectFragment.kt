package ru.vegax.xavier.serp.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import ru.vegax.xavier.serp.R
import ru.vegax.xavier.serp.adapters.FlightSelectAdapter
import ru.vegax.xavier.serp.models.Company
import ru.vegax.xavier.serp.models.Flight
import ru.vegax.xavier.serp.models.Hotel
import ru.vegax.xavier.serp.models.SelectedFlight
import java.util.*


class FlightSelectFragment : AppCompatDialogFragment(), FlightSelectAdapter.OnFlightSelectedClickListener {


    private lateinit var mListener: FragmentInteractionListener
    private var mSelFlightList: ArrayList<SelectedFlight>? = null
    private var mFilteredFlights : ArrayList<Flight>?=null
    private var mHotelList : ArrayList<Hotel>?=null
    private var mCompanyList : ArrayList<Company>?=null
    private var mLastSelectedFlight: SelectedFlight? = null // last selected option from list



    override fun onResume() {
        super.onResume()
        val window = dialog.window ?: return
        val params = window.attributes
//        params.width = resources.getDimensionPixelSize(R.dimen.popup_width)
        window.attributes = params
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val parent: ViewGroup? = null
        val view = inflater?.inflate(R.layout.selected_flights_layout, parent, false)
        builder.setView(view)

        //Initialize the RecyclerView
        val recVListOfFlights: RecyclerView? = view?.findViewById(R.id.recVListOfFlights)
        val txtVOkSel : TextView? = view?.findViewById<TextView>(R.id.txtOKSel)
        txtVOkSel?.setOnClickListener {
            if (mLastSelectedFlight != null){
                mListener.onFragmentResult(mLastSelectedFlight!!)
            }
            dialog.dismiss()
        }
        //Set the Layout Manager
        recVListOfFlights?.layoutManager = LinearLayoutManager(view?.context)

        //Initialize the ArrayLIst that will contain the data

        val currHotelId = arguments?.getInt(EXTRA_CURR_HOTEL)
        mHotelList= arguments?.getParcelableArrayList<Hotel>(EXTRA_HOTELS)
        mFilteredFlights = arguments?.getParcelableArrayList<Flight>(EXTRA_FLIGHTS)
        mCompanyList = arguments?.getParcelableArrayList<Company>(EXTRA_COMPANIES)



        if (currHotelId != null) {
            val currHotel: Hotel? = mHotelList?.get(currHotelId)
            if (currHotel != null) {
                mSelFlightList = getflightsPriceList(currHotel)
                if (view != null && mSelFlightList != null) {
                    val adapter = FlightSelectAdapter(view.context, mSelFlightList!!, this)
                    //Initialize the adapter and set it ot the RecyclerView
                    recVListOfFlights?.adapter = adapter
                }
            }
        }

        return builder.create()
    }



    override fun onFlightSelectedClick(position: Int) {
        val flightId = mSelFlightList?.get(position)?.flightId
        if (flightId != null) {
            val selectedFlight = mSelFlightList?.get(position)
            if (selectedFlight != null) {
                mLastSelectedFlight = selectedFlight

            }
        }

    }

    private fun getflightsPriceList(hotel: Hotel): ArrayList<SelectedFlight> {

        val flightList = ArrayList<SelectedFlight>()

        val flights = mFilteredFlights
        val companies = mCompanyList


        val flightIterator = flights?.listIterator()
        if (flightIterator != null) {
            for (flight in flightIterator) { // find the lowest price in its flights
                val price = flight.price
                val companyId = flight.companyId
                var companyName = ""
                if (companies != null && companies.size > companyId) {
                    companyName = companies[companyId].name ?: ""
                }
                val currFlight = SelectedFlight(flight.id, companyName, price + hotel.price)
                flightList.add(currFlight)
            }
        }
        //sort by price
        Collections.sort(flightList, object : Comparator<SelectedFlight> {
            override fun compare(o1: SelectedFlight, o2: SelectedFlight): Int {
                return o1.price  - o2.price
            }
        })

        return flightList
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    fun newInstance(curHotel: Int, hotelList: ArrayList<Hotel>, filteredFlights: ArrayList<Flight>, companyList: ArrayList<Company>): FlightSelectFragment {
        val fragment = FlightSelectFragment()

        val bundle = Bundle()
        bundle.putInt(EXTRA_CURR_HOTEL, curHotel)
        bundle.putParcelableArrayList(EXTRA_HOTELS,hotelList)
        bundle.putParcelableArrayList(EXTRA_FLIGHTS,filteredFlights)
        bundle.putParcelableArrayList(EXTRA_COMPANIES,companyList)


        fragment.arguments = bundle

        return fragment
    }


    interface FragmentInteractionListener {
        fun onFragmentResult(selectedFlight: SelectedFlight)
    }

    companion object {

        private const val EXTRA_CURR_HOTEL = "CURR_HOTEL_EXTRA"
        private const val EXTRA_FLIGHTS = "FLIGHTS_EXTRA"
        private const val EXTRA_HOTELS = "HOTELS_EXTRA"
        private const val EXTRA_COMPANIES = "COMPANIES_EXTRA"
    }

}// Required empty public constructor
