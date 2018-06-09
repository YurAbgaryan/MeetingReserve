package com.awasome.meetingreserve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Event");

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


        RecyclerView recyclerView = findViewById(R.id.participants_recycle);
        AdapterParticipants adapter = new AdapterParticipants(arrayList, R.layout.card_participants);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
