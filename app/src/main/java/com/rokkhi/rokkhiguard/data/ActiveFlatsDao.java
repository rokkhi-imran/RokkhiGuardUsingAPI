package com.rokkhi.rokkhiguard.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
