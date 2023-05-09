package com.tt.ktdemo.function.Jetpack

import android.os.Handler
import android.util.Log
import androidx.lifecycle.*

/***
 * 跟随生命周期启动暂停
 */
class Timercycle : DefaultLifecycleObserver {
    private var handler: Handler? = null
    private var seconds = 0

    /**
     * activity start 的时候调用
     */
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        handler = Handler()
        handler!!.post(object : Runnable {
            override fun run() {
                Log.d("Timer", "Seconds: $seconds")
                seconds++
                handler!!.postDelayed(this, 1000)
            }
        })
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

}