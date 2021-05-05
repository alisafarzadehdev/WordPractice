package com.alisafarzadeh.roomdb.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao {

    @Insert
    void insert (MainData mainData);

    @Delete
    void delete(MainData mainData);

    @Query("DELETE FROM wordsTB")
    void deleteAll();

    @Query("DELETE FROM wordsTB WHERE uid = :UID")
    void deleteID(int UID);

    @Query("Select * From wordsTB")
    List<MainData> getAllData();

    @Query("DELETE FROM wordsTB WHERE uid = :ID")
    int delete(int ID);

   // @Query("Update wordsTB Set persian = :PersianText Where Id")
}
