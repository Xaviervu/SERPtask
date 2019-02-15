package ru.vegax.xavier.serp.main


import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent

import android.view.View
import android.support.v7.widget.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import ru.vegax.xavier.serp.R
import ru.vegax.xavier.serp.adapters.HotelAdapter
import ru.vegax.xavier.serp.models.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener





class MainActivity : AppCompatActivity(), OnDataListener, SwipeRefreshLayout.OnRefreshListener,
        HotelAdapter.OnAdapterClickListener,FlightSelectFragment.FragmentInteractionListener {


    private val TAG = "XavvMainActivity"

    private lateinit var mAdapter: HotelAdapter

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var lastSelectedHotel: Hotel? = null
    private var mToolbar: Toolbar? = null

    private var mSpinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "on Create")

        mToolbar = findViewById(R.id.toolbar)
        mToolbar?.title = resources.getString(R.string.app_name)

        mSwipeRefreshLayout = findViewById(R.id.swipe_container)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.isRefreshing = true



        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.init(this) // set data listener

        mAdapter = HotelAdapter(this, this)



        initRecyclerView()


    }

    private fun initSpinner() {
        //spinner
        mSpinner = this.findViewById(R.id.spinner)

        val spinnerList = ArrayList<String>()
        val companyIdList = ArrayList<Int>()
        spinnerList.add(getString(R.string.filter)) // first word means no filtration
        companyIdList.add(-1)

        val companiesList = mainActivityViewModel.companies?.companies
        val iterator = companiesList?.iterator()
        if (iterator !=null)
        for(company in iterator){
            company.name?.let { spinnerList.add(it) }
            companyIdList.add(company.id)
        }
        val myAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.custom_spinner_item,
                spinnerList)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner?.adapter = myAdapter

        mSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {

                filterBy(companyIdList[position])

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // do nothing
            }

        }
    }

    private fun filterBy(companyId: Int) {
        val list = mainActivityViewModel.getFilteredAdapterData(companyId)
        mAdapter.setDataLists(list)
    }

    override fun onClick(position: Int) {
        val adapterData = mainActivityViewModel.adapterList[position]

        val nOfHotels = adapterData.options
        if (nOfHotels > 0) {
            if (nOfHotels == 1) {
                val companies = mainActivityViewModel.companies?.companies
                if (companies != null){

                    Toast.makeText(this@MainActivity, "Выбран отель \"${adapterData.hotel.name}\" через ${companies.get(adapterData.flights[0].companyId).name} за ${adapterData.flights[0].price +adapterData.hotel.price }р", Toast.LENGTH_SHORT).show()
                }
            } else {
                lastSelectedHotel = adapterData.hotel
                val deviceSelect = FlightSelectFragment().newInstance(adapterData.hotel.id, mainActivityViewModel.hotelsList?.hotels as ArrayList<Hotel>,
                        adapterData.flights, mainActivityViewModel.companies?.companies as ArrayList<Company>)

                deviceSelect.show(supportFragmentManager, "selectDialog")
            }
        }
    }

    private fun initRecyclerView() {
        //Initialize the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVUsers)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //Set the Layout Manager
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = mAdapter
        recyclerView?.addItemDecoration(VerticalSpaceItemDecoration(40))
        recyclerView?.setHasFixedSize(true)
    }
    override fun onFragmentResult(selectedFlight: SelectedFlight) {
        if (lastSelectedHotel != null){
            Toast.makeText(this@MainActivity, "Выбран отель \"${lastSelectedHotel!!.name}\" через ${selectedFlight.company} за ${selectedFlight.price}р", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRefresh() {
        refreshData()
    }

    private fun refreshData() {
        Log.d(TAG, "refresh")
        mainActivityViewModel.getData()

    }

    override fun onNewData(hotelsList: HotelsList?, flightList: FlightList?, companies: Companies?) {
        Log.d(TAG, "new Data")
        mSwipeRefreshLayout.isRefreshing = false
        val list = mainActivityViewModel.adapterList
        mAdapter.setDataLists(list)
        initSpinner()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun onError(error: String) {
        mSwipeRefreshLayout.isRefreshing = false
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }

    inner class VerticalSpaceItemDecoration internal constructor(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() { // separation between cards

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}
