package com.example.myapplication05

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.link.*


class Link : AppCompatActivity() {
    var webView: WebView? = null
    var activityList = arrayListOf<ActivityModel>()
    var driveActivityCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.link)
        webView = findViewById<View>(R.id.webview) as WebView
        webView!!.webViewClient = WebViewClient()
        webView!!.loadUrl(Util.FOLDER_LINK);
        var c: Context = this




        var webSettings: WebSettings = webView!!.getSettings();
        webSettings.setJavaScriptEnabled(true);

        btnRefresh.setOnClickListener {
            webView!!.webViewClient = WebViewClient()
            webView!!.loadUrl(Util.FOLDER_LINK);
        }

        btnCopyLink.setOnClickListener {
           Util.CopyText(this,Util.FOLDER_LINK, Util.FOLDER_LINK)
        }



    }



}
