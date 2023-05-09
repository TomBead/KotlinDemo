package com.tt.ktdemo.base

import android.util.Log

/**
 * obj 静态类
 */
object Utlis {
    private const val TAG = "utlis"

    /***
     * 调用
     * utlis。a（）；
     */
    fun a() {
        Log.e(TAG, "此时 object 表示 声明静态内部类")
    }
}