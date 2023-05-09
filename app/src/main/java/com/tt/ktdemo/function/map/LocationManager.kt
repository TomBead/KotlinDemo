package com.tt.ktdemo.function.map

import com.orhanobut.logger.Logger
import timber.log.Timber

object LocationManager {

    //可变集合
    val poiList: MutableList<PoiBean> = mutableListOf<PoiBean>();

    /**
     * 保存经纬度
     */
    fun savePoi(lat: Double, log: Double) {
        MMKVSavePoi.addPoi("$lat#$log");
    }
    /**
     * 保存经纬度
     */
    fun cleanPoi() {
        MMKVSavePoi.cleanData()
    }

    /**
     * 初始化读取
     */
    fun readPoiList() {
        val list = MMKVSavePoi.getPoiList();
        poiList.clear()
        for (item in list) {
            Logger.d("item=======$item")
            val poi = PoiBean();
            val spList = item.split("#")
            poi.id = spList[0].toLong();
            poi.time = spList[1];
            poi.lat = spList[2].toDouble();
            poi.log = spList[3].toDouble();
            poiList.add(poi)
        }
    }
}


class PoiBean {
    //顺序
    var id: Long = 0;

    //经纬度
    var lat: Double = 0.0;
    var log: Double = 0.0;

    //时间
    var time: String = "";


}