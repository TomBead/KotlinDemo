package com.tt.ktdemo.function.map

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.AMap.CancelableCallback
import com.amap.api.maps.AMap.OnMapTouchListener
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.model.*
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.tt.ktdemo.R
import java.io.IOException
import java.io.InputStream

class MapActivity : AppCompatActivity(), OnClickListener, LocationSource,
    AMapLocationListener, OnMapTouchListener {

    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var basicmap: Button? = null
    private var rsmap: Button? = null
    private var nightmap: Button? = null
    private var navimap: Button? = null
    private var clean: Button? = null

    private var mStyleCheckbox: CheckBox? = null
    private val mapStyleOptions: CustomMapStyleOptions? = CustomMapStyleOptions()
    private var myLocationStyle: MyLocationStyle? = null

    //
    private var polygon: Polygon? = null


    private var mListener: OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    var useMoveToLocationWithMapMode = true

    //自定义定位小蓝点的Marker
    private var locationMarker: Marker? = null

    //坐标和经纬度转换工具
    var projection: Projection? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.hide()
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        //  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = findViewById<View>(R.id.map) as MapView
        mapView!!.onCreate(savedInstanceState) // 此方法必须重写
//        init()
        getLocationPre();
        privacyCompliance();
    }

    fun getLocationPre() {
        XXPermissions.with(this)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {

                    }
                }

            })
