package com.tt.ktdemo.copy


/**
 * calss和方法
 */
//如果没有 open 那么
open class ClassAndFun {
    // （如果不想暴露成员变量的set方法，可以将 var 改成 val )
    var name: String? = null
        get() = field
        set(value) {
            field = value
        }


    var age: Int = 0
        get() = field
        set(value) {
            field = value
        }

//调用
//    var person = Person("Android轮子哥", 100)
//    person.name = "HJQ"
//    person.age = 50
//    println("name: {$person.name}, age: {$person.age}")


    //=====================================================
    // void 方法
    open fun test(message: String) {

    }

    //有返回值的方法
    fun testReturn(message: String): String {
        return "ssss";
    }

    //可变参数方法
    fun add(vararg array: Int): Int {
        var count = 0
        //for (i in array) {
        //    count += i
        //}
        array.forEach {
            count += it
        }
        return count
    }

    //静态方法
    companion object {
        // 包裹范围内 属于静态方法
        fun staticFun() {
        }
    }

    //内部类
    inner class MyTask {

    }
}

//继承
class ClassAndFunSon : ClassAndFun() {
    //重写方法
    override fun test(message: String) {
        super.test(message);
    }
}

//抽象类
abstract class BaseActivity : ClassAndFun(), Runnable {
    abstract fun init()
}

//接口
interface Callback {
    fun onSuccess()
    fun onFail() {
    }
}


//静态类
object StaticUtil {
    fun staticFun() {

    }
}

//枚举单例模式
//在Kotlin中调用
//fun main(args: Array<String>) {
//    KEnumSingleton.INSTANCE.doSomeThing()
//}
////在Java中调用
//KEnumSingleton.INSTANCE.doSomeThing();
enum class KEnumSingleton {
    INSTANCE;

    fun doSomeThing() {
        println("do some thing")
    }
}