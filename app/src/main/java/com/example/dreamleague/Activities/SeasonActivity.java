package com.example.dreamleague.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dreamleague.Adapters.SeasonAdapters.SeasonFragmentAdapter;
import com.example.dreamleague.Fragments.SeasonFragments.HomeFragment;
import com.example.dreamleague.Fragments.SeasonFragments.TransfersFragment;
import com.example.dreamleague.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;
import androidx.viewpager2.widget.ViewPager2;



public class SeasonActivity extends AppCompatActivity {

    public ViewPager2 viewPager2;
    public SeasonFragmentAdapter seasonFragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        setUpViewPager();
        viewPager2.setCurrentItem(1);
        navView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_home){
                viewPager2.setCurrentItem(1);
            }
            else if(item.getItemId() == R.id.nav_transfer){
               viewPager2.setCurrentItem(0);
            }
            else if(item.getItemId() == R.id.nav_list){
                viewPager2.setCurrentItem(2);
            }
            return true;
        });

    }



    private void setUpViewPager(){
        viewPager2 = findViewById(R.id.season_viewpager);
        seasonFragmentAdapter = new SeasonFragmentAdapter(this);
        HomeFragment homeFragment = new HomeFragment();
        TransfersFragment transfersFragment = new TransfersFragment();
        ListFragment listFragment = new ListFragment();
        seasonFragmentAdapter.addFragment(homeFragment);
        seasonFragmentAdapter.addFragment(transfersFragment);
        seasonFragmentAdapter.addFragment(listFragment);
        viewPager2.setAdapter(seasonFragmentAdapter);
    }

}