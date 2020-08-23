package com.rokkhi.rokkhiguard.data;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.rokkhi.rokkhiguard.Model.Whitelist;

import java.util.List;

public class WhiteListRepository {


    private String DB_NAME = "room_db_wl";

    private static RoomDB whitelistDatabase;
    public WhiteListRepository(Context context) {
        whitelistDatabase = Room.databaseBuilder(context, RoomDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public  static void insert(final Whitelist whiteList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                whitelistDatabase.whiteListDao().insertWhiteList(whiteList);
                return null;
            }
        }.execute();
    }



    public LiveData<List<Whitelist>> getAllWhiteList(String buildID) {
        return whitelistDatabase.whiteListDao().fetchAllWhiteLists(buildID);
    }

    public static void deleteWhiteList(final Whitelist whiteList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                whitelistDatabase.whiteListDao().deleteWhiteList(whiteList);
                return null;
            }
        }.execute();
    }

    public static void dropWhiteListTable() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                whitelistDatabase.whiteListDao().dropWhiteListTable();
                return null;
            }
        }.execute();
    }

}
