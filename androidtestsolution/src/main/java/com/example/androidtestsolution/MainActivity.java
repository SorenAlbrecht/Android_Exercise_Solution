package com.example.androidtestsolution;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kaufland.com.hydraapi.HydraApiClient;
import kaufland.com.hydraapi.IHydraApi;


public class MainActivity extends AppCompatActivity
{

    static AndroidTestSolutionApp app;

    public static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (AndroidTestSolutionApp)getApplication();

        recyclerView = findViewById(R.id.rv_groupList);
        setupRecyclerView();



    }

    /**
     * sets up the Adapter and LayoutManager for the Recyclerview
     */
    public static void setupRecyclerView()
    {

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(app.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        List<Map<String, Object>> groupData =app.getGroupData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(groupData);
        recyclerView.setAdapter(adapter);
    }


}
