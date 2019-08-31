package com.cheekibreeki.foodr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface FoodDao {
    @Delete
    void delete(Food... foods);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Food... foods);

    @Query("SELECT  * FROM Food")
    LiveData<List<Food>> getAll();


}
