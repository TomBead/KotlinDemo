package com.tt.ktdemo.copy

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


/**
 * 协程
 * 协程其实是一个线程切换的api,用线程池执行耗时任务
 * 目标：
 * 简单的应用协程，结合viewmodel，可取消
 * 1.简单跑耗时方法,然后返回结果到主线程，可取消，
 * 2.多个请求一起，用协程获取结果，可取消，
 */
class CoroutineSample {
    fun test() {
        //创建作用域，Dispatchers.Default==计算密集型？
        val scope = CoroutineScope(Dispatchers.Default)
        //协程job1将会被取消，而另一个job2则不受任何影响
        val job1 = scope.launch {
            //TODO
            //挂起协程
            delay(1000L)
        }
        val job2 = scope.launch {
            //TODO
        }
        //取消单个协程
        job1.cancel()

    }

    fun test1(callBack: CallBack) {
        //创建作用域，Dispatchers.IO io密集型？
        val scope = CoroutineScope(Dispatchers.IO)
        val job1 = scope.launch {
            //TODO
            callBack.runWork()
        }
    }

    fun test2() {
        //创建作用域，Dispatchers.IO io密集型？
        val scope = CoroutineScope(Dispatchers.IO)
        val job1 = scope.launch {
            val time = measureTimeMillis {//计算执行时间
                val deferredOne: Deferred<Int> = async {
                    delay(2000)
                    print("asyncOne")
                    100//这里返回值为100
                }

                val deferredTwo: Deferred<Int> = async {
                    delay(3000)
                    print("asyncTwo")
                    200//这里返回值为200
                }

                val deferredThr: Deferred<Int> = async {
                    delay(4000)
                    print("asyncThr")
                    300//这里返回值为300
                }

                //等待所有需要结果的协程完成获取执行结果
                val result = deferredOne.await() + deferredTwo.await() + deferredThr.await()
                print("result == $result")
            }
            print("耗时 $time ms")
        }
    }
}

interface CallBack {
    fun runWork();
}