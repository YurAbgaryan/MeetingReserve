package com.awasome.meetingreserve;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterParticipants extends RecyclerView.Adapter<AdapterParticipants.MyViewHolder> {

    private ArrayList<String> nameArrayList;
    private int itemLayout;

    public AdapterParticipants(ArrayList<String> pictureArrayList, int itemLayout) {
        this.nameArrayList = pictureArrayList;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public AdapterParticipants.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_participants, parent, false);
        return new AdapterParticipants.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterParticipants.MyViewHolder holder, final int position) {
        holder.name.setText(nameArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }

    }


}