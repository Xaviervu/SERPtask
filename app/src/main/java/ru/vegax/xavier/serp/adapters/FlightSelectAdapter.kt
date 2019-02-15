package ru.vegax.xavier.serp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import ru.vegax.xavier.serp.R
import ru.vegax.xavier.serp.models.SelectedFlight


class FlightSelectAdapter(private val mContext: Context,
                          private val mSelectedFlights: ArrayList<SelectedFlight>, private val mOnFlightSelectedClickListener: OnFlightSelectedClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    override fun getItemCount(): Int {

        return mSelectedFlights.size
    }


    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly create ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_view_layout, parent, false))
    }

    private var mLastCheckedRB: RadioButton? = null

    private var mLastPosition: Int? = null

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder   The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolder = holder as ViewHolder
        val currentItem = mSelectedFlights.toTypedArray()[position]
        viewHolder.bindTo(currentItem)
        //Get current item

        viewHolder.rbCompany.setOnClickListener {
            handleClick(viewHolder, position)
        }
        viewHolder.holderLayout.setOnClickListener {
            handleClick(viewHolder, position)
        }
        viewHolder.rbCompany.tag = currentItem

    }

    private fun handleClick(viewHolder: ViewHolder, position: Int) {
        viewHolder.rbCompany.isChecked = true
        if (mLastCheckedRB != null && mLastCheckedRB != viewHolder.rbCompany) {
            mLastCheckedRB?.isChecked = false
        }
        mLastPosition = position
        mLastCheckedRB = viewHolder.rbCompany
        mOnFlightSelectedClickListener.onFlightSelectedClick(position)
    }


    /**
     * ViewHolder class that represents each row of data in the RecyclerView
     */
    internal inner class ViewHolder
    /**
     * Constructor for the ViewHolder, used in onCreateViewHolder().
     *
     * @param itemView The rootview of the list_item_input_input.xml layout file
     */
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rbCompany: RadioButton = itemView.findViewById(R.id.rBCompany)

        private val txtVPrice: TextView = itemView.findViewById(R.id.txtVPriceTotal)
        val holderLayout : LinearLayout = itemView.findViewById(R.id.holderLayout)

        fun bindTo(currentItem: SelectedFlight) {
            rbCompany.text = currentItem.company

            txtVPrice.text = mContext.resources.getString(R.string.price, "",currentItem.price)
        }

    }

    interface OnFlightSelectedClickListener {
        fun onFlightSelectedClick(position: Int)
    }
}
