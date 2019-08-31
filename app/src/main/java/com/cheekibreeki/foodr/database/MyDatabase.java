package com.cheekibreeki.foodr.database;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version = 3)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public static synchronized MyDatabase instanciate(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "foods_db")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract FoodDao foodDao();
}