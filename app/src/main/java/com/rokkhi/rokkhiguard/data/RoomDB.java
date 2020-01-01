package com.rokkhi.rokkhiguard.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.BlackList;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Whitelist;

@Database(entities = {ActiveFlats.class , Whitelist.class, BlackList.class, Vehicle.class}, version = 4 , exportSchema = false)

public abstract class RoomDB extends RoomDatabase {

    private static RoomDB INSTANCE;
    public abstract ActiveFlatsDao roomDao();
    public abstract WhiteListDao whiteListDao();
    public abstract BlackListDao blackListDao();
    public abstract VehiclesDao vehicleDao();
}
