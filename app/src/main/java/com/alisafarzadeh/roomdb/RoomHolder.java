package com.alisafarzadeh.roomdb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MainData.class},version =1,exportSchema = false)
public abstract class RoomHolder extends RoomDatabase {

    static RoomHolder database;
    static String DB_Name ="wordDB";

    public synchronized static RoomHolder getInstance(Context context)
    {
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext()
                    ,RoomHolder.class,DB_Name)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDao mainDao();


    public static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MainDao mAsyncTaskDao;
        MainData mainData;
        deleteAllWordsAsyncTask(MainDao dao, MainData main) {
            mAsyncTaskDao = dao;
            mainData = main;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.delete(mainData);
            return null;
        }
    }
}
