package com.shengcangblue.blogrn.activity

import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shengcangblue.blogrn.R
import com.shengcangblue.blogrn.util.Constants
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)


        // Get the web view settings instance
        val settings = aboutWebView.settings
        // Enable java script in web view
        settings.javaScriptEnabled = true
        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)
        settings.setSupportZoom(false)
        // Enable zooming in web view
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true

        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true
        }
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.setGeolocationEnabled(true)
        settings.allowFileAccess = true
        //webview setting
        aboutWebView.fitsSystemWindows = true
        /* if SDK version is greater of 19 then activate hardware acceleration
        otherwise activate software acceleration  */
        aboutWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        aboutWebView.loadUrl(Constants.dataUrl)
        // Set web view client
        aboutWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Page loading started
                // Do something
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Page loading finished
                // Enable disable back forward button
            }
        }

    }



    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.btn_save -> {
//                println("onClick" + btn_save.text)
//            }
//            R.id.btn_cancel -> {
//                println("onclick" + btn_cancel.text)
//            }
//
//        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {

        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (aboutWebView.canGoBack()) {
            // If web view have back history, then go to the web view back history
            aboutWebView.goBack()
        }
    }




}
