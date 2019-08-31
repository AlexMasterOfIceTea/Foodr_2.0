package com.cheekibreeki.foodr.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cheekibreeki.foodr.MainActivity;
import com.cheekibreeki.foodr.MyViewModel;
import com.cheekibreeki.foodr.R;
import com.cheekibreeki.foodr.adapters.SearchAdapter;
import com.cheekibreeki.foodr.adapters.SwipeAdapter;
import com.cheekibreeki.foodr.database.Food;
import com.cheekibreeki.foodr.database.Repository;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {

    private SwipeFlingAdapterView swipeView;
    private MyViewModel viewModel;
    private View view;
    private SwipeAdapter adapter;
    private SearchAdapter searchAdapter;
    private ArrayList<Food> foods;
    private List<Food> lastDbFoods = new ArrayList<>();

    public ExploreFragment(){
        foods = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_explore, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of((MainActivity)context).get(MyViewModel.class);
        adapter = new SwipeAdapter(getContext(), viewModel);
        searchAdapter = new SearchAdapter(context);


        final Observer observer = new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
             for(Food food: difference(foods, lastDbFoods)){
                 int index = ExploreFragment.this.foods.indexOf(food);
                 if(index >= 0) {
                     ExploreFragment.this.foods.get(index).set(food);
                     if (index == 0) {
                         swipeView.removeAllViewsInLayout();
                     }
                 }
             }
             lastDbFoods.clear();
             lastDbFoods.addAll(foods);
             adapter.notifyDataSetChanged();
            }
        };
        viewModel.getDbData().observe(this, observer);

        Repository.QueryBuilder builder = new Repository.QueryBuilder(Repository.QueryBuilder.TEXTEARCH).addLocation(53.588270f, 10.146522f, 1000).addQuery("restaurant");

        if(viewModel.getApiFoods().size() == 0) {
            MyViewModel.loadFromApi(new Repository.OnLoadingListener() {
                @Override
                public void loadingFinished(List<Food> list) {
                    foods = (ArrayList<Food>) list;
                    adapter.setDataset(foods);
                    viewModel.setApiFoods(foods);
                    observer.onChanged(foods);
                }
            }, builder.toString());
        }else{
            foods = (ArrayList<Food>) viewModel.getApiFoods();
            adapter.setDataset(foods);
            viewModel.setApiFoods(foods);
            observer.onChanged(foods);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AutoCompleteTextView searchView = view.findViewById(R.id.search_bar);
        searchView.setAdapter(searchAdapter);
        searchView.setThreshold(0);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((SearchAdapter.SearchTerm) parent.getItemAtPosition(position)).getTitle();
                Repository.QueryBuilder builder = new Repository.QueryBuilder(Repository.QueryBuilder.TEXTEARCH).addLocation(53.588270f, 10.146522f, 1000).addQuery(title+" restaurant");

                MyViewModel.loadFromApi(new Repository.OnLoadingListener() {
                    @Override
                    public void loadingFinished(List<Food> list) {
                        foods = (ArrayList<Food>) list;
                        adapter.setDataset(foods);
                        viewModel.setApiFoods(foods);
                    }}, builder.toString());
            }
        });

        swipeView = view.findViewById(R.id.swipeView);

        swipeView.setAdapter(adapter);
        swipeView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                adapter.removeFirstObjectInAdapter();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(getContext(), "left exit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(getContext(), "right exit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) { }

            @Override
            public void onScroll(float v) { }
        });
    }

    private HashSet<Food> difference(List<Food> list1, List<Food> list2){
        HashSet<Food> set1 = new HashSet<>(list1);
        HashSet<Food> set2 = new HashSet<>(list2);
        HashSet<Food> allItems = new HashSet<>(set1);
        HashSet<Food> doubles = new HashSet<>();

        for(Food currentFood: set2) {
            if(!allItems.add(currentFood))
                doubles.add(currentFood);
        }
        allItems.removeAll(doubles);
        return allItems;
    }
}
