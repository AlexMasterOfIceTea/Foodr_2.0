package com.cheekibreeki.foodr;

import android.app.Application;
import android.util.SparseArray;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cheekibreeki.foodr.database.Food;
import com.cheekibreeki.foodr.database.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyViewModel extends AndroidViewModel {

    private LiveData<List<Food>> dbFoods;
    private List<Food> apiFoods;
    private Repository repository;
    private ArrayList<Food> dbQueueInsert;
    private ArrayList<Food> dbQueueDelete;

    public MyViewModel(Application application){
        super(application);
        dbQueueInsert = new ArrayList<>();
        dbQueueDelete = new ArrayList<>();
        repository = new Repository(application);
        dbFoods = repository.getAllDbFoods();
        apiFoods = new ArrayList<>();
    }

    public List<Food> getApiFoods() {
        return apiFoods;
    }

    public void setApiFoods(List<Food> apiFoods) {
        this.apiFoods = apiFoods;
    }

    public LiveData<List<Food>> getDbData() {
        return dbFoods;
    }

    public void insertToDb(Food... foods){
        repository.insertToDb(foods);
    }

    public void deleteFromDb(Food... foods){
        repository.deleteFromDb(foods);
    }

    public static void loadFromApi(Repository.OnLoadingListener listener, String searchQuery){
        Repository.loadFromApi(listener, searchQuery);
    }

    public void queueInsertToDb(Food... foods){
        dbQueueInsert.addAll(Arrays.asList(foods));
        for(Food food: foods){
            dbQueueDelete.remove(food);
        }
    }

    public void queueDeleteFromDb(Food... foods){
        dbQueueDelete.addAll(Arrays.asList(foods));
        for(Food food: foods){
            dbQueueInsert.remove(food);
        }
    }

    public void releaseQueue(){
        insertToDb(dbQueueInsert.toArray(new Food[0]));
        deleteFromDb(dbQueueDelete.toArray(new Food[0]));

        dbQueueInsert.clear();
        dbQueueDelete.clear();
    }
}
