package com.example.androidtestsolution;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<Map<String, Object>> groupData;

     RecyclerViewAdapter(List<Map<String, Object>> groupData)
    {
        this.groupData = groupData;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Map<String,Object> currentGroup = groupData.get(position);
        holder.name.setText((String)currentGroup.get("name"));
        holder.item.getBackground().setTint(Color.parseColor((String)currentGroup.get("color")));

        holder.badgeCounter.setText(String.valueOf((int)currentGroup.get("TaskItemCount")));
        holder.badgeCounter.getBackground().setTint(Color.parseColor((String)currentGroup.get("color")));
    }

    @Override
    public int getItemCount()
    {
        return groupData.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView badgeCounter;
        CardView item;
         ViewHolder(@NonNull View v)
        {
            super(v);
            name = v.findViewById(R.id.tw_item_name);
            badgeCounter = v.findViewById(R.id.tw_item_count);
            item = v.findViewById(R.id.cv_item1);
        }

    }
}
