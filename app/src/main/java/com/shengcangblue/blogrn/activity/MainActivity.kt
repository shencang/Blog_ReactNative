package com.shengcangblue.blogrn.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.WindowManager
import com.shengcangblue.blogrn.R

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