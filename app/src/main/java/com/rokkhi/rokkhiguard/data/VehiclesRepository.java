package com.rokkhi.rokkhiguard.data;


import androidx.lifecycle.LiveData;
import androidx.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.rokkhi.rokkhiguard.Model.Vehicle;

import java.util.List;

public class VehiclesRepository {


    private String DB_NAME = "room_db_vh";

    private static RoomDB vehiclesDatabase;
    public VehiclesRepository(Context context) {
        vehiclesDatabase = Room.databaseBuilder(context, RoomDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public  static void insert(final Vehicle vehicle) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                vehiclesDatabase.vehicleDao().insertVehicle(vehicle);
                return null;
            }
        }.execute();
    }

    public static void deleteTask(final String build_id) {
        //final LiveData<ActiveFlats> task = getTask(id);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                vehiclesDatabase.vehicleDao().getVehiclesforDeletion(build_id);
                return null;
            }
        }.execute();
    }



    public LiveData<List<Vehicle>> getVehicleFromPhoneAndFlatId(String flat_id ) {
        return vehiclesDatabase.vehicleDao().fetchVehicleWithFlatId(flat_id );
    }


    public LiveData<List<Vehicle>> getAllVehicle() {
        return vehiclesDatabase.vehicleDao().fetchAllVehicles();
    }

    public static void deleteVehicle(final Vehicle vehicle) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                vehiclesDatabase.vehicleDao().deleteVehicle(vehicle);
                return null;
            }
        }.execute();
    }

}

