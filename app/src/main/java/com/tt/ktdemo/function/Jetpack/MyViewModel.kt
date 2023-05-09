package com.tt.ktdemo.function.Jetpack

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyViewModel : ViewModel() {
    // Add ViewModel logic here

    private var mLiveData: MutableLiveData<String>? = null

    init {
        mLiveData = MutableLiveData<String>();
    }


    fun getLoadingLiveData(): LiveData<String>? {
        return mLiveData;
    }

    fun getUserInfo() {
        log("=========getUserInfo")
        Handler().postDelayed({
            mLiveData?.value ="666"
        }, 2000)
    }

    private fun log(msg: String) {
        Log.d("MyViewModel", msg)
    }
}