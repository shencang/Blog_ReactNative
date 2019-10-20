package com.shengcangblue.blogrn.util

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

class ViewUtil {
    /**
     * @ 获取当前手机屏幕的尺寸(单位:像素)
     */
    private var width: Float = 0.0f
     private var height: Float = 0.0f
     var widthPixel: Float = 0.0f
     var heightPixel: Float = 0.0f
    // 这样可以计算屏幕的物理尺寸
    val pingMuSize: Float
        get() = Math.sqrt((width + height).toDouble()).toFloat()

    val proportion: Float
        get() = (widthPixel / heightPixel)


    internal constructor() {

    }

    internal constructor(mContext: Context) {
        val densityDpi = mContext.resources.displayMetrics.densityDpi
        val scaledDensity = mContext.resources.displayMetrics.scaledDensity
        val density = mContext.resources.displayMetrics.density
        val xdpi = mContext.resources.displayMetrics.xdpi
        val ydpi = mContext.resources.displayMetrics.ydpi
        val widths = mContext.resources.displayMetrics.widthPixels
        val heights = mContext.resources.displayMetrics.heightPixels
        this.width = widths / xdpi * (widths / xdpi)
        this.height = (heights) / ydpi * (widths / xdpi)
        Log.i("viewProportion", this.width.toString())
        Log.i("viewProportion", this.height.toString())
        Log.i("viewProportions", widths.toFloat().toString())
        Log.i("viewProportions", heights.toFloat().toString())
        Log.i("viewProportions",(widthPixel / (heightPixel)).toString() )

    }

    fun dis(windowManager: WindowManager) {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(outMetrics)
        val widthPixel = outMetrics.widthPixels
        val heightPixel = outMetrics.heightPixels
        Log.i("viewProportion", "widthPixel = $widthPixel,heightPixel = $heightPixel")
        //widthPixel = 1440,heightPixel = 2960
        this.widthPixel = widthPixel.toFloat()
        this.heightPixel = heightPixel.toFloat()

    }


}
