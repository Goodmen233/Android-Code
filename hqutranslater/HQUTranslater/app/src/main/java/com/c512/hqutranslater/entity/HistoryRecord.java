package com.c512.hqutranslater.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class HistoryRecord {

    /**
     * 主键
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="record_id")
    private long recordId;

    /**
     * 输入的源语言
     */
    @ColumnInfo(name = "src")
    private String src;

    /**
     * 输出的目标语言
     */
    @ColumnInfo(name = "dest")
    private String dest;

    /**
     * 选择的源语言类型
     */
    @ColumnInfo(name = "from")
    private String from;

    /**
     * 选择的目标语言类型
     */
    @ColumnInfo(name = "to")
    private String to;

    /**
     * 翻译时的日期
     */
    @ColumnInfo(name = "date")
    private long date;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public HistoryRecord(String src, String dest, String from, String to, long date) {
        this.src = src;
        this.dest = dest;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "HistoryRecord{" +
                "recordId=" + recordId +
                ", src='" + src + '\'' +
                ", dest='" + dest + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date +
                '}';
    }
}
