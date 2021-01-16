package com.example.dreamleague.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.dreamleague.Adapters.CreateAdapters.CreateTeamFragmentAdapter;
import com.example.dreamleague.Fragments.CreateFragments.CreateTeamLineupFragment;
import com.example.dreamleague.Fragments.CreateFragments.CreateTeamListFragment;
import com.example.dreamleague.R;

public class CreateTeamFragmentHolderActivity extends AppCompatActivity {

    public ViewPager2 viewPager2;
    public CreateTeamFragmentAdapter adapter;
    String TeamName ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_fragment_holder);
        TeamName = getIntent().getStringExtra("TEAM_NAME");
        ViewPagerSetup();
    }

    private void ViewPagerSetup(){
        adapter = new CreateTeamFragmentAdapter(this);
        adapter.addFragment(new CreateTeamLineupFragment());
        adapter.addFragment(new CreateTeamListFragment());
        adapter.setTeamName(TeamName);
        viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(adapter);
    }

    public void selectIndex(int newIndex){
        viewPager2.setCurrentItem(newIndex);
    }
    @Override
    public void onBackPressed(){
        int currentPosition = viewPager2.getCurrentItem();
        if(currentPosition != 0){
            viewPager2.setCurrentItem(0);
        }else{
            super.onBackPressed();
        }
    }

}