package com.tt.ktdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tt.ktdemo.R

class MainActivity : AppCompatActivity() {

    //by lazy 懒加载
    private val recyclerView: RecyclerView? by lazy { findViewById(R.id.main_rv) }

    private val textView: TextView? by lazy { findViewById(R.id.main_tv) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ?是判空安全类型。没有报错
        textView?.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
//                TODO("Not yet implemented")
            }
        })
        //或者简单的写成
        textView?.setOnClickListener {
            //不注释todo报错，，An operation is not implemented: Not yet implemented
            // TODO("Not yet implemented")
        }

        //let，进入设置自己
        textView?.let {
            //表明设置自己
            it.text = "66666"
            if (true) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }
    }
}