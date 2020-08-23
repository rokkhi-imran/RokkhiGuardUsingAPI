package com.rokkhi.rokkhiguard.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rokkhi.rokkhiguard.Model.BlackList;

import java.util.List;

@Dao
public interface BlackListDao {

    @Insert
    public void insertBlackList(BlackList blackList);

    @Query("SELECT * FROM BlackList WHERE buildID=:buildID ORDER BY flatNo desc")
   public LiveData<List<BlackList>> fetchAllBlackLists(String buildID);

    @Query("SELECT * FROM BlackList WHERE flatID =:flatId AND phone=:b_phone")
    public LiveData<List<BlackList>> fetchBlackListWithPhoneAndFlatId(int flatId , String b_phone);


    @Delete
    public void deleteBlackList(BlackList note);


    @Query("DELETE FROM BlackList")
    public void dropBlackListTable();

}
