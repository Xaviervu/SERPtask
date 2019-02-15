package ru.vegax.xavier.serp.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import ru.vegax.xavier.serp.R


class HotelAdapter(private val mContext: Context, private val mOnAdapterClickListener: OnAdapterClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val mHotelsData: ArrayList<AdapterData> = ArrayList()
    private val TAG = "XavvHotelAdapter"

    override fun getItemCount(): Int {
        val size = mHotelsData.size
        Log.d(TAG, "count $size")
        return size

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolderHotels(LayoutInflater.from(mContext).inflate(R.layout.list_item_hotels, parent, false))


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolderUsers = holder as ViewHolderHotels
        //Get current item
        val currentItem = mHotelsData[position]

        viewHolderUsers.bindTo(currentItem)


        val textView = viewHolderUsers.txtVHotel
        //disable swipe for the switch, only click on the item will trigger the output

        textView.tag = position

        holder.cardView.setOnClickListener {
            mOnAdapterClickListener.onClick(position)
        }
    }

    fun setDataLists(hotelsData: ArrayList<AdapterData>) {

        mHotelsData.clear()

        mHotelsData.addAll(hotelsData)

        this.notifyDataSetChanged()
    }


    internal inner class ViewHolderHotels(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtVHotel: TextView = itemView.findViewById(R.id.txtVHotel)
        private val txtVOptions: TextView = itemView.findViewById(R.id.txtVOptions)
        private val txtVPrice: TextView = itemView.findViewById(R.id.txtVPrice)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bindTo(currentItem: AdapterData?) {
            if (currentItem != null) {
                txtVHotel.text = mContext.getResources().getString(R.string.hotel, currentItem.hotel.name)

                var rusSuffix = ""
                var fromText = ""
                val size = currentItem.options


                val lstDigit = lastDigit(currentItem.hotel.flightIds?.size!!)
                when (lstDigit) {
                    1 -> rusSuffix = ""
                    2, 3, 4 -> rusSuffix = mContext.getString(R.string.two2fourSuffixRus)
                    5 - 9, 0 -> rusSuffix = mContext.getString(R.string.manySuffixRus)
                }
                if (size > 1) {
                    fromText = mContext.resources.getString(R.string.from)
                }

                txtVOptions.text = mContext.resources.getString(R.string.flight_option, currentItem.options, rusSuffix)

                txtVPrice.text = mContext.resources.getString(R.string.price, fromText,currentItem.lowestPrice)
            }
        }

        private fun lastDigit(number: Int): Int {
            return number % 10
        }
    }

    interface OnAdapterClickListener {
        fun onClick(position: Int)
    }
}
