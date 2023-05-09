package com.tt.ktdemo.app

import android.app.Activity
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.hjq.demo.http.glide.GlideApp
import com.hjq.demo.http.model.RequestHandler
import com.hjq.demo.http.model.RequestServer
import com.hjq.demo.manager.ActivityManager
import com.hjq.demo.other.CrashHandler
import com.hjq.demo.other.ToastLogInterceptor
import com.hjq.demo.other.ToastStyle
import com.hjq.gson.factory.GsonFactory
import com.hjq.http.EasyConfig
import com.hjq.http.config.IRequestApi
import com.hjq.http.model.HttpHeaders
import com.hjq.http.model.HttpParams
import com.hjq.toast.ToastUtils
import com.hjq.umeng.UmengClient
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.tt.ktdemo.R
import com.tt.ktdemo.function.map.LocationManager
import com.tt.ktdemo.function.map.MMKVSavePoi
import okhttp3.OkHttpClient
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 应用入口
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initSdk(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // 清理所有图片内存缓存
        GlideApp.get(this).onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // 根据手机内存剩余情况清理图片内存缓存
        GlideApp.get(this).onTrimMemory(level)
    }


    companion object {
        //单例化
        lateinit var instance: Application private set
        fun instance() = instance

        /**
         * 初始化一些第三方框架
         */
        fun initSdk(application: Application) {

            // 初始化吐司
            ToastUtils.init(application, ToastStyle())
            // 设置调试模式
            ToastUtils.setDebugMode(AppConfig.isDebug())
            // 设置 Toast 拦截器
            ToastUtils.setInterceptor(ToastLogInterceptor())

            // 本地异常捕捉
            CrashHandler.register(application)

            // 友盟统计、登录、分享 SDK
            UmengClient.init(application, AppConfig.isLogEnable())

            // Bugly 异常捕捉
            CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug())

            // Activity 栈管理初始化
            ActivityManager.getInstance().init(application)

            // MMKV 初始化
            MMKV.initialize(application)

            // 网络请求框架初始化
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .build()

            EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(AppConfig.isLogEnable())
                // 设置服务器配置
                .setServer(RequestServer())
                // 设置请求处理策略
                .setHandler(RequestHandler(application))
                // 设置请求重试次数
                .setRetryCount(1)
                .setInterceptor { api: IRequestApi, params: HttpParams, headers: HttpHeaders ->
                    // 添加全局请求头
                    headers.put("token", "66666666666")
                    headers.put("deviceOaid", UmengClient.getDeviceOaid())
                    headers.put("versionName", AppConfig.getVersionName())
                    headers.put("versionCode", AppConfig.getVersionCode().toString())
                }
                .into()

            // 设置 Json 解析容错监听
            GsonFactory.setJsonCallback { typeToken: TypeToken<*>, fieldName: String?, jsonToken: JsonToken ->
                // 上报到 Bugly 错误列表
                CrashReport.postCatchedException(IllegalArgumentException("类型解析异常：$typeToken#$fieldName，后台返回的类型为：$jsonToken"))
            }

            // 初始化日志打印
            if (AppConfig.isLogEnable()) {
                Timber.plant(DebugLoggerTree())
            }

            // 注册网络状态变化监听
            val connectivityManager: ConnectivityManager? =
                ContextCompat.getSystemService(application, ConnectivityManager::class.java)
            if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onLost(network: Network) {
                        val topActivity: Activity? = ActivityManager.getInstance().getTopActivity()
                        if (topActivity !is LifecycleOwner) {
                            return
                        }
                        val lifecycleOwner: LifecycleOwner = topActivity
                        if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                            return
                        }
                        ToastUtils.show(R.string.common_network_error)
                    }
                })
            }


            /**
             * 日记
             */
            Logger.addLogAdapter(object : AndroidLogAdapter() {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return true
                }
            })
            try {
                //
                val info: PackageInfo = instance.getPackageManager().getPackageInfo(
                    instance.getPackageName(), PackageManager.GET_SIGNATURES
                )
                val cert = info.signatures[0].toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA1")
                val publicKey: ByteArray = md.digest(cert)

                val hexString = StringBuffer()
                for (element in publicKey) {
                    val appendString =
                        Integer.toHexString(0xFF and element.toInt()).uppercase(Locale.US)
                    if (appendString.length == 1) hexString.append("0")
                    hexString.append(appendString)
                    hexString.append(":")
                }
                val result = hexString.toString();
                result.substring(0, result.length - 1);
                Log.i("", "签名信息：================" + result)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.i("", "签名信息：================" + "NameNotFoundException")
            } catch (e: NoSuchAlgorithmException) {
                Log.i("", "签名信息：================" + "NoSuchAlgorithmException")
            }


            MMKVSavePoi.inti();
            LocationManager.readPoiList()
        }
    }


}