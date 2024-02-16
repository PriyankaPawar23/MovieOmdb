package com.priyanka.movieomdb

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast

fun Context.showProgressDialog(): ProgressDialog {
    val progressDialog = ProgressDialog(this)
    progressDialog.setMessage("Please wait...")
    progressDialog.setCancelable(false)
    progressDialog.setCanceledOnTouchOutside(false)
    progressDialog.show()
    return progressDialog
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun isInternetAvailable(context: Context): Boolean {
    (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return this.getNetworkCapabilities(this.activeNetwork)?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) ?: false
        } else {
            (@Suppress("Deprecation")
            return this.activeNetworkInfo?.isConnected ?: false)

        }
    }
}