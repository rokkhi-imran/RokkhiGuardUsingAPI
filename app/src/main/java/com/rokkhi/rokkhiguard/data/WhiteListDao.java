package com.rokkhi.rokkhiguard.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rokkhi.rokkhiguard.Model.Whitelist;

import java.util.List;

@Dao
public interface WhiteListDao {


    @Insert
    void insertWhiteList(Whitelist whitelist);


    @Query("SELECT * FROM Whitelist ORDER BY f_no desc")
    LiveData<List<Whitelist>> fetchAllWhiteLists();


    @Query("SELECT * FROM Whitelist WHERE flat_id =:flatId AND w_phone=:w_phone")
    LiveData<List<Whitelist>> fetchWhiteListWithPhoneAndFlatId(int flatId , String w_phone);




    @Delete
    void deleteWhiteList(Whitelist note);

    @Query("DELETE  FROM Whitelist WHERE build_id !=:buildId")
    void getWhitelistsforDeletion(String buildId);


}
