package com.example.multifx

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    lateinit var activity: Activity
    lateinit var progDailog: ProgressDialog
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        val url = "https://maxelint.com/"
        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
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
    }}




