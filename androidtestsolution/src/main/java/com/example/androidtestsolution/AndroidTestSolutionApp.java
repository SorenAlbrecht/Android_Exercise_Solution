package com.example.androidtestsolution;

import android.app.Application;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import kaufland.com.hydraapi.HydraApiClient;
import kaufland.com.hydraapi.HydraNotInstalledException;


/*
Ich habe mich dazu entschlossen, die DWGroups ohne verfügbare Taskitems angezeigt zu lassen.
Ich vermute, dass die App dazu dienen könnte den Warenbestand bzw. die verfügbaren Produkte zu überprüfen.
Dadurch wird direkt sichtbar, für welche Kategorien keine Produkte mehr verfügbar sind.

 */

public class AndroidTestSolutionApp extends Application implements HydraApiClient.HydraServiceCallback
{
    HydraApiClient apiClient = new HydraApiClient();
    @Override
    public void onCreate()
    {
        super.onCreate();
        try
        {
            apiClient.connectService(this, this);
        }catch(HydraNotInstalledException e)
        {
            Toast t = Toast.makeText(this,"Hydra not installed", Toast.LENGTH_LONG);
            t.show();
        }

    }

    @Override
    public void sessionExpired()
    {

    }

    @Override
    public void onServiceConnected()
    {
        Toast t = Toast.makeText(this,"Connected to Hydra", Toast.LENGTH_LONG);
        t.show();
        MainActivity.setupRecyclerView();


    }

    @Override
    public void onServiceDisconnected()
    {

    }

    @Override
    public void onServiceDied()
    {

    }

    /**
     * fetches groupData from remote Service and adds the TaskItemCount to the groups
     * @return List containing Groups as Maps including their TaskItemCount
     */
    public List<Map<String, Object>> getGroupData()
    {

        List<Map<String, Object>> groupData = apiClient.findByType("DWGroup");
       for(int i =0;i<groupData.size();i++)
       {
           String itemType = (String)groupData.get(i).get("item_type");
           List taskItems = apiClient.findByTypeAndItemType("TaskItem", itemType);
           int count = taskItems.size();
           groupData.get(i).put("TaskItemCount",count);
       }


        return groupData;
    }
}
