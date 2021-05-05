package com.alisafarzadeh.roomdb.Utill;

import android.content.Context;
import android.content.SharedPreferences;

import com.alisafarzadeh.roomdb.Room.MainData;
import com.alisafarzadeh.roomdb.Room.RoomHolder;

public class OneTimeRun {

     SharedPreferences onetime;
     SharedPreferences.Editor editor;

    public OneTimeRun() {
        roomHolder = RoomHolder.getInstance(G.context.getApplicationContext());
        onetime = G.context.getSharedPreferences("com.alisafarzadeh.roomdb.isonetime", Context.MODE_PRIVATE);
        editor = onetime.edit();
        editor.putBoolean("isOneTime",true);

    }

    public boolean isOne() {

        if (onetime.getBoolean("isOneTime",true))
        {
            editor.putBoolean("isOneTime",false);
            editor.commit();
            editor.apply();
            return true;
        }else
        {
            return false;
        }
    }

    static RoomHolder roomHolder;

    public void setdata()
    {
        if (isOne())
        {
            MainData onedata = new MainData();
            onedata.setEnglish("Hello");
            onedata.setPersian("سلام");

            MainData twedata = new MainData();
            twedata.setEnglish("what day is tomorrow?");
            twedata.setPersian("فردا چند شنبه است؟");

            MainData threedata = new MainData();
            threedata.setEnglish("the hell with it");
            threedata.setPersian("به درک");

            roomHolder.mainDao().insert(onedata);
            roomHolder.mainDao().insert(twedata);
            roomHolder.mainDao().insert(threedata);
            //
        }
    }
}

