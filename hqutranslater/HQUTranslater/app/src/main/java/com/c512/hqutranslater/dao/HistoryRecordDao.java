package com.c512.hqutranslater.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.c512.hqutranslater.entity.HistoryRecord;

import java.util.List;

@Dao
public interface HistoryRecordDao {

    /**
     * 插入一条查询记录
     * @param historyRecord 记录
     * @return 插入条数
     */
    @Insert
    long insertOneRecord(HistoryRecord historyRecord);

    /**
     * 查询所有记录
     * @return 所有记录
     */
    @Query("SELECT * FROM HistoryRecord")
    List<HistoryRecord> selectAllRecord();

    /**
     * 查询前十条记录
     * @return 前十条记录
     */
    @Query("SELECT * FROM HistoryRecord order by date desc limit 0, 10")
    List<HistoryRecord> selectTopTenRecord();

    /**
     * 删除指定记录
     * @param historyRecord 记录
     */
    @Delete
    void deleteOneRecord(HistoryRecord historyRecord);

}
