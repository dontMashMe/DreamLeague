package com.example.dreamleague.Fragments.SeasonFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamleague.R;

public class TransfersFragment extends Fragment {

    @NonNull
    public static TransfersFragment newInstance(){
        Bundle args = new Bundle();
        TransfersFragment fragment = new TransfersFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfers, container, false);
        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

