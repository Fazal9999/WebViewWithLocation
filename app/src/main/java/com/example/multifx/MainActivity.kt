package com.example.multifx

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var context: Context
    lateinit var activity: Activity
    lateinit var progDailog: ProgressDialog
   lateinit var locationManager:LocationManager
    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    //Defining constant request code, add in your activity class
    private val REQUEST_CHECK_SETTINGS = 111


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        statusCheck()
        webView = findViewById(R.id.webView)
        val url = "https://maxelint.com/"
        activity = this;
        context=this
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }
        }
        webView.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath());
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                progDailog.show()
                view.loadUrl(url)
                return true
            }
            override fun onPageFinished(view: WebView, url: String) {
                progDailog.dismiss()
            }
        }
        webView.loadUrl(url)
    }

    fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        if(!isGpsEnabled()) {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true) //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS


            val task =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
            task.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(ApiException::class.java)
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                val resolvable = exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    this@MainActivity,
                                    REQUEST_CHECK_SETTINGS
                                )
                            } catch (e: SendIntentException) {
                                // Ignore the error.
                            } catch (e: ClassCastException) {
                                // Ignore, should be an impossible error.
                            }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            }

        }
        else{
            Toast.makeText(getApplicationContext(), "GPS is already Enabled!", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val states = LocationSettingsStates.fromIntent(data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK ->
                    // All required changes were successfully made

                    Toast.makeText(
                        applicationContext,
                        "User has clicked on OK - So GPS is on",
                        Toast.LENGTH_SHORT
                    ).show()
                RESULT_CANCELED ->closeApp()
                // The user was asked to change settings, but chose not to


                else -> {

                }
            }
        }
    }

    private fun closeApp() {
        Toast.makeText(
            applicationContext,
            "User has clicked on NO, THANKS - So GPS is still off.",
            Toast.LENGTH_SHORT
        ).show()
        this.finishAffinity();
        System.exit(0)
    }
}




