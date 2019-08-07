package com.rokkhi.rokkhiguard.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Whitelist;

@Database(entities = {ActiveFlats.class , Whitelist.class , Vehicle.class}, version = 3 , exportSchema = false)

public abstract class RoomDB extends RoomDatabase {

    private static RoomDB INSTANCE;
    public abstract ActiveFlatsDao roomDao();
    public abstract WhiteListDao whiteListDao();
    public abstract VehiclesDao vehicleDao();
}