//            .request(object : PermissionCallback() {
//                override fun onGranted(
//                    permissions: MutableList<String?>?,
//                    all: Boolean
//                ) {
//                    if (all) {
//                        callback.invoke(origin, true, true)
//                    }
//                }
//            })
    }


    private fun privacyCompliance() {
        MapsInitializer.updatePrivacyShow(this@MapActivity, true, true)
        val spannable =
            SpannableStringBuilder("\"亲，感谢您对XXX一直以来的信任！我们依据最新的监管要求更新了XXX《隐私权政策》，特向您说明如下\n1.为向您提供交易相关基本功能，我们会收集、使用必要的信息；\n2.基于您的明示授权，我们可能会获取您的位置（为您提供附近的商品、店铺及优惠资讯等）等信息，您有权拒绝或取消授权；\n3.我们会采取业界先进的安全措施保护您的信息安全；\n4.未经您同意，我们不会从第三方处获取、共享或向提供您的信息；\n")
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            35,
            42,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        AlertDialog.Builder(this)
            .setTitle("温馨提示(隐私合规示例)")
            .setMessage(spannable)
            .setPositiveButton("同意") { dialogInterface, i ->
                MapsInitializer.updatePrivacyAgree(this@MapActivity, true)
                init()
            }
            .setNegativeButton("不同意") { dialogInterface, i ->
                MapsInitializer.updatePrivacyAgree(
                    this@MapActivity,
                    true
                )
                init()
            }
            .show()
    }

    /**
     * 初始化AMap对象
     */
    private fun init() {
        if (aMap == null) {
            aMap = mapView!!.map
        }
        setMapCustomStyleFile(this)
        basicmap = findViewById<View>(R.id.basicmap) as Button
        basicmap!!.setOnClickListener(this)
        rsmap = findViewById<View>(R.id.rsmap) as Button
        rsmap!!.setOnClickListener(this)
        nightmap = findViewById<View>(R.id.nightmap) as Button
        nightmap!!.setOnClickListener(this)
        navimap = findViewById<View>(R.id.navimap) as Button
        navimap!!.setOnClickListener(this)

        clean = findViewById<View>(R.id.clean) as Button
        clean!!.setOnClickListener(this)
        //
        mStyleCheckbox = findViewById<View>(R.id.check_style) as CheckBox
        mStyleCheckbox!!.setOnCheckedChangeListener { compoundButton, b ->
            if (mapStyleOptions != null) {
                // 设置自定义样式
                mapStyleOptions.isEnable = b
                //					mapStyleOptions.setStyleId("your id");
                aMap!!.setCustomMapStyle(mapStyleOptions)
            }
        }

        setUpMap()


    }

    /**
     * 设置一些amap的属性
     */
    private fun setUpMap() {
        aMap!!.setLocationSource(this) // 设置定位监听
        aMap!!.uiSettings.isMyLocationButtonEnabled = true // 设置默认定位按钮是否显示
        aMap!!.isMyLocationEnabled = true // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap!!.setOnMapTouchListener(this)

        //移除所有覆盖物
        aMap!!.clear()
        //显示多个点
        for (item in LocationManager.poiList) {

            val latLng = LatLng(item.lat, item.log)
            aMap!!.addMarker(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.poin1))
                    .anchor(1f, 1f)
            )
        }

        //把多个点连线
        val options = PolylineOptions()
        //设置线宽度
        options.width(20f)
        options.color(Color.GREEN)
        for (item in LocationManager.poiList) {
            val latLng = LatLng(item.lat, item.log)
            //加入点
            options.add(latLng)
        }
        //加入对应的颜色,使用colorValues 即表示使用多颜色，使用color表示使用单色线
        aMap!!.addPolyline(options)

        // 绘制一个长方形
        // 绘制一个长方形
        val pOption = PolygonOptions()
        //右上
        //22.822638, 108.255827
        pOption.add(LatLng(22.822638 + 0.001, 108.255827 + 0.001))
        pOption.add(LatLng(22.822638 - 0.001, 108.255827 + 0.001))
        pOption.add(LatLng(22.822638 - 0.001, 108.255827 - 0.001))
        pOption.add(LatLng(22.822638 + 0.001, 108.255827 - 0.001))

        polygon = aMap!!.addPolygon(
            pOption.strokeWidth(4f)
                .strokeColor(Color.argb(50, 1, 1, 1))
                .fillColor(Color.argb(50, 1, 1, 1))
        )

    }

    private fun setMapCustomStyleFile(context: Context) {
        val styleName = "style.data"
        var inputStream: InputStream? = null
        try {
            inputStream = context.assets.open(styleName)
            val b = ByteArray(inputStream.available())
            inputStream.read(b)
            if (mapStyleOptions != null) {
                // 设置自定义样式
                mapStyleOptions.styleData = b
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
        mapView!!.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
        mlocationClient?.onDestroy()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.basicmap -> aMap!!.mapType = AMap.MAP_TYPE_NORMAL // 矢量地图模式
            R.id.rsmap -> aMap!!.mapType = AMap.MAP_TYPE_SATELLITE // 卫星地图模式
            R.id.nightmap -> aMap!!.mapType = AMap.MAP_TYPE_NIGHT //夜景地图模式
            R.id.navimap -> aMap!!.mapType = AMap.MAP_TYPE_NAVI //导航地图模式
            R.id.clean -> LocationManager.cleanPoi()
        }
        mStyleCheckbox!!.isChecked = false
    }

    /**
     * 定位回调
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mListener = listener
        if (mlocationClient == null) {
            try {
                mlocationClient = AMapLocationClient(this)
                mLocationOption = AMapLocationClientOption()
                //设置定位监听
                mlocationClient!!.setLocationListener(this)
                //设置为高精度定位模式
                mLocationOption!!.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                //是指定位间隔
                mLocationOption!!.interval = 5000
                //设置定位参数
                mlocationClient!!.setLocationOption(mLocationOption)
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mlocationClient!!.startLocation()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun deactivate() {
        mListener = null
        mlocationClient!!.stopLocation()
        mlocationClient!!.onDestroy()
        mlocationClient = null
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                ////通过以上方法获取定位类型，如果对定位类型要求比较高，可以过滤掉基站定位（类型6）结果。
                val locationType = amapLocation.getLocationType();
                //通过以上方法获取定位精度，例如超过500M精度的定位结果可以考虑不在业务场景里使用。
                val accuracy = amapLocation.getAccuracy();
                Log.i(
                    "AmapErr",
                    "======定位成功 lat==" + amapLocation.getLatitude()
                            + " log== " + amapLocation.getLongitude()
                            + " type== " + locationType
                            + " accuracy == " + accuracy
                )
                LocationManager.savePoi(amapLocation.getLatitude(), amapLocation.getLongitude())

                val latLng = LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())
                //展示自定义定位小蓝点
                if (locationMarker == null) {
                    //首次定位
                    locationMarker = aMap!!.addMarker(
                        MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.now_point))
                            .title("66666")
                            .anchor(1f, 1f)
                    )

                    //首次定位,选择移动到地图中心点并修改级别到15级
                    aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                } else {
                    if (useMoveToLocationWithMapMode) {
                        //二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
                        startMoveLocationAndMap(latLng)
                    } else {
                        startChangeLocation(latLng)
                    }
                }
            } else {
                val errText =
                    "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo()
                Log.e("AmapErr", errText)
            }
        }
    }

    /**
     * 修改自定义定位小蓝点的位置
     * @param latLng
     */
    private fun startChangeLocation(latLng: LatLng) {
        if (locationMarker != null) {
            val curLatlng = locationMarker!!.position
            if (curLatlng == null || curLatlng != latLng) {
                locationMarker!!.position = latLng
            }
        }
    }

    /**
     * 同时修改自定义定位小蓝点和地图的位置
     * @param latLng
     */
    private fun startMoveLocationAndMap(latLng: LatLng) {

        //将小蓝点提取到屏幕上
        if (projection == null) {
            projection = aMap!!.projection
        }
        if (locationMarker != null && projection != null) {
            val markerLocation = locationMarker!!.position
            val screenPosition = aMap!!.projection.toScreenLocation(markerLocation)
            locationMarker!!.setPositionByPixels(screenPosition.x, screenPosition.y)
        }

        //移动地图，移动结束后，将小蓝点放到放到地图上
        myCancelCallback?.setTargetLat(latLng)
        locationMarker?.let { myCancelCallback?.setMarker(it) }
        //动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
        //如果超过了，需要在myCancelCallback中进行处理被打断的情况
        aMap!!.animateCamera(CameraUpdateFactory.changeLatLng(latLng), 1000, myCancelCallback)
    }


    override fun onTouch(p0: MotionEvent?) {
        useMoveToLocationWithMapMode = false
    }

    /**
     * 监控地图动画移动情况，如果结束或者被打断，都需要执行响应的操作
     */
    private var myCancelCallback: MyCancelCallback? = MyCancelCallback()

    internal class MyCancelCallback : CancelableCallback {
        var targetLatlng: LatLng? = null
        var locationMarker: Marker? = null

        fun setTargetLat(latlng: LatLng?) {
            targetLatlng = latlng
        }

        fun setMarker(mark: Marker) {
            locationMarker = mark
        }

        override fun onFinish() {
            if (locationMarker != null && targetLatlng != null) {
                locationMarker!!.setPosition(targetLatlng)
            }
        }

        override fun onCancel() {
            if (locationMarker != null && targetLatlng != null) {
                locationMarker!!.setPosition(targetLatlng)
            }
        }
    }

}
