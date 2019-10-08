package com.shengcangblue.blogrn.activity

import android.os.Handler
import java.lang.ref.WeakReference

open class UIHandler<T>(cls:T): Handler() {

    var ref: WeakReference<T>? = null

    init {
        ref = WeakReference(cls)
    }

    fun getRef(): T? {
        return if (ref != null) ref!!.get() else null
    }
}
