package com.cheekibreeki.foodr;

import android.os.Bundle;

import com.cheekibreeki.foodr.fragments.ExploreFragment;
import com.cheekibreeki.foodr.fragments.FavouriteFragment;
import com.cheekibreeki.foodr.fragments.RadarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.PersistableBundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fManager;
    private FavouriteFragment favFragment;
    private RadarFragment radarFragment;
    private ExploreFragment exploreFragment;
    private Fragment currentFragment;
    private MyViewModel viewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.explore:
                    setFragment(exploreFragment);
                    return true;
                case R.id.favourites:
                    setFragment(favFragment);
                    return true;
                case R.id.radar:
                    setFragment(radarFragment);
                    return true;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment) {
        if (currentFragment != fragment){
            viewModel.releaseQueue();
            fManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        currentFragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fManager = getSupportFragmentManager();
        favFragment = new FavouriteFragment();
        radarFragment = new RadarFragment();
        exploreFragment = new ExploreFragment();
        fManager.beginTransaction()
                .add(R.id.container, favFragment).hide(favFragment)
                .add(R.id.container, radarFragment).hide(radarFragment)
                .add(R.id.container, exploreFragment).commit();
        currentFragment = exploreFragment;
    }

}
