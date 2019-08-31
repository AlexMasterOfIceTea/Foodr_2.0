package com.cheekibreeki.foodr.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheekibreeki.foodr.MainActivity;
import com.cheekibreeki.foodr.MyViewModel;
import com.cheekibreeki.foodr.R;
import com.cheekibreeki.foodr.adapters.RecyclerAdaper;
import com.cheekibreeki.foodr.database.Food;
import com.cheekibreeki.foodr.database.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAdaper adaper;
    private MyViewModel viewModel;
    private LinearLayoutManager layoutManager;
    private View view;
    private List<Food> dbFoods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favoutite, container, false);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = view.findViewById(R.id.fav_list);
        recyclerView.setAdapter(adaper);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of((MainActivity)context).get(MyViewModel.class);
        dbFoods = viewModel.getDbData().getValue();
        if(dbFoods == null)
            dbFoods = new ArrayList<>();
        adaper = new RecyclerAdaper(dbFoods, viewModel);

        viewModel.getDbData().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {   //keep db and dbFoods in sync
                HashSet<Food> set = new HashSet<Food>(dbFoods);
                for (Food f : foods) {
                    int index = dbFoods.indexOf(f);
                    if (index >= 0) {
                        dbFoods.get(index).set(f);  //copy the new values over
                    } else {
                        dbFoods.add(f);
                    }
                    set.remove(f);
                }
                dbFoods.removeAll(set);
                adaper.notifyDataSetChanged();
            }
        });
    }
}
