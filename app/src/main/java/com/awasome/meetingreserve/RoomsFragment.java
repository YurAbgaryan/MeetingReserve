package com.awasome.meetingreserve;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yuri on 6/9/18.
 */

public class RoomsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_rooms, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<RoomModel> arrayList = new ArrayList<>();
		arrayList.add(new RoomModel("Saryan", true, "16:00","Staff Meeting" ));
		arrayList.add(new RoomModel("Da Vinci", false, "12:55", "22"));
		arrayList.add(new RoomModel("Aywasovski", true, "11:30", "Check-In"));
		arrayList.add(new RoomModel("Library", false, "09:09","Stand Up" ));

		RecyclerView recyclerView = getActivity().findViewById(R.id.rooms_recycler);
		RoomsAdapter adapter = new RoomsAdapter(arrayList, R.layout.card_item);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter.notifyDataSetChanged();
	}

	public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {

		private ArrayList<RoomModel> roomsList;
		private int itemLayout;

		public RoomsAdapter(ArrayList<RoomModel> pictureArrayList, int itemLayout) {
			this.roomsList = pictureArrayList;
			this.itemLayout = itemLayout;
		}

		@NonNull
		@Override
		public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
			return new MyViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
			int index = Math.min(position, roomsList.size() - 1);

			RoomModel model = roomsList.get(index);
			holder.time.setText(model.getTime());
			holder.eventName.setText(model.getEventName());
			holder.roomName.setText(model.getName());
		}

		@Override
		public int getItemCount() {
			return roomsList.size();
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
				Intent intent = new Intent(getActivity(), RoomActivity.class);
				intent.putExtra("roomName", roomName.getText());
				intent.putExtra("time", time.getText());
				intent.putExtra("eventName", eventName.getText());
				startActivity(intent);
			}
		}


	}
}
