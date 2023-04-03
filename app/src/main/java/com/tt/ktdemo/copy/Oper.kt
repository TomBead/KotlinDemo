package com.tt.ktdemo.copy

/**
 * 操作符
 */
class Oper {

    //比较类型
    //if ("1" instanceof String) {
    //}
    fun test1() {
        if ("1" is String) {

        }
        if ("2" !is String) {
        }
    }

    fun testNUll() {
        //类型后面加?表示可为空
        var age: String? = "23"
        //抛出空指针异常
        val ages = age!!.toInt()
        //不做处理返回 null
        val ages1 = age?.toInt()
        //age为空返回-1
        val ages2 = age?.toInt() ?: -1
    }


    //对象比较
    fun test2() {
        var object1 = "123";
        var object2 = "123";
        var object3 = "456";
        // 比较两个对象内容是否一样 equals
        if (object1 == object2) {

        }
        if (object1 != object2) {
        }
        // 比较两个对象是否是同一个,相当于Java ==
        if (object1 === object2) {

        }
    }

    fun testEQ() {
//        int c = a > b ? a : b;
        var a = 10
        var b = 20
        var c = if (a > b) a else b
    }

    //判断 在不在区间内
    fun testIn(args: Array<String>) {
        val x = 5
        val y = 9
        if (x in 1..8) {
            println("x 在区间内")
        }
    }

    //判断 when==switch
    fun test3() {
        val count = 1
        when (count) {
            0 -> {
                println(count)
            }
            in 1..3 -> {
                println(count)
            }
            5, 7 -> {
                println(count)
            }
            else -> {
                println(count)
            }
        }

        // 换种更简洁的写法
        when (count) {
            0 -> println(count)
            in 1..3 -> println(count)
            5, 7 -> println(count)
            else -> println(count)
        }
    }

    fun testWhen(args: Array<String>) {
        val items = setOf("apple", "banana", "kiwi")
        when {
            "orange" in items -> println("juicy")
            "apple" in items -> println("apple is fine too")
        }
    }

    //循环 for
    fun testFor() {
        for (index in 1 until 10) {
            println(index)//输出0..9
        }
        for (index in 1..100) {
            print(index)
        }
        //100-1
        for (index in 100 downTo 1) {
            print(index)
        }
    }

    //循环  while
    fun testWhile() {
        println("----while 使用-----")
        var x = 5
        while (x > 0) {
            println(x--)
        }
        println("----do...while 使用-----")
        var y = 5
        do {
            println(y--)
        } while (y > 0)
    }

}