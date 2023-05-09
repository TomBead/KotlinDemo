package com.tt.ktdemo.function.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class], version = 2)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    //静态内部类单例
    companion object {
        private var instance: UserDataBase? = null

        private val TAG: String? = UserDataBase::class.simpleName

        /**
         * 增加字段升级(增加一个String 类型 的字段sex)
         */
        private val ADD_FIELD_MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User ADD COLUMN sex Text")
            }
        }

        fun get(context: Context): UserDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, UserDataBase::class.java, "user_.db")
                    .fallbackToDestructiveMigration()
                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()
                    //如果升级失败回滚
                    .fallbackToDestructiveMigration()
                    //升级操作
                    .addMigrations(ADD_FIELD_MIGRATION_1_2)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.e(TAG, "onCreate db_name is=" + db.path)
                        }
                    })
                    .build()
            }
            return instance!!
        }
    }
}
