package com.tt.ktdemo.base

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.tt.ktdemo.function.map.PoiBean

/***
 * 数据结构
 */
//静态常量
const val TEXT: String = "TEXT"

class DataBean {
    //val== final
    val aData: String = ""

    companion object {
        //静态常量
        const val TEXT: String = ""

        //静态变量,加这个@JvmField ，在java代码里面就可以在像在kt里面一样访问，否则不能
        @JvmField
        var errorCode = "ssssss"
    }
    //变量
//    var i: Int = 1
//    var l: Long = 2
//    var b: Boolean = true
//    var f: Float = 0F
//    var d: Double = 0.0
//    var c: Char = 'A'
//    var s: String = "text"

    // 更简洁点可以这样，自动推导类型
    var i = 1
    var l = 2
    var b = true
    var f = 0F
    var d = 0.0
    var c = 'A'
    var s = "text"

    //数组
    val array1 = intArrayOf(1, 2, 3)
    val array2 = floatArrayOf(1f, 2f, 3f)
    val array3 = arrayListOf("1", "2", "3")

    //循环写法
    fun test() {
        val array = arrayListOf("1", "2", "3")
        for (i in array.indices) {
            println(array[i])
        }

        for (text in array) {
            println(text)
        }
    }

    //数组集合互相转化
    fun testArrToList() {
        //声明一个数组
        val array1 = arrayListOf("str1", "str2", "str3")
        //将数组转化为集合
        val list1 = array1.toList()
        //将集合转化为数组
        val array2 = list1.toTypedArray()
    }


    /**
     * 只读集合和可变集合
     */
    //集合遍历的方法 for-in  forEach   迭代器遍历
    fun testForList() {
        //不可变集合，listOf
        val titleList0 = listOf<String>();
        //
        val titleList =
            listOf<String>("推荐", "热点", "视频", "北京", "社会", "问答", "图片", "科技", "财经", "军事", "国际")
        //报错
        //titleList.add("aaa");
        for (title in titleList) {
            println(title + "//")
        }
        //第二种
        for (i in titleList.indices) {
            println("item：" + titleList[i])
        }
        //倒叙循环，
        for (i in titleList.size - 1 downTo 0) {
            println("invertedFor：" + titleList[i])
        }


        //迭代器遍历
        val iterator = titleList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            println(item + "//")
        }

        //可变list，mutableListOf，arrayListOf
        val list2 = mutableListOf("苹果", "西瓜", "橙子")
        list2.add("香蕉")

        //可变集合
        val poiList: MutableList<String> = mutableListOf();
        poiList.add("aaa")
        //不可变集合,add 会报错，一般也不会用mutableListOf 来生成可变集合
        val poiList1: List<String> = mutableListOf();
        // poiList1.add("aaa")

        //
        for (i in 0..10000) {
            println("item=====：$i")
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun testMap() {
        //map和MutableMap
        var goodsMap: Map<String, String> =
            mapOf("苹果" to "iphone8", "华为" to "Mate 10", "小米" to "小米9", "魅族" to "魅族Pro6s")

        //Pair方式初始化映射
        var goodsMutmap: MutableMap<String, String> =
            mutableMapOf(Pair("苹果", "iphone9"), Pair("华为", "荣耀10"), Pair("Vivo", "Vivox9"))


        //打印元素的总个数
        println(goodsMap.size)
        //获取某个key对应的value
        println(goodsMap.get("苹果"))  //iphone8

        println(goodsMap.getOrDefault("11", "不存在值"))  //不存在值

        //获取所有的key，和 value
        for (key in goodsMap.keys) {
            println(key)
        }
        for (value in goodsMap.values) {
            println(value)
        }

        //可以将map转换成mutablemap
        val toMutableMap = goodsMap.toMutableMap()
        toMutableMap["苹果"] = "iphone x max"

        println(toMutableMap.get("苹果"))  //iphone x max

        //移除元素
        toMutableMap.remove("小米")


        //也是3种遍历map的方式 for-in forEach iterator
        for (item in goodsMutmap) {
            println(item.key + "")
            println(item.value + "/")
        }

        val iterator = goodsMutmap.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            println(item.key + "")
            println(item.value + "/")
        }

        // 使用forEach遍历
        goodsMap.forEach { (key, value) ->
            println(key)
            println(value)
        }

        goodsMutmap.forEach { (key, value) ->
            println(key)
            println(value)
        }
    }


    //初始化 对象
    var intent = Intent()


}