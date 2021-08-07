package com.example.multifx.gps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GpsLocationReceiver : BroadcastReceiver() {
    lateinit var locationManager: LocationManager
    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    override fun onReceive(context: Context, intent: Intent) {
        locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        if (intent.action!!.matches("android.location.PROVIDERS_CHANGED")) {
            Toast.makeText(context, "GPS STATUS CHANGED",
                    Toast.LENGTH_SHORT).show()
            // Intent pushIntent = new Intent(context, LocalService.class);
            // context.startService(pushIntent);
            if(!isGpsEnabled()) {
                Toast.makeText(context, "GPS has turned off, Closing app",
                        Toast.LENGTH_SHORT).show()
                System.exit(0)
            }
        }
    }
}

private fun String.matches(regex: String): Boolean {
return true
}
