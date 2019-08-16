package com.rokkhi.rokkhiguard.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
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



    public LiveData<ActiveFlats> getFlatWithId(int flat_id) {
        return activeFlatsDatabase.roomDao().getActiveFlat(flat_id);
    }

    public LiveData<List<ActiveFlats>> getAllActiveFlats() {
        return activeFlatsDatabase.roomDao().fetchAllActiveFlats();


    }

//    public static void updateTask(final ActiveFlats activeFlats) {
//        activeFlats.setModifiedAt(AppUtils.getCurrentDateTime());
//
//        new  AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                RoomDB.roomDao().updateTask(activeFlats);
//                return null;
//            }
//        }.execute();
//    }
//
//    public void deleteTask(final int id) {
//        final LiveData<ActiveFlats> task = getTask(id);
//        if(task != null) {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    activeFlatsDatabase.daoAccess().deleteTask(task.getValue());
//                    return null;
//                }
//            }.execute();
//        }
//    }
//
    public static void deleteActiveFlat(final ActiveFlats activeFlats) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                activeFlatsDatabase.roomDao().deleteActiveFlat(activeFlats);
                return null;
            }
        }.execute();
    }

}