package com.awasome.meetingreserve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yuri on 6/9/18.
 */

public class EventsListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_events, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add("Board Meeting");
		arrayList.add("Staff");
		arrayList.add("Blot Mlot");
		arrayList.add("Ads meeting");
		arrayList.add("meeting");
		arrayList.add("Just meeting");
		arrayList.add("Just meeting");



		RecyclerView recyclerView = getActivity().findViewById(R.id.event_recycle);
		AdapterExample adapter = new AdapterExample(arrayList, R.layout.card_item, getContext());
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter.notifyDataSetChanged();
	}
}
