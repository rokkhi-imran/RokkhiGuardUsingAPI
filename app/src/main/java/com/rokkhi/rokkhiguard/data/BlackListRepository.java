package com.rokkhi.rokkhiguard.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.rokkhi.rokkhiguard.Model.BlackList;

import java.util.List;

public class BlackListRepository {


    private String DB_NAME = "room_db_bl";

    private static RoomDB blacklistDatabase;
    public BlackListRepository(Context context) {
        blacklistDatabase = Room.databaseBuilder(context, RoomDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public  static void insert(final BlackList blackList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                blacklistDatabase.blackListDao().insertBlackList(blackList);
                return null;
            }
        }.execute();
    }


    public LiveData<List<BlackList>> getAllBlackList(String buildID) {
        return blacklistDatabase.blackListDao().fetchAllBlackLists(buildID);
    }

    public static void deleteBlackList(final BlackList blackList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                blacklistDatabase.blackListDao().deleteBlackList(blackList);
                return null;
            }
        }.execute();
    }
    public static void dropBlackListTable() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                blacklistDatabase.blackListDao().dropBlackListTable();
                return null;
            }
        }.execute();
    }

}
