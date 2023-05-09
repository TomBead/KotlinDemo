package com.tt.ktdemo.function.room

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.ktdemo.app.AppApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RoomModel : ViewModel() {
    // Add ViewModel logic here
    private val TAG: String = "RoomModel";
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
            mLiveData?.value = "666"
        }, 2000)
    }

    fun add() {
        viewModelScope.launch {
            //随机创建一个random
            val random = (1..10).random()
            //先查询再插入
            val hotDog = UserDataBase.get(AppApplication.instance()).userDao().findById(random)
            hotDog?.let {
                Log.d("RoomModel", "已经有一条相同的数据啦")
            } ?: randomInsert(random)

            //先查询,如果没这条数据 就插入，有的话打印数据

        }
    }

    /**
     * 删除数据
     */
    fun deleteAllData() {
        viewModelScope.launch {
            //随机取一条数据
            val random = (1..10).random()
            //查询数据库是否有此条数据
            val user = UserDataBase.get(AppApplication.instance()).userDao().findById(random)
            user?.let {
                UserDataBase.get(AppApplication.instance()).userDao().delete(user)
                Log.d("RoomModel", "删除某条数据成功")
            } ?: randomInsert(random)
        }
    }

    /**
     * 随机更新某条User数据
     */
    fun updateRandomData() {
        //先查询
        val random = (1..10).random()

        viewModelScope.launch {
            val user = UserDataBase.get(AppApplication.instance()).userDao().findById(random)
            user?.let {
                //如果不为空，则更新
                UserDataBase.get(AppApplication.instance()).userDao()
                    .updateUser(User(random, "我是更新整个user后的数据", "男"))
                Log.d(TAG, "更新整个user数据成功")
            } ?: randomInsert(random)
        }
    }


    /**
     * 随机单独更新某个字段
     */
    fun updateSingleData() {
        //先查询
        val random = (1..10).random()
        //
        viewModelScope.launch {
            val user = UserDataBase.get(AppApplication.instance()).userDao().findById(random)
            user?.let {
                UserDataBase.get(AppApplication.instance()).userDao()
                    .updateSingleName(UserName(random, "我是单独更新后的数据"))
                Log.d(TAG, "更新单独数据成功")
            } ?: randomInsert(random)
            log("====线程：" + Thread.currentThread())
        }
        viewModelScope.launch(Dispatchers.IO) {
            //执行耗时代码，比如网络请求
            val user = UserDataBase.get(AppApplication.instance()).userDao().findById(random)
            user?.let {
                UserDataBase.get(AppApplication.instance()).userDao()
                    .updateSingleName(UserName(random, "我是单独更新后的数据"))
                Log.d(TAG, "更新单独数据成功")
            } ?: randomInsert(random)
            log("====线程：" + Thread.currentThread())
        }

    }


    /**
     * 随机插入某条数据，方便测试
     */
    private fun randomInsert(random: Int) {
        val mD = User(random, "热狗先生$random", "女")
        UserDataBase.get(AppApplication.instance()).userDao().insertAll(mD)

        Log.d(TAG, "插入数据成功")
    }

    private fun log(msg: String) {
        Log.d("MyViewModel", msg)
    }
}