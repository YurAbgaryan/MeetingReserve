package com.awasome.meetingreserve;

import android.graphics.Picture;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");
        arrayList.add("adsad");


        RecyclerView recyclerView = findViewById(R.id.event_recycle);
        AdapterExample adapter = new AdapterExample(arrayList, R.layout.card_item);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
