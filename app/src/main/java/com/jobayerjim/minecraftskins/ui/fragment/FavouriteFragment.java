package com.jobayerjim.minecraftskins.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jobayerjim.minecraftskins.R;
import com.jobayerjim.minecraftskins.controller.adapter.MainContentRecyclerAdapter;
import com.jobayerjim.minecraftskins.controller.helper.dbhelper.DatabaseAccess;
import com.jobayerjim.minecraftskins.controller.listener.RefreshListener;
import com.jobayerjim.minecraftskins.models.Constants;
import com.jobayerjim.minecraftskins.models.SkinsModel;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment implements RefreshListener {
    private ArrayList<SkinsModel> skinsModels = new ArrayList<>();
    MainContentRecyclerAdapter mainContentRecyclerAdapter;
    SwipeRefreshLayout favouriteSwipe;
    TextView noItemText;
    private FragmentManager fragmentManager;

    public FavouriteFragment(FragmentManager fragmentManager) {
        Constants.favouriteListener = this;
        this.fragmentManager = fragmentManager;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_favourite, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        mainContentRecyclerAdapter = new MainContentRecyclerAdapter(requireContext(), skinsModels, fragmentManager);
        RecyclerView recyclerView=v.findViewById(R.id.favouriteRecycler);
        favouriteSwipe=v.findViewById(R.id.favouriteSwipe);
        noItemText=v.findViewById(R.id.noItemText);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));
        recyclerView.setAdapter(mainContentRecyclerAdapter);
        getDataFromDb();
        favouriteSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromDb();
            }
        });
    }

    public void getDataFromDb() {
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(requireContext());
        databaseAccess.open();
        skinsModels.clear();
        skinsModels.addAll(databaseAccess.getAllFavourite());
        if (skinsModels.size()<1) {
            noItemText.setVisibility(View.VISIBLE);
        }
        mainContentRecyclerAdapter.notifyDataSetChanged();
        databaseAccess.close();
        if (favouriteSwipe.isRefreshing()) {
            favouriteSwipe.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getDataFromDb();
    }
}