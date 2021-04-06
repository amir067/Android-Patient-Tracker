package com.my.AndroidPatientTracker.ui.Rooms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import java.util.ArrayList;
import java.util.List;

public class RoomsListAdapter extends RecyclerView.Adapter<RoomsListAdapter.MyviewHolder> {
    private static final String TAG = "RoomsListAdapter";
    private static OnClickListner onClickListner;

    Context context;
    List<RoomObject> roomsList;
    private RecyclerviewOnClickListener listener;

    //constructor
    public RoomsListAdapter(Context context, List<RoomObject> RoomsList, RecyclerviewOnClickListener Listener) {
        this.context = context;
        this.roomsList = RoomsList;
        this.listener=Listener;
    }

    @Override
    public RoomsListAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_room,parent,false);
        return new MyviewHolder(view);
    }
    public static class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView roomNameTV;
        public TextView roomStatusTV;
        public TextView roomSpaceFilledTV;

        public MyviewHolder(View itemView) {
            super(itemView);

            roomNameTV = (TextView) itemView.findViewById(R.id.tv_room_name);
            roomStatusTV = (TextView) itemView.findViewById(R.id.tv_room_status);
            roomSpaceFilledTV = itemView.findViewById(R.id.tv_room_space_filled);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickListner.onItemClick(getAdapterPosition(), v);
        }

    }
    public void setOnItemClickListener(OnClickListner onclicklistner) {
        RoomsListAdapter.onClickListner = onclicklistner;
    }
    public interface OnClickListner {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    @Override
    public void onBindViewHolder(RoomsListAdapter.MyviewHolder holder, int position) {

        TextView roomNameTV = holder.roomNameTV;
        TextView roomStatusTV = holder.roomStatusTV;
        TextView roomSpaceFilledTV = holder.roomSpaceFilledTV;

        roomNameTV.setText(roomsList.get(position).getName());
        roomStatusTV.setText(roomsList.get(position).getStatus());
        roomSpaceFilledTV.setText(""+roomsList.get(position).getSpace_filled());

        //Picasso.get().load(placeObjects.get(position).getImageURL()).into(placeImageURLIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.recyclerviewClick(position);
            }
        });

    }

    public void filterList(ArrayList<RoomObject> filteredList) {
        roomsList = filteredList;
        notifyDataSetChanged();
    }

    public void setPlaceObjects(List<RoomObject> AlertList) {
        this.roomsList = AlertList;
        Log.d("checkid",String.valueOf(roomsList.size())+"adpter");
        notifyDataSetChanged();
    }

    public void clearList() {
        this.roomsList.clear();
        Log.d("clearList Done: size",String.valueOf(roomsList.size())+"in adapter");
        notifyDataSetChanged();
    }

    public void mergeAlertList(List<RoomObject> AlertList) {
        this.roomsList.addAll(AlertList);
        Log.d("appendedlistsize",String.valueOf(roomsList.size()));
        notifyDataSetChanged();
    }

    public List<RoomObject> getPlaceObjects() {
        Log.d("returning List in adptr",String.valueOf(roomsList.size()));
        return roomsList;
    }

    public RoomObject getPlace(int index) {
        Log.i(TAG,"returning place from adapter :"+String.valueOf(roomsList.get(index).getName()));
        return roomsList.get(index);
    }

    @Override
    public int getItemCount() {
        if(roomsList != null){
            return roomsList.size();
        }
        return 0;
    }




}
