package com.c512.hqutranslater.utils;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.c512.hqutranslater.dao.HistoryRecordDao;
import com.c512.hqutranslater.database.QTranslatorDatabase;

public class DbUtil {

    // 数据库实例
    private static QTranslatorDatabase db;
    // dao实例
    private static HistoryRecordDao recordDao;

    /**
     * 初始化实例
     * @param context 上下文
     */
    public static void init(Context context){
        // 获取数据库实例
        db = Room.databaseBuilder(context, QTranslatorDatabase.class,
                "QTranslator_db").allowMainThreadQueries().build();
        // 初始化dao实例
        recordDao = db.recordDao();
    }

    /**
     * 获取dao实例
     * @return dao实例
     */
    public static HistoryRecordDao getDao(){
        return recordDao;
    }
}
