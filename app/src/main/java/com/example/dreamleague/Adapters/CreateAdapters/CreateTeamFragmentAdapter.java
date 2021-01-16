package com.example.dreamleague.Adapters.CreateAdapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.Fragments.CreateFragments.CreateTeamLineupFragment;
import com.example.dreamleague.Fragments.CreateFragments.CreateTeamListFragment;
import com.example.dreamleague.Listeners.PositionGetter;
import com.example.dreamleague.Listeners.PositionListener;

import java.util.ArrayList;

public class CreateTeamFragmentAdapter extends FragmentStateAdapter implements PositionListener, PositionGetter {

    public ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private String position;
    private int balance;
    private String TeamName;

    public CreateTeamFragmentAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString("TEAM_NAME", TeamName);
        switch(position){
            case 0:
                fragment = CreateTeamLineupFragment.newInstance();
                fragment.setArguments(bundle);
                ((CreateTeamLineupFragment)fragment).positionListener = this;
                break;
            case 1:
                fragment = CreateTeamListFragment.newInstance();
                ((CreateTeamListFragment)fragment).positionGetter = this;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
    public void addFragment(Fragment fragment){
        fragmentArrayList.add(fragment);
    }

    public void setTeamName(String teamName){
        this.TeamName = teamName;
    }
    @Override
    public void setPosition(String position) {
        this.position = position;
    }
    @Override
    public void setBalance(int balance) {
            this.balance = balance;
    }
    @Override
    public Utils getPositon() {
        return new Utils(position, balance);
    }
    @Override
    public Utils getBalance() {
        return null;
    }
}
