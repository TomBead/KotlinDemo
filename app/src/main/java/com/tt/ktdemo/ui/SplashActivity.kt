package com.tt.ktdemo.ui

import android.animation.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.tt.ktdemo.R
import com.tt.ktdemo.function.room.UserDataBase
import java.util.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-KotlinAppActivity
 *    time   : 2018/10/18
 *    desc   : 闪屏界面
 */
class SplashActivity : AppCompatActivity() {
    private val imageView: ImageView? by lazy { findViewById(R.id.start_img) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


    }

    override fun onBackPressed() {
        // 禁用返回键
        //super.onBackPressed();
    }


    override fun onDestroy() {
        // 因为修复了一个启动页被重复启动的问题，所以有可能 Activity 还没有初始化完成就已经销毁了
        // 所以如果需要在此处释放对象资源需要先对这个对象进行判空，否则可能会导致空指针异常
        super.onDestroy()
    }


}