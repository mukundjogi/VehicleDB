package com.vehicledb.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.vehicledb.databinding.ActivityDashboardBinding
import com.vehicledb.model.VehicleModel
import com.vehicledb.repo.VehicleViewModel
import com.vehicledb.utiltyandconstant.isConnectedToNetwork
import java.io.Serializable

class DashboardActivity : AppCompatActivity() {
    private val REQUEST_UPDATE = 10
    private var binding: ActivityDashboardBinding? = null
    private var vehicleViewModel: VehicleViewModel? = null
    private var vehicleList: ArrayList<VehicleModel>? = null
    private var vehicleListAdapter: VehicleListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        title = "Vehicle List"
        initUI()
    }

    private fun initUI() {
        binding?.recyclerView!!.layoutManager = LinearLayoutManager(this)
        binding?.recyclerView!!.isNestedScrollingEnabled = false
        binding?.recyclerView!!.setPadding(0,0,0,200)
        binding?.fabAddVehicle!!.setOnClickListener { onAddVehicleClick() }
        vehicleViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(VehicleViewModel::class.java)
        vehicleViewModel?.getDatabaseRepository(this)
        getVehicleList()
    }

    private fun onAddVehicleClick() {
        val intent = Intent(this,VehicleEntryActivity::class.java)
        startActivity(intent)
    }

    private fun getVehicleList() {
        vehicleViewModel?.getVehicleList()?.observe(this) { vehicleDataList ->
            if (vehicleDataList.isNullOrEmpty()) {
                binding?.layoutError?.txtErrorName?.text = "No vehicle found!!"
                binding?.layoutError?.layout?.visibility = View.VISIBLE
                binding?.layoutError?.btnTryAgain?.visibility = View.GONE
                binding?.layoutError?.txtErrorDetail?.visibility = View.GONE
            } else {
                vehicleList = vehicleDataList as ArrayList<VehicleModel>?
                binding?.layoutError?.layout?.visibility = View.GONE
                vehicleListAdapter = VehicleListAdapter(this,vehicleList!!)
                binding?.recyclerView!!.adapter = vehicleListAdapter
            }
        }
        binding?.recyclerView!!.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if(!adapter?.data.isNullOrEmpty() && position<adapter?.data?.size!!) {
                    val intent = Intent(this@DashboardActivity, VehicleDetailActivity::class.java)
                    intent.putExtra("EXTRA_VEHICLE_MODEL", vehicleList?.get(position) as Serializable)
                    startActivityForResult(intent, REQUEST_UPDATE)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            getVehicleList()
        }
    }
}