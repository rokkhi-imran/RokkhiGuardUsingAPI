package com.rokkhi.rokkhiguard.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Whitelist;

@Database(entities = {ActiveFlats.class , Whitelist.class}, version = 2 , exportSchema = false)

public abstract class RoomDB extends RoomDatabase {

    private static RoomDB INSTANCE;
    public abstract ActiveFlatsDao roomDao();
    public abstract WhiteListDao whiteListDao();
}
