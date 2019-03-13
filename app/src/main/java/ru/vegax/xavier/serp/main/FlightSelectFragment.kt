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
    private var mCurrHotel: Hotel? = null
    private var mFilteredFlights: ArrayList<Flight>? = null
    private var mCompanyList: ArrayList<Company>? = null
    private var mSelFlightList: List<SelectedFlight>? = null // for showing in dialog


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
        val txtVOkSel : TextView? = view?.findViewById<TextView>(R.id.txtVOkSel)
        txtVOkSel?.setOnClickListener {
            if (mLastSelectedFlight != null) {
                mListener.onFragmentResult(mCurrHotel!!,mLastSelectedFlight!!)
            }
            dialog.dismiss()
        }
        val recVListOfFlights: RecyclerView? = view?.findViewById(R.id.recVListOfFlights)
        recVListOfFlights?.layoutManager = LinearLayoutManager(view?.context)
        mCurrHotel = arguments?.getParcelable(EXTRA_CURR_HOTEL)
        mFilteredFlights = arguments?.getParcelableArrayList<Flight>(EXTRA_FLIGHTS)
        mCompanyList = arguments?.getParcelableArrayList<Company>(EXTRA_COMPANIES)

        if (mCurrHotel != null) {
            mSelFlightList = getFlightsPriceList()
            if (view != null && mSelFlightList != null) {
                val adapter = FlightSelectAdapter(view.context, mSelFlightList!!, this)
                recVListOfFlights?.adapter = adapter
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

    private fun getFlightsPriceList(): List<SelectedFlight>? {
        val hotel = mCurrHotel
        val flights = mFilteredFlights
        val companies = mCompanyList
        return flights?.map { flight ->
            SelectedFlight(flight.id, companies!![flight.companyId], flight.price + hotel!!.price) }?.toList()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    fun newInstance(curHotel: Hotel, filteredFlights: ArrayList<Flight>, companyList: ArrayList<Company>): FlightSelectFragment {
        val fragment = FlightSelectFragment()
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_CURR_HOTEL, curHotel)
        bundle.putParcelableArrayList(EXTRA_FLIGHTS, filteredFlights)
        bundle.putParcelableArrayList(EXTRA_COMPANIES, companyList)
        fragment.arguments = bundle
        return fragment
    }

    interface FragmentInteractionListener {
        fun onFragmentResult(currHotel :Hotel, selectedFlight: SelectedFlight)
    }

    companion object {
        private const val EXTRA_CURR_HOTEL = "CURR_HOTEL_EXTRA"
        private const val EXTRA_FLIGHTS = "FLIGHTS_EXTRA"
        private const val EXTRA_COMPANIES = "COMPANIES_EXTRA"
    }

}
