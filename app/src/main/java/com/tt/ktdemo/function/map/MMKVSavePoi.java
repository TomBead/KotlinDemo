package com.tt.ktdemo.function.map;


import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 保存经纬度点的数据
 */
public class MMKVSavePoi {
    private static final String ID = "poi";
    private static final String cryptKey = "poikey";
    //编号，mmkv有时候对加入很快的东西没写入完
    private static int num = 0;
    private static MMKV kv;
    private static ExecutorService singleThreadExecutor;

    public static void inti() {
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        kv = MMKV.mmkvWithID(ID, MMKV.SINGLE_PROCESS_MODE, cryptKey);
    }

    /**
     * 写入poi数据
     */
    public static void addPoi(final String log) {
//        Logger.d("======MyMMKVLog:???????" + callMethodAndLine());
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //时间 key
                num += 1;
                String key = System.currentTimeMillis() + "==";
                String info = System.currentTimeMillis() + "#" + getCurrTimes() + "#" + log;
                kv.encode(key, info);
//                Logger.d("======MyMMKVLog:" + info);
            }
        });
    }

    private static String callMethodAndLine() {
        String result = "";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")";
        result += ":-->>" + thisMethodStack.getMethodName();
        return result;
    }

    /**
     * 获取所有log列表，
     */
    public static List<String> getPoiList() {
        String[] key = kv.allKeys();
        if (key == null) {
            Logger.d("key.length====null");
            return null;
        }
        Logger.d("key.length====" + key.length);
        List<String> stringList = new ArrayList<>();
        //排序
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            String str = kv.decodeString(key[i]);
            stringList.add(str);
        }
        return stringList;
    }

    public static void cleanData() {
        kv.clearAll();
    }

    public static void checkSize() {
        String[] key = kv.allKeys();
        if (key == null) {
            Logger.d("key.length====null");
            return;
        }
        Arrays.sort(key);
        String currday = getCurrDayTimes();
        for (int i = 0; i < key.length; i++) {
            //不是今天的日志，全部删掉
            if (!key[i].contains(currday)) {
                kv.remove(key[i]);
            }
        }
    }


    private static String getCurrTimes() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private static String getCurrDayTimes() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}