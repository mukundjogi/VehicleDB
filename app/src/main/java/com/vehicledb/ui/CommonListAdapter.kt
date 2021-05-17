package com.vehicledb.ui

import android.content.Context
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.vehicledb.R
import com.vehicledb.model.VehicleModel

class CommonListAdapter(context: Context?, mData: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.raw_vehicle_list, mData) {
    val context: Context = context!!
    var index = -1
    override fun convert(holder: BaseViewHolder?, vehicleModel: String?) {
        if (vehicleModel != null) {
            holder?.setGone(R.id.txtVehicleNumber, false)
            holder?.setGone(R.id.imgSelected, index == holder.layoutPosition)
            holder?.setText(
                R.id.txtModelName,
                if (vehicleModel!!.isNullOrEmpty()) context.getString(R.string.not_available) else vehicleModel!!
            )
        }
    }
}