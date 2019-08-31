package com.cheekibreeki.foodr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cheekibreeki.foodr.R;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<SearchAdapter.SearchTerm> {

    private LayoutInflater mInflater;

    private static SearchTerm[] list = new SearchTerm[]{new SearchTerm("asian", R.drawable.ic_asian),
                                                        new SearchTerm("italian", R.drawable.ic_pizza),
                                                        new SearchTerm("bar", R.drawable.ic_bar),
                                                        new SearchTerm("burger", R.drawable.ic_burger),
                                                        new SearchTerm("german", R.drawable.ic_german)};

    public SearchAdapter(@NonNull Context context) {
        super(context, R.layout.search_autocomplete, list);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.search_autocomplete, parent, false);

        TextView title = convertView.findViewById(R.id.tv_autocomplete);
        ImageView image = convertView.findViewById(R.id.iv_autocomplete);

        SearchTerm term = getItem(position);

        title.setText(term.title);
        image.setImageResource(term.resDrawable);
        return convertView;
    }

    static public class SearchTerm{
        String title;
        int resDrawable;

        public SearchTerm(String title, int resDrawable) {
            this.title = title;
            this.resDrawable = resDrawable;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }

        public String getTitle() {
            return title;
        }
    }
}
