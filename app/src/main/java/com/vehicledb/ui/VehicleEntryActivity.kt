package com.vehicledb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vehicledb.R
import com.vehicledb.databinding.ActivityVehicleEntryBinding

class VehicleEntryActivity : AppCompatActivity() {
    var binding : ActivityVehicleEntryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleEntryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initUI()
    }

    private fun initUI() {
        binding?.fabAddVehicle!!.setOnClickListener { onAddVehicleClick() }
    }

    private fun onAddVehicleClick() {
        if(binding?.editVehicleNumber!!.text.isNullOrEmpty()){
            binding?.inputVehicleNumber!!.error = "Please enter valid vehicle number."
            binding?.inputVehicleNumber!!.isErrorEnabled = true
        }else{
            binding?.inputVehicleNumber!!.error = null
            binding?.inputVehicleNumber!!.isErrorEnabled = false
            val intent = Intent(this,VehicleDetailActivity::class.java)
            intent.putExtra("EXTRA_VEHICLE_NUMBER",binding?.editVehicleNumber!!.text.toString())
            startActivity(intent)
            finish()
        }
    }
}