package com.jobayerjim.minecraftskins.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobayerjim.minecraftskins.R;
import com.jobayerjim.minecraftskins.controller.adapter.MainContentRecyclerAdapter;
import com.jobayerjim.minecraftskins.controller.helper.dbhelper.DatabaseAccess;
import com.jobayerjim.minecraftskins.controller.listener.RefreshListener;
import com.jobayerjim.minecraftskins.models.Constants;
import com.jobayerjim.minecraftskins.models.SkinsModel;

import java.util.ArrayList;


public class MainFragment extends Fragment implements RefreshListener {
    private ArrayList<SkinsModel> skinsModels=new ArrayList<>();
    MainContentRecyclerAdapter mainContentRecyclerAdapter;
    RefreshListener refreshListener;
    public MainFragment() {
        // Required empty public constructor
        Constants.mainListener=this;
    }

    public void getDataFromDb() {
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(requireContext());
        databaseAccess.open();
        skinsModels.clear();
        skinsModels.addAll(databaseAccess.getAllData());
        mainContentRecyclerAdapter.notifyDataSetChanged();
        databaseAccess.close();
        if (mainSwipe.isRefreshing()) {
            mainSwipe.setRefreshing(false);
        }
        for (SkinsModel skinsModel:skinsModels) {
            Log.d(skinsModel.getId()+"",skinsModel.getImage_name()+" "+skinsModel.getRes_name());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        initView(v);

        return v;
    }
    SwipeRefreshLayout mainSwipe;
    private void initView(View v) {
        mainContentRecyclerAdapter=new MainContentRecyclerAdapter(requireContext(),skinsModels);
        RecyclerView recyclerView=v.findViewById(R.id.mainRecycler);
        mainSwipe=v.findViewById(R.id.mainSwipe);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));
        recyclerView.setAdapter(mainContentRecyclerAdapter);
        getDataFromDb();

        mainSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromDb();
            }
        });
    }

    @Override
    public void onRefresh() {
        getDataFromDb();
    }
}