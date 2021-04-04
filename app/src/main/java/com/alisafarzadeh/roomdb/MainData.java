package com.alisafarzadeh.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "wordsTB")
public class MainData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "english")
    private String English;


    @ColumnInfo(name = "persian")
    private String Persian;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEnglish() {
        return English;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public String getPersian() {
        return Persian;
    }

    public void setPersian(String persian) {
        Persian = persian;
    }
}
