package com.awasome.meetingreserve;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdapterExample extends RecyclerView.Adapter<AdapterExample.MyViewHolder> {

    private ArrayList<String> pictureArrayList;
    private int itemLayout;
    private ArrayList<String> rooms = new ArrayList<>();
    private Context context;

    public AdapterExample(ArrayList<String> pictureArrayList, int itemLayout, Context context) {
        this.pictureArrayList = pictureArrayList;
        this.context = context;
        this.itemLayout = itemLayout;
        rooms.add("Saryan");
        rooms.add("Da Vinci");
        rooms.add("Library");
        rooms.add("Aywasovsky");
        rooms.add("Piccasso");
        rooms.add("Dalli");
        rooms.add("Besedka");
    }

    @NonNull
    @Override
    public AdapterExample.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new AdapterExample.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterExample.MyViewHolder holder, final int position) {


            holder.time.setText(new Random().nextInt(24) + ":" + new Random().nextInt(60));
            holder.eventName.setText(pictureArrayList.get(Math.min(position, pictureArrayList.size()-1)));
            holder.roomName.setText(rooms.get(new Random().nextInt(rooms.size())));

    }


    @Override
    public int getItemCount() {
        return pictureArrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView roomName;
        TextView eventName;
        TextView time;

        MyViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name);
            eventName = itemView.findViewById(R.id.event_name);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RoomActivity.class);
            intent.putExtra("roomName", roomName.getText());
            intent.putExtra("time", time.getText());
            intent.putExtra("eventName", eventName.getText());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


}