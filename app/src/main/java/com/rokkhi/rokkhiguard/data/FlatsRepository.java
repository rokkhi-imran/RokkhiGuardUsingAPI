package com.rokkhi.rokkhiguard.data;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.rokkhi.rokkhiguard.Model.ActiveFlats;

import java.util.List;

public class FlatsRepository {


    private String DB_NAME = "room_db";

    private static RoomDB activeFlatsDatabase;
    public FlatsRepository(Context context) {
        activeFlatsDatabase = Room.databaseBuilder(context, RoomDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public  static void insertActiveFlat(final ActiveFlats activeFlat) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                activeFlatsDatabase.roomDao().insertActiveFlat(activeFlat);
                return null;
            }
        }.execute();
    }


    public LiveData<List<ActiveFlats>> getAllActiveFlats(String buildID) {
        return activeFlatsDatabase.roomDao().fetchAllActiveFlats(buildID);


    }

    public static void deleteActiveFlat(final ActiveFlats activeFlats) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                activeFlatsDatabase.roomDao().deleteActiveFlat(activeFlats);
                return null;
            }
        }.execute();
    }
    public static void dropActiveFlat() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                activeFlatsDatabase.roomDao().dropActiveFlat();
                return null;
            }
        }.execute();
    }

}
