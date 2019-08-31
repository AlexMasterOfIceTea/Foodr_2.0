package com.cheekibreeki.foodr.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cheekibreeki.foodr.adapters.SwipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Entity
public class Food{

    @NonNull
    @PrimaryKey
    private String primKey;
    private String name;
    private float latitude;
    private float longitude;
    private String photoReference;
    private String attribution;
    private float rating;

    @ColumnInfo(name = "favourite")
    private boolean isFav;
    private String types;

    @Ignore
    private int dist;
    @Ignore
    private Bitmap photo;

    public Food(String name, float latitude, float longitude, boolean isFav, float rating, String photoReference, String attribution) {
        init(name, latitude, longitude, isFav, rating, photoReference, attribution);
    }

    public Food(JSONObject data) throws JSONException {
        name = data.getString("name");
        rating = (float)data.getDouble("rating");
        JSONObject pos = data.getJSONObject("geometry").getJSONObject("location");
        latitude = (float)pos.getDouble("lat");
        longitude = (float)pos.getDouble("lng");;
        //dist = data.getInt("distance");
        if(data.has("photos")) {
            JSONObject photo = data.getJSONArray("photos").getJSONObject(0);
            photoReference = photo.getString("photo_reference");
            attribution = photo.getJSONArray("html_attributions").getString(0); //TODO: change 4 multiple attributions
        }
        primKey = latitude+"|"+longitude;
    }

    private void init(String name, float latitude, float longitude, boolean isFav, float rating, String photoReference, String attribution) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFav = isFav;
        this.rating = rating;
        this.photoReference = photoReference;
        this.attribution = attribution;
        primKey = latitude+"|"+longitude;
    }

    public void loadBitmap(final OnLoadingDone listener){
        if(photo != null)   listener.onLoadingDone(photo);
        if(photoReference != null) {
            String searchString = new Repository.QueryBuilder(Repository.QueryBuilder.PHOTOS)
                    .addPhotoReference(photoReference)
                    .addConstraints(256, 0)
                    .toString();
            new ImageLoader(new OnLoadingDone() {
                @Override
                public void onLoadingDone(Bitmap bm) {
                    if(photo != null)
                        photo = bm;
                    listener.onLoadingDone(bm);
                }
            }).execute(searchString);
        }
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public interface OnLoadingDone{
        void onLoadingDone(Bitmap bm);
    }

    private static class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        private OnLoadingDone listener;

        ImageLoader(OnLoadingDone listener){
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream is = url.openStream();
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                return bm;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            listener.onLoadingDone(bitmap);
        }
    }

    public void set(Food food){
        init(food.getName(), food.getLatitude(), food.getLongitude(), food.isFav(), food.getRating(), food.getPhotoReference(), food.getAttribution());
    }

    public String getAttribution() {
        return attribution;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public int getDist() {
        return dist;
    }

    public String getTypesFormatted() {
        String out = "";
        for(String type: types.split(",")){
            out += type+"\n";
        }
        return out;
    }

    public String getPrimKey() {
        return primKey;
    }

    public void setPrimKey(String primKey) {
        this.primKey = primKey;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Food)
            return ((Food) obj).getPrimKey().equals(getPrimKey());
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    public String getCoordinatesAsString() {
        return latitude+","+longitude;
    }
}
