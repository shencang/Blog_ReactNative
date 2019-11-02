package com.shengcangblue.blogrn.activity

import android.content.res.Configuration
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
import com.githang.statusbar.StatusBarCompat
import com.shengcangblue.blogrn.R
import com.shengcangblue.blogrn.util.Constants
import com.shengcangblue.blogrn.util.StatusBarUtil

class WebBodyActivity : AppCompatActivity() {
    private val mTAG  = "WebBodyActivity"
    //定义变量
    private var mWebView: WebView? = null

    private var mExitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_body)
        StatusBarCompat.setStatusBarColor(this,Constants.statusBarColorWhite, true)
      //  supportActionBar?.hide()
        initAndSetupView()
       // initStatusBar()


    }
    private fun changeStatusBarColor(colorId:Int){
        StatusBarCompat.setStatusBarColor(this,colorId, true)
    }

    private fun initStatusBar() {
        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        Log.i("color","color!")
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0)
            Log.i("color","color?")

        }
    }

    // 初始化对象
    private fun initAndSetupView() {
        val webViewContainer = findViewById<FrameLayout>(R.id.webContainer)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        this.mWebView = WebView(applicationContext)
        webViewContainer.addView(mWebView, params)
        val webSettings = mWebView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true// 设置允许访问文件数据
        webSettings.setSupportZoom(true)//支持缩放
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        mWebView!!.setOnKeyListener(OnKeyEvent)
        mWebView!!.webViewClient = webClient
        mWebView!!.loadUrl(Constants.blogUrl)

    }

    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            Log.i("url", url.toString())
            Log.i("urlgetDomain", url?.let { getDomain(it) }.toString())
            if (url?.let { getDomain(it) } == Constants.blogUrlNoHttp) {
               // changeStatusBarColor(Constants.statusBarColorWhite)
                Log.i("urlBlog",url)

            }
            if (url?.let { getDomain(it) } == Constants.dataUrlNoHttp){
                Log.i("urlData",url)
                changeStatusBarColor(Constants.statusBarColorDarkBlue)

            }
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
        mWebView?.webViewClient = null
        mWebView?.webChromeClient = null
        mWebView?.removeAllViews()
        mWebView?.destroy()
        mWebView = null
    }

    //扩展函数
    private fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }

    fun getDomain(url: String): String {
        val result: String
        var j = 0
        var startIndex = 0
        var endIndex = 0
        for (i in url.indices) {
            if (url[i] == '/') {
                j++
                if (j == 2)
                    startIndex = i
                else if (j == 3)
                    endIndex = i
            }

        }
        result = url.substring(startIndex + 1, endIndex)
        return result
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 检测屏幕的方向：纵向或横向
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏， 在此处添加额外的处理代码
            Log.i(mTAG,"Configuration.ORIENTATION_LANDSCAPE-横屏")
        } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //当前为竖屏， 在此处添加额外的处理代码
            Log.i(mTAG,"Configuration.ORIENTATION_PORTRAIT-竖屏")
        }
        //检测实体键盘的状态：推出或者合上
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //实体键盘处于推出状态，在此处添加额外的处理代码
            Log.i(mTAG,"Configuration.HARDKEYBOARDHIDDEN_NO-键盘展开")
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            //实体键盘处于合上状态，在此处添加额外的处理代码
            Log.i(mTAG,"Configuration.HARDKEYBOARDHIDDEN_YES-键盘合上")
        }
    }



}
