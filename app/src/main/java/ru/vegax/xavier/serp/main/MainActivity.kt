package ru.vegax.xavier.serp.main


import android.arch.lifecycle.Observer
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.vegax.xavier.serp.R
import ru.vegax.xavier.serp.adapters.HotelAdapter
import ru.vegax.xavier.serp.layout.CenterZoomLayoutManager
import ru.vegax.xavier.serp.main.MainActivityViewModel.Companion.NO_FILTER
import ru.vegax.xavier.serp.main.MainActivityViewModel.LoadingStatus.LOADING
import ru.vegax.xavier.serp.main.MainActivityViewModel.LoadingStatus.NOT_LOADING
import ru.vegax.xavier.serp.models.Company
import ru.vegax.xavier.serp.models.Flight
import ru.vegax.xavier.serp.models.Hotel
import ru.vegax.xavier.serp.models.SelectedFlight

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
        FlightSelectFragment.FragmentInteractionListener, HotelAdapter.OnAdapterClickListener {
    private val TAG = "XavvMainActivity"

    private val viewModel by lazy {
        viewModel { MainActivityViewModel() }
    }

    lateinit var hotelAdapter: HotelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "on Create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipe_container.setOnRefreshListener(this)
        initRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.adapterData.observe(this, Observer {
            if (it != null) {
                hotelAdapter.setDataLists(it)

            }
        })
        viewModel.loadingStatus.observe(this, Observer {
            when (it) {
                LOADING -> {
                    swipe_container.isRefreshing = true
                }
                NOT_LOADING -> {
                    initSpinner()
                    swipe_container.isRefreshing = false
                }
            }
        })

        viewModel.loadErr.observe(this, Observer {
            swipe_container.isRefreshing = false
            toast(getString(R.string.cannot_donwload))
        })
    }

    private fun initRecyclerView() {
        val layoutManager = CenterZoomLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerVUsers.layoutManager = layoutManager
        hotelAdapter = HotelAdapter(this, this)
        recyclerVUsers.adapter = hotelAdapter
        recyclerVUsers.addItemDecoration(VerticalSpaceItemDecoration(40))
        recyclerVUsers.setHasFixedSize(true)
        if (hotelAdapter.mHotelsData.size == 0) {
            onRefresh()
        }
    }

    private fun initSpinner() {
        val spinnerList = ArrayList<String>()
        val companyIdList = ArrayList<Int>()
        spinnerList.add(getString(R.string.filter)) // first word means no filtration
        companyIdList.add(NO_FILTER)

        val companiesList = viewModel.detailsModel?.companies
        if (companiesList != null) {
            companyIdList.addAll(companiesList.map { company -> company.id })
            spinnerList.addAll(companiesList.map { company -> company.name ?: "" })
        }

        val myAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.custom_spinner_item,
                spinnerList)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = myAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                viewModel.getHotelResults(companyIdList[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
    }

    override fun onRefresh() {
        viewModel.loadData(NO_FILTER)
    }

    override fun onFragmentResult(currHotel: Hotel, selectedFlight: SelectedFlight) {
            Toast.makeText(this@MainActivity, "Выбран отель \"${currHotel.name}\" через ${selectedFlight.company.name} за ${selectedFlight.price}р", Toast.LENGTH_SHORT).show()
    }


    override fun onClick(position: Int) {
        val deviceSelect = FlightSelectFragment().newInstance(viewModel.adapterData.value!![position].hotel,
                viewModel.adapterData.value!![position].flights, viewModel.detailsModel?.companies as ArrayList<Company>)

        deviceSelect.show(supportFragmentManager, "selectDialog")
    }

    inner class VerticalSpaceItemDecoration internal constructor(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() { // separation between cards

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }

    }
}
