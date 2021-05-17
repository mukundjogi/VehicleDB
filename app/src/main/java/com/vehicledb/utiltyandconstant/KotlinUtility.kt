package com.vehicledb.utiltyandconstant

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


fun isConnectedToNetwork(context: Context?): Boolean {
    var hasInternet = false
    val cm: ConnectivityManager? =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        val networks = cm?.allNetworks
        if (networks?.isNotEmpty()!!) {
            for (network in networks) {
                val nc = cm.getNetworkCapabilities(network!!)
                if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet =
                    true
            }
        }
    } else {
        if (cm != null && cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting) {
            hasInternet = true
        }
    }
    return hasInternet
}
