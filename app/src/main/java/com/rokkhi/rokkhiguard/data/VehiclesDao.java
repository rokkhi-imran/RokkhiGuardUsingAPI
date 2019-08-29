package com.rokkhi.rokkhiguard.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rokkhi.rokkhiguard.Model.Vehicle;

import java.util.List;

@Dao

public interface VehiclesDao {



    @Insert
    void insertVehicle(Vehicle vehicle);


    @Query("SELECT * FROM Vehicle ORDER BY f_no desc")
    LiveData<List<Vehicle>> fetchAllVehicles();


    @Query("SELECT * FROM Vehicle WHERE flat_id =:flatId")
    LiveData<List<Vehicle>> fetchVehicleWithFlatId(String flatId );



    @Delete
    void deleteVehicle(Vehicle vehicle);
}
