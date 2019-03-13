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
                          private val mSelectedFlights: List<SelectedFlight>, private val mOnFlightSelectedClickListener: OnFlightSelectedClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {

        return mSelectedFlights.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_view_layout, parent, false))
    }

    private var mLastCheckedRB: RadioButton? = null

    private var mLastPosition: Int? = null

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


    internal inner class ViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rbCompany: RadioButton = itemView.findViewById(R.id.rBCompany)

        private val txtVPrice: TextView = itemView.findViewById(R.id.txtVPriceTotal)
        val holderLayout: LinearLayout = itemView.findViewById(R.id.holderLayout)

        fun bindTo(currentItem: SelectedFlight) {
            rbCompany.text = currentItem.company.name

            txtVPrice.text = mContext.resources.getString(R.string.price, "", currentItem.price)
        }
    }

    interface OnFlightSelectedClickListener {
        fun onFlightSelectedClick(position: Int)
    }
}
