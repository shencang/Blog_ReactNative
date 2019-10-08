package com.shengcangblue.blogrn.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import com.shengcangblue.blogrn.R

class WebBodyActivity : AppCompatActivity() {
    //定义变量
    private var mWebView: WebView? = null
    private val blogUrl = "http://blog.shencangblue.com/"
    private var mExitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_body)
        supportActionBar?.hide()
        initAndSetupView()
    }

    // 初始化对象
    fun initAndSetupView() {
        val webViewContainer = findViewById<FrameLayout>(R.id.webContainer)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        mWebView = WebView(applicationContext)
        webViewContainer.addView(mWebView, params)
        var webSettings = mWebView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true// 设置允许访问文件数据
        webSettings.setSupportZoom(true)//支持缩放
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        mWebView!!.setOnKeyListener(OnKeyEvent)
        mWebView!!.webViewClient = webClient
        mWebView!!.loadUrl(blogUrl)
    }

    private val webClient = object :WebViewClient(){
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
    private val OnKeyEvent = View.OnKeyListener { v, keyCode, event ->
        val action = event.action
        val webView = v as WebView
        if (KeyEvent.ACTION_DOWN == action && KeyEvent.KEYCODE_BACK == keyCode) {
            if (webView?.canGoBack()) {
                webView.goBack()
                return@OnKeyListener true
            }
        }

        false
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            showToast("连按两下退出应用")
            //showToast("连按两下退出应用",Toast.LENGTH_SHORT) //此种方法调用也可以 有点可变参数的意思在里面
            mExitTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        mWebView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView?.clearCache(true)
        (mWebView?.parent as FrameLayout).removeView(mWebView)
        mWebView?.stopLoading()
        mWebView?.setWebViewClient(null)
        mWebView?.setWebChromeClient(null)
        mWebView?.removeAllViews()
        mWebView?.destroy()
        mWebView = null
    }

    //扩展函数
    fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }


}
