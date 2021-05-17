package com.vehicledb.ui

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vehicledb.databinding.ActivityVehicleDetailBinding
import com.vehicledb.model.VehicleModel
import com.vehicledb.repo.VehicleViewModel

class VehicleDetailActivity : AppCompatActivity() {
    private var REQUESTLIST = 1
    private var binding: ActivityVehicleDetailBinding? = null
    private var vehicleModel: VehicleModel? = null
    private var vehicleViewModel: VehicleViewModel? = null
    private var isEditMode = false
    private var inputType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        title = "Vehicle Details"
        initUI()
    }

    private fun initUI() {
        if (!intent.getStringExtra("EXTRA_VEHICLE_NUMBER").isNullOrEmpty()) {
            binding?.layoutEnterDetails!!.editVehicleNumber.setText(intent.getStringExtra("EXTRA_VEHICLE_NUMBER"))
            binding?.layoutEnterDetails!!.editVehicleNumber.setSelection(binding?.layoutEnterDetails!!.editVehicleNumber.text.length)
            isEditMode = true
        } else if (intent.getSerializableExtra("EXTRA_VEHICLE_MODEL") != null) {
            vehicleModel = intent.getSerializableExtra("EXTRA_VEHICLE_MODEL") as VehicleModel
            isEditMode = false
        }
        vehicleViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(VehicleViewModel::class.java)
        vehicleViewModel?.getDatabaseRepository(this)

        if (isEditMode) {
            binding?.layoutEnterDetails!!.mainEditCard.visibility = View.VISIBLE
            binding?.btnSubmit!!.visibility = View.VISIBLE
            binding?.layoutViewDetails!!.mainCard.visibility = View.GONE
            binding?.layoutEnterDetails!!.editClass.setOnClickListener {
                openCommonList(4)
            }
            binding?.layoutEnterDetails!!.editMadeBy.setOnClickListener {
                when {
                    binding?.layoutEnterDetails!!.editClass.text.isNullOrEmpty() -> {
                        binding?.layoutEnterDetails!!.inputClass.error =
                            "Please select vehicle class"
                        binding?.layoutEnterDetails!!.inputClass.isErrorEnabled = true
                    }
                    else -> {
                        removeAllErrors()
                        openCommonList(0)
                    }
                }
            }
            binding?.layoutEnterDetails!!.editModel.setOnClickListener {
                when {
                    binding?.layoutEnterDetails!!.editClass.text.isNullOrEmpty() -> {
                        binding?.layoutEnterDetails!!.inputClass.error =
                            "Please select vehicle class"
                        binding?.layoutEnterDetails!!.inputClass.isErrorEnabled = true
                    }
                    binding?.layoutEnterDetails!!.editMadeBy.text.isNullOrEmpty() -> {
                        binding?.layoutEnterDetails!!.inputMadeBy.error = "Please select make"
                        binding?.layoutEnterDetails!!.inputMadeBy.isErrorEnabled = true
                    }
                    else -> {
                        removeAllErrors()
                        openCommonList(1)
                    }
                }
            }
            binding?.layoutEnterDetails!!.editFuelType.setOnClickListener {
                openCommonList(2)
            }
            binding?.layoutEnterDetails!!.editTransmission.setOnClickListener {
                openCommonList(3)
            }
            binding?.btnSubmit!!.setOnClickListener {
                onSubmitClick()
            }
        } else if (vehicleModel != null) {
            binding?.layoutEnterDetails!!.mainEditCard.visibility = View.GONE
            binding?.btnSubmit!!.visibility = View.GONE
            binding?.layoutViewDetails!!.mainCard.visibility = View.VISIBLE
            binding?.layoutViewDetails!!.txtVehicleNumber.text = vehicleModel?.vehicleNumber
            binding?.layoutViewDetails!!.txtVehicleName.text = vehicleModel?.modelName+" "+vehicleModel?.fuelType
            binding?.layoutViewDetails!!.txtMadeBy.text = vehicleModel?.madeBy
            binding?.layoutViewDetails!!.txtModel.text = vehicleModel?.modelName
            binding?.layoutViewDetails!!.txtFuelType.text = vehicleModel?.fuelType
            binding?.layoutViewDetails!!.txtTransmission.text = vehicleModel?.transmissionType
        }
    }

    private fun onSubmitClick() {
        if (checkValidation()) {
            if (vehicleModel == null) {
                vehicleModel = VehicleModel()
                vehicleModel?.vehicleId = System.currentTimeMillis().toString()
            } else {
                vehicleModel?.vehicleId = vehicleModel?.vehicleId!!
            }
            vehicleModel?.vehicleNumber =
                binding?.layoutEnterDetails!!.editVehicleNumber.text.toString()
            vehicleModel?.madeBy = binding?.layoutEnterDetails!!.editMadeBy.text.toString()
            vehicleModel?.modelName = binding?.layoutEnterDetails!!.editModel.text.toString()
            vehicleModel?.fuelType =
                binding?.layoutEnterDetails!!.editFuelType.text.toString()
            vehicleModel?.transmissionType =
                binding?.layoutEnterDetails!!.editTransmission.text.toString()
            vehicleViewModel?.insertVehicleDetails(vehicleModel)
            let {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun checkValidation(): Boolean {
        var isValid = false
        removeAllErrors()
        when {
            binding?.layoutEnterDetails!!.editClass.text.isNullOrEmpty() -> {
                binding?.layoutEnterDetails!!.inputClass.error = "Please select vehicle class"
                binding?.layoutEnterDetails!!.inputClass.isErrorEnabled = true
            }
            binding?.layoutEnterDetails!!.editMadeBy.text.isNullOrEmpty() -> {
                binding?.layoutEnterDetails!!.inputMadeBy.error = "Please select make"
                binding?.layoutEnterDetails!!.inputMadeBy.isErrorEnabled = true
            }
            binding?.layoutEnterDetails!!.editModel.text.isNullOrEmpty() -> {
                binding?.layoutEnterDetails!!.inputModel.error = "Please select model"
                binding?.layoutEnterDetails!!.inputModel.isErrorEnabled = true
            }
            binding?.layoutEnterDetails!!.editFuelType.text.isNullOrEmpty() -> {
                binding?.layoutEnterDetails!!.inputFuelType.error = "Please select fuel type"
                binding?.layoutEnterDetails!!.inputFuelType.isErrorEnabled = true
            }
            binding?.layoutEnterDetails!!.editTransmission.text.isNullOrEmpty() -> {
                binding?.layoutEnterDetails!!.inputTransmission.error = "Please select transmission"
                binding?.layoutEnterDetails!!.inputTransmission.isErrorEnabled = true
            }
            else -> {
                isValid = true
            }
        }
        return isValid
    }

    private fun removeAllErrors() {
        binding?.layoutEnterDetails!!.inputClass.error = null
        binding?.layoutEnterDetails!!.inputClass.isErrorEnabled = false
        binding?.layoutEnterDetails!!.inputMadeBy.error = null
        binding?.layoutEnterDetails!!.inputMadeBy.isErrorEnabled = false
        binding?.layoutEnterDetails!!.inputModel.error = null
        binding?.layoutEnterDetails!!.inputModel.isErrorEnabled = false
        binding?.layoutEnterDetails!!.inputFuelType.error = null
        binding?.layoutEnterDetails!!.inputFuelType.isErrorEnabled = false
        binding?.layoutEnterDetails!!.inputTransmission.error = null
        binding?.layoutEnterDetails!!.inputTransmission.isErrorEnabled = false
    }

    private fun openCommonList(type: Int?) {
        inputType = type!!
        val intent = Intent(this, CommonListActivity::class.java)
        intent.putExtra("EXTRA_TYPE", type)
        when (type) {
            1 -> {
                intent.putExtra(
                    "EXTRA_CLASS",
                    binding?.layoutEnterDetails!!.editClass.text.toString()
                )
                intent.putExtra(
                    "EXTRA_MADE_BY",
                    binding?.layoutEnterDetails!!.editMadeBy.text.toString()
                )
            }
            0 -> {
                intent.putExtra(
                    "EXTRA_CLASS",
                    binding?.layoutEnterDetails!!.editClass.text.toString()
                )
            }
        }
        startActivityForResult(intent, REQUESTLIST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && requestCode == REQUESTLIST) {
            when (inputType) {
                4 -> {
                    if (data.getStringExtra("EXTRA_VALUE") != null) {
                        binding?.layoutEnterDetails!!.editClass.setText(
                            data.getStringExtra("EXTRA_VALUE").toString()
                        )
                    }
                }
                3 -> {
                    if (data.getStringExtra("EXTRA_VALUE") != null) {
                        binding?.layoutEnterDetails!!.editTransmission.setText(
                            data.getStringExtra("EXTRA_VALUE").toString()
                        )
                    }
                }
                2 -> {
                    if (data.getStringExtra("EXTRA_VALUE") != null) {
                        binding?.layoutEnterDetails!!.editFuelType.setText(
                            data.getStringExtra("EXTRA_VALUE").toString()
                        )
                    }
                }
                1 -> {
                    if (data.getStringExtra("EXTRA_VALUE") != null) {
                        binding?.layoutEnterDetails!!.editModel.setText(
                            data.getStringExtra("EXTRA_VALUE").toString()
                        )
                    }
                }
                0 -> {
                    if (data.getStringExtra("EXTRA_VALUE") != null) {
                        binding?.layoutEnterDetails!!.editMadeBy.setText(
                            data.getStringExtra(
                                "EXTRA_VALUE"
                            ).toString()
                        )
                    }
                }
            }
        }
    }
}