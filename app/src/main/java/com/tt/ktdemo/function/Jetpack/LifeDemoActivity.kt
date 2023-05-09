package com.tt.ktdemo.function.Jetpack

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tt.ktdemo.R
import com.tt.ktdemo.function.room.RoomModel
import java.util.*

/**
 *
 */
class LifeDemoActivity : AppCompatActivity() {
    private val mTextView: TextView? by lazy { findViewById(R.id.life_test) }

    private lateinit var viewModel: MyViewModel

    private lateinit var roomModel: RoomModel;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life)

        // Get a reference to the ViewModel
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        // Observe a LiveData object in the ViewModel
        viewModel.getLoadingLiveData()?.observe(this, androidx.lifecycle.Observer {
            mTextView?.text = it;
        })

        viewModel.getUserInfo();


        //
        roomModel = ViewModelProvider(this).get(RoomModel::class.java)
        roomModel.updateSingleData()
    }

    override fun onStart() {
        super.onStart()
        // 获取LifecycleOwner对象LifecycleOwner lifecycleOwner = this;
        // 将Timer实例添加为Observer
        // 获取LifecycleOwner对象LifecycleOwner lifecycleOwner = this;
        // 将Timer实例添加为Observer
        lifecycle.addObserver(Timercycle())
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

