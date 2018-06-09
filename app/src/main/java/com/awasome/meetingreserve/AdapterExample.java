package com.awasome.meetingreserve;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterExample extends RecyclerView.Adapter<AdapterExample.MyViewHolder> {

    private ArrayList<String> pictureArrayList;
    private int itemLayout;

    public AdapterExample(ArrayList<String> pictureArrayList, int itemLayout) {
        this.pictureArrayList = pictureArrayList;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public AdapterExample.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new AdapterExample.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterExample.MyViewHolder holder, final int position) {
        holder.time.setText("17:00");
        holder.eventName.setText(pictureArrayList.get(Math.min(position, pictureArrayList.size() -1 )));
        holder.roomName.setText("Room N_" + position);
    }


    @Override
    public int getItemCount() {
        return pictureArrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView roomName;
        TextView eventName;
        TextView time;

        MyViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name);
            eventName = itemView.findViewById(R.id.event_name);
            time = itemView.findViewById(R.id.time);

        }

    }


}