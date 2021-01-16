package com.example.dreamleague.Adapters.SeasonAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dreamleague.Fragments.SeasonFragments.HomeFragment;
import com.example.dreamleague.Fragments.SeasonFragments.MatchesFragment;
import com.example.dreamleague.Fragments.SeasonFragments.TransfersFragment;

import java.util.ArrayList;
import java.util.List;

public class SeasonFragmentAdapter extends FragmentStateAdapter {

    public ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public SeasonFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch(position){
            case 0:
                fragment = TransfersFragment.newInstance();
                break;
            case 1:
                fragment = HomeFragment.newInstance();
                break;
            case 2:
                fragment = MatchesFragment.newInstance();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }

    public int getItemCount() {
        return fragmentArrayList.size();
    }
    public void addFragment(Fragment fragment){
        fragmentArrayList.add(fragment);
    }
}
