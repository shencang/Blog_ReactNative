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
import com.shengcangblue.blogrn.util.StatusBarUtil

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
        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, R.color.colorPrimary)
        }
    }

    // 初始化对象
    fun initAndSetupView() {
        val webViewContainer = findViewById<FrameLayout>(R.id.webContainer)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        this.mWebView = WebView(applicationContext)
        webViewContainer.addView(mWebView, params)
        val webSettings = mWebView!!.settings
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
            if (webView.canGoBack()) {
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
