package com.rokkhi.rokkhiguard.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rokkhi.rokkhiguard.Model.ActiveFlats;

import java.util.List;


@Dao
public interface ActiveFlatsDao {

    @Insert
    void insertActiveFlat(ActiveFlats activeFlat);


    @Query("SELECT * FROM ActiveFlats ORDER BY f_no desc")
    LiveData<List<ActiveFlats>> fetchAllActiveFlats();


    @Query("SELECT * FROM ActiveFlats WHERE flat_id =:flatId")
    LiveData<ActiveFlats> getActiveFlat(int flatId);


    @Update
    void updateActiveFlat(ActiveFlats note);


    @Delete
    void deleteActiveFlat(ActiveFlats note);

}
