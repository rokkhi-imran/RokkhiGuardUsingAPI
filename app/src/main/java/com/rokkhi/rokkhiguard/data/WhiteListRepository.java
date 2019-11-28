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

 /*   public static void deleteTask(final String build_id) {
        //final LiveData<ActiveFlats> task = getTask(id);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                whitelistDatabase.whiteListDao().getWhitelistsforDeletion(build_id);
                return null;
            }
        }.execute();
    }
*/


/*

    public LiveData<List<Whitelist>> getWhiteListFromPhoneAndFlatId(int flat_id , String w_phone) {
        return whitelistDatabase.whiteListDao().fetchWhiteListWithPhoneAndFlatId(flat_id , w_phone);
    }
*/


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

}
