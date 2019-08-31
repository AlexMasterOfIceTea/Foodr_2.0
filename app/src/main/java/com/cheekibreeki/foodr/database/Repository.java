package com.cheekibreeki.foodr.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.InvalidationTracker;

import com.cheekibreeki.foodr.adapters.SwipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Repository {

    public interface OnLoadingListener {
        void loadingFinished(List<Food> list);
    }

    private LiveData<List<Food>> dbFoods;
    private FoodDao dao;

    public Repository(Context context) {
        dao = MyDatabase.instanciate(context).foodDao();
        dbFoods = dao.getAll();
    }

    public LiveData<List<Food>> getAllDbFoods(){
        return dbFoods;
    }

    public void insertToDb(Food... foods){
        new InsertTask(dao).execute(foods);
    }

    public void deleteFromDb(Food... foods){
        new DeleteTask(dao).execute(foods);
    }

    public static void loadFromApi(OnLoadingListener listener, String searchQuery){
        new LoadFromApi(listener).execute(searchQuery);
    }

    public static class QueryBuilder{

        /*
        private final String APP_ID = "Kz5CsuxEX3s7xbc6WW7k";
        private final String APP_CODE = "CNDA4BWaUhs8ItuEhxnLdA";
        private final String BASE_STRING = "https://places.cit.api.here.com/places/v1/discover/around?";
        */
        private final String API_KEY = "AIzaSyCuoz0pNMmSH0n1MuTid18Kl7DfCyiYCQQ";
        private final String BASE_STRING = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
        private final String BASE_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?";
        public final static int TEXTEARCH = 0;
        public final static int PHOTOS = 1;

        private StringBuilder sb;

        public QueryBuilder(int type){
            if(type == TEXTEARCH)
                sb = new StringBuilder(BASE_STRING);
            else
                sb = new StringBuilder(BASE_PHOTO);
            sb.append("key=").append(API_KEY);
        }

        public QueryBuilder addPhotoReference(String photoReference){
            sb.append("&photoreference=").append(photoReference);
            return this;
        }

        public QueryBuilder addConstraints(int maxWidth, int maxHeight){
            if(maxWidth != 0){
                sb.append("&maxwidth=").append(maxWidth);
            }else{
                sb.append("&maxheight=").append(maxHeight);
            }
            return this;
        }

        public QueryBuilder addLocation(float latitude, float longituge, float radius){
            sb.append("&location=").append(latitude).append(",").append(longituge);
            if(radius != 0)
                sb.append("&radius=").append(radius);
            return this;
        }

        public QueryBuilder addQuery(String query){
            sb.append("&query=").append(query);
            return this;
        }

        @NonNull
        @Override
        public String toString() {
            String s =  sb.toString();
            return s;
        }
    }

    //Helper Classes for Async-loading
    private static class InsertTask extends AsyncTask<Food, Void, Void> {
        private FoodDao dao;

        InsertTask(FoodDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            dao.insert(foods);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Food, Void, Void> {
        private FoodDao dao;

        DeleteTask(FoodDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            dao.delete(foods);
            return null;
        }
    }

    private static class LoadFromApi extends AsyncTask<String, Void, List<Food>> {
        private OnLoadingListener listener;

        LoadFromApi(OnLoadingListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Food> doInBackground(String... strings) {
            try {
                JSONArray results;
                StringBuilder sb = new StringBuilder();

                URL searchQuery = new URL(strings[0]);
                InputStream stream = searchQuery.openConnection().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                results = new JSONObject(sb.toString()).getJSONArray("results");
                ArrayList<Food> foods = new ArrayList<>();
                for(int i=0 ; i<results.length(); i++){
                    Food f = new Food(results.getJSONObject(i));
                    foods.add(f);
                }
                stream.close();
                reader.close();
                return foods;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Food> foods) {
            super.onPostExecute(foods);
            listener.loadingFinished(foods);
        }
    }
}
