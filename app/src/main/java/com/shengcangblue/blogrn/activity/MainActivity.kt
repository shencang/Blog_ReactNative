package com.shengcangblue.blogrn.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.renderscript.Allocation
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.shengcangblue.blogrn.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mHandler = SplashHandle(this)

    // 将常量放入这里
    companion object {

        // 正常跳转到登录界面 常量 防止以后增加业务逻辑
        const val MSG_LAUNCH : Int = 0

        // 延时时间
        const val SLEEP_TIME = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initView()

    }
    private fun initView(){
        splashImage!!.findViewById<View>(R.id.splashImage)
        var viewProportion = ViewUtil(this)
        var proportion :Float = viewProportion.proportion
        if (proportion<=((9/16)*1.1)&&proportion>=(9/16)*0.9){
            splashImage!!.setImageResource(R.mipmap.spl16d5a9)
            Log.i("viewProportion","9/16")
        }else if (proportion<=((9/18)*1.1)&&proportion>=(9/18)*0.9){
            splashImage!!.setImageResource(R.mipmap.spl18a9)
            Log.i("viewProportion","9/18")
        }else if (proportion<=((9/19)*1.1)&&proportion>=(9/19)*0.9){
            splashImage!!.setImageResource(R.mipmap.spl19d5a9)
            Log.i("viewProportion","9/19")
        } else if (proportion<=((9/19.5)*1.1)&&proportion>=(9/19.5)*0.9){
            splashImage!!.setImageResource(R.mipmap.spl19d5a9)
            Log.i("viewProportion","9/19.5")
        }else if (proportion<=((9/20.5)*1.1)&&proportion>=(9/20.5)*0.9){
            splashImage!!.setImageResource(R.mipmap.spl20d5a9)
            Log.i("viewProportion","9/20.5")
        }else if (proportion<=((9/21.5)*1.1)&&proportion>=(9/21.5)*0.9){
            splashImage!!.setImageResource(R.mipmap.spl21d5a9)
            Log.i("viewProportion","9/21.5")
        }
    }

    override fun onResume() {
        super.onResume()
        val start = System.currentTimeMillis()

        /*
        这里计算了两个时间
        两个时间间可以放入判断条件：是否需要自动登录等
         */

        val costTime = System.currentTimeMillis() - start

        val left = SLEEP_TIME - costTime

        // kotlin中取消了java中的三目运算，换成if...else...
        mHandler.postDelayed(runnable, if (left > 0) left else 0)
    }



    fun startBlog(){
        startActivity(Intent(this, WebBodyActivity::class.java))
    }

    private val runnable = Runnable {
        run {
            val message = mHandler.obtainMessage(MSG_LAUNCH)
            mHandler.sendMessage(message)
        }
    }



    // 弱引用handler内部类
    private class SplashHandle(cls : MainActivity) : UIHandler<MainActivity>(cls) {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity = ref?.get()
            if (null != activity){

                if (activity.isFinishing)
                    return

                when(msg?.what){

                    // 正常跳转到登录界面
                    MSG_LAUNCH -> {
                        activity.startActivity(Intent(activity, WebBodyActivity::class.java))
                        activity.finish()
                    }
                }
            }
        }
    }

}