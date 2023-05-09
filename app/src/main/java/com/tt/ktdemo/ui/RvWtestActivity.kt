package com.tt.ktdemo.ui;


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tt.ktdemo.R


/***
 * weiview例子，还需要完善，
 */
class RvWtestActivity : AppCompatActivity() {

    private val rv1: RecyclerView? by lazy { findViewById(R.id.rv_test) }

    lateinit var adapter: ListAdapter;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rvtest)
        inti();
    }


    fun inti() {
        getSupportActionBar()?.hide();
        intirvPageRv();
    }

//

    fun intirvPageRv() {
        val poiList: MutableList<String> = mutableListOf();
        for (i in 0..10000) {
            poiList.add("item $i 可就不是v离开识别率布兰卡吧v不离开你四点九八v科技部打开v就不是人拿了金牌女款基本上口诀工人被困v不是三百v科技博览会本科生")
        }
        adapter = ListAdapter(null);
        adapter.setNewInstance(poiList);
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv1!!.layoutManager = layoutManager
        rv1!!.adapter = adapter;
    }


}