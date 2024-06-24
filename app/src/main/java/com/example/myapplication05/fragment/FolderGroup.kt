package com.example.myapplication05.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.myapplication05.R

import kotlinx.android.synthetic.main.fragment_folder_group.view.*
import kotlinx.android.synthetic.main.fragment_picture.view.*

class FolderGroup : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        Log.e("PIC7000", "HEloo12222222")

        viewfolder = inflater.inflate(R.layout.fragment_folder_group, container, false)
        containerGlobal = container
        var context = container!!.context

        webView = viewfolder!!.findViewById<View>(R.id.webviewGroupFolder) as WebView
        var webSettings: WebSettings = webView!!.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return viewfolder
    }



    companion object {
        var viewfolder: View? = null
        var containerGlobal: ViewGroup? = null
        var webView: WebView? = null
    }
}
