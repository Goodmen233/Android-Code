package com.c512.hqutranslater.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.c512.hqutranslater.dao.HistoryRecordDao;
import com.c512.hqutranslater.entity.HistoryRecord;

@Database(entities = {HistoryRecord.class}, version = 1, exportSchema = false)
public abstract class QTranslatorDatabase extends RoomDatabase {
    public abstract HistoryRecordDao recordDao();
}
