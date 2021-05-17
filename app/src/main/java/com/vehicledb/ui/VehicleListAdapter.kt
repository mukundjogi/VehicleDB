package com.vehicledb.ui

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.vehicledb.R
import com.vehicledb.model.VehicleModel

class VehicleListAdapter(context: Context?, mData: ArrayList<VehicleModel>) :
    BaseQuickAdapter<VehicleModel, BaseViewHolder>(R.layout.raw_vehicle_list, mData) {
    val context: Context = context!!
    override fun convert(holder: BaseViewHolder?, vehicleModel: VehicleModel?) {
        if (vehicleModel != null) {
            holder?.setText(
                R.id.txtVehicleNumber,
                if (vehicleModel?.vehicleNumber!!.isNullOrEmpty()) context.getString(R.string.not_available) else vehicleModel?.vehicleNumber!!
            )
            holder?.setText(
                R.id.txtModelName,
                if (vehicleModel?.modelName!!.isNullOrEmpty()) context.getString(R.string.not_available) else vehicleModel?.madeBy!!+" "+vehicleModel?.modelName!!
            )
        }
    }
}