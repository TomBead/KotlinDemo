package com.tt.ktdemo.function.file;

import android.os.Environment
import com.tt.ktdemo.app.AppApplication
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

object FileUtils {


    //获取app内置存储数据
    //context.getFilesDir().getPath() ;
//    Context.getCacheDir():               /data/data/com.learn.test/cache
//    Context.getFilesDir():               /data/data/com.learn.test/files
//    Context.getFileStreamPath(""):       /data/data/com.learn.test/files
//    Context.getFileStreamPath("test"):   /data/data/com.learn.test/files/test

    
    //获取 sd卡app的私有目录，位置 /storage/emulated/0/Android/data/包名/files
    //filePath = context.getExternalFilesDir(null).getPath();
//    Context.getExternalCacheDir():                           /storage/emulated/0/Android/data/com.learn.test/cache
//    Context.getExternalFilesDir(""):                         /storage/emulated/0/Android/data/com.learn.test/files
//    Context.getExternalFilesDir("test"):                     /storage/emulated/0/Android/data/com.learn.test/files/test
//    Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES):    /storage/emulated/0/Android/data/com.learn.test/files/Pictures

    //公共目录，只要读写权限就可以读写，而不用其他东西
    //除这个之外，还有其他目录
    private val tempPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            .toString() + "/openglDemo/test.mp4"


    /**
     * 复制文件，最快方法
     *
     * @param newPath
     * @param oldPath
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copyFileUsingFileChannels(newPath: String?, oldPath: String?) {
        val newFile = File(newPath)
        val oldFile = File(oldPath)
        var inputChannel: FileChannel? = null
        var outputChannel: FileChannel? = null
        inputChannel = FileInputStream(newFile).channel
        outputChannel = FileOutputStream(oldFile).channel
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
    }


    /**
     * 写入文件
     */
    //用于导出数据库文件，测试用
    fun copyFileToData() {
        //找到文件的路径  /data/data/包名/databases/数据库名称
        val dbname =
            (Environment.getDataDirectory().absolutePath + "/data/" + AppApplication.instance().packageName) + "/databases/learn.db"
        val dbFile = File(dbname)
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        try {
            //文件复制到sd卡中
            fis = FileInputStream(dbFile)
            fos = FileOutputStream(
                Environment.getExternalStorageDirectory()
                    .absolutePath + "/learn.db"
            )
            var len = 0
            val buffer = ByteArray(2048)
            while (-1 != fis.read(buffer).also { len = it }) {
                fos.write(buffer, 0, len)
            }
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
                fis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}
