package com.vehicledb.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.vehicledb.databinding.ActivityCommonListBinding
import com.vehicledb.repo.VehicleViewModel
import com.vehicledb.utiltyandconstant.isConnectedToNetwork
import kotlinx.android.synthetic.main.activity_common_list.*

class CommonListActivity : AppCompatActivity() {
    private var binding: ActivityCommonListBinding? = null
    private var vehicleViewModel: VehicleViewModel? = null
    private var type = 0
    private var index = -1
    private var vehicleClass = ""
    private var madeBy = ""
    private var commonListAdapter: CommonListAdapter? = null
    private var list: java.util.ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonListBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        initUI()
    }

    private fun initUI() {
        vehicleViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(VehicleViewModel::class.java)
        vehicleViewModel?.getDatabaseRepository(this)
        vehicleViewModel?.getRepository()
        binding?.recyclerView!!.layoutManager = LinearLayoutManager(this)
        binding?.recyclerView!!.isNestedScrollingEnabled = false
        type = intent.getIntExtra("EXTRA_TYPE", 0)
        binding?.recyclerView!!.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View?,
                position: Int
            ) {
                if (commonListAdapter != null && position < commonListAdapter?.data!!.size) {
                    index = position
                    commonListAdapter?.index = position
                    commonListAdapter?.notifyDataSetChanged()
                    binding?.btnSubmit!!.visibility = View.VISIBLE
                }
            }
        })
        binding?.btnSubmit!!.setOnClickListener {
            val intent = Intent()
            intent.putExtra("EXTRA_VALUE", commonListAdapter?.data!!.get(index))
            setResult(RESULT_OK, intent)
            finish()
        }
        when (type) {
            4 -> {
                title = "Select Vehicle Class"
                setVehicleClassView()
            }
            3 -> {
                title = "Select Transmission"
                setTransmissionView()
            }
            2 -> {
                title = "Select Fuel Type"
                setFuelType()
            }
            1 -> {
                title = "Select Model"
                vehicleClass = intent.getStringExtra("EXTRA_CLASS")!!
                madeBy = intent.getStringExtra("EXTRA_MADE_BY")!!
                setModelView()
            }
            0 -> {
                title = "Select Make"
                vehicleClass = intent.getStringExtra("EXTRA_CLASS")!!
                setMadeByView()
            }
        }
    }

    private fun setVehicleClassView() {
        list = ArrayList()
        list?.add("2w")
        list?.add("4w")
        setAdapterData(list)
    }

    private fun setMadeByView() {
        if (isConnectedToNetwork(this)) {
            setInternetAvailable(true)
            vehicleViewModel?.getVehicleMadeBy(vehicleClass)!!.observe(this) { madeByResponse ->
                if (!madeByResponse.isNullOrEmpty()) {
                    list = madeByResponse
                    setAdapterData(list)
                }
            }
        } else {
            setInternetAvailable(false)
        }
    }

    private fun setModelView() {
        if (isConnectedToNetwork(this)) {
            setInternetAvailable(true)
            vehicleViewModel?.getVehicleModels(vehicleClass, madeBy)!!
                .observe(this) { modelResponse ->
                    if (!modelResponse.isNullOrEmpty()) {
                        list = modelResponse
                        setAdapterData(list)
                    }
                }
        } else {
            setInternetAvailable(false)
        }
    }

    private fun setFuelType() {
        list = ArrayList()
        list?.add("Petrol")
        list?.add("Diesel")
        list?.add("CNG")
        list?.add("Petrol+CNG")
        list?.add("Electric")
        list?.add("Hybrid")
        setAdapterData(list)
    }

    private fun setTransmissionView() {
        list = ArrayList()
        list?.add("Manual")
        list?.add("Automatic")
        setAdapterData(list)
    }

    private fun setAdapterData(list: ArrayList<String>?) {
        Log.e("List = ", list?.size.toString())
        if (list != null) {
            commonListAdapter = CommonListAdapter(this, list!!)
            recyclerView.adapter = commonListAdapter
            commonListAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setInternetAvailable(isAvailable: Boolean) {
        if (isAvailable) {
            binding?.layoutError?.layout?.visibility = View.GONE
            binding?.layoutError?.btnTryAgain?.visibility = View.GONE
            binding?.layoutError?.txtErrorDetail?.visibility = View.GONE
        } else {
            binding?.layoutError?.txtErrorName?.text = "You are not connected to network!!"
            binding?.layoutError?.txtErrorDetail?.text =
                "If your are connected to network please click below."
            binding?.layoutError?.layout?.visibility = View.VISIBLE
            binding?.layoutError?.btnTryAgain?.visibility = View.VISIBLE
            binding?.layoutError?.txtErrorDetail?.visibility = View.VISIBLE
            binding?.layoutError?.btnTryAgain?.setOnClickListener {
                initUI()
            }
        }
    }
}