package com.my.AndroidPatientTracker.ui.Chat;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyviewHolder> {
    private static final String TAG = "RoomsListAdapter";
    private static OnClickListner onClickListner;


    Context context;
    List<ChatModel> roomsList;
    private RecyclerviewOnClickListener listener;

    //constructor
    public ChatListAdapter(Context context, List<ChatModel> RoomsList, RecyclerviewOnClickListener Listener) {
        this.context = context;
        this.roomsList = RoomsList;
        this.listener=Listener;
    }

    @Override
    public ChatListAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_chat,parent,false);
        return new MyviewHolder(view);
    }
    public static class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {


        public TextView userMessage;
        public TextView senderName;
        public TextView messageTime;
        public ImageView userImage;

        public MyviewHolder(View itemView) {
            super(itemView);

            userMessage = (TextView) itemView.findViewById(R.id.userMessage);
            messageTime = (TextView) itemView.findViewById(R.id.timeAgo);
            senderName = (TextView) itemView.findViewById(R.id.userName);

            userImage = itemView.findViewById(R.id.userImage);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListner.onItemClick(getAdapterPosition(), v);
        }


        @Override
        public boolean onLongClick(View v) {
            onClickListner.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }
    public void setOnItemClickListener(OnClickListner onclicklistner) {
        ChatListAdapter.onClickListner = onclicklistner;
    }
    public interface OnClickListner {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.MyviewHolder holder, int position) {

       /* TextView roomNameTV = holder.roomNameTV;
        TextView roomStatusTV = holder.roomStatusTV;
        TextView roomSpaceFilledTV = holder.roomSpaceFilledTV;*/


        holder.userMessage.setText(roomsList.get(position).getMessage());
        holder.senderName.setText(roomsList.get(position).getSenderName());

        long update_date = roomsList.get(position).getMessageTime();
        if(update_date!=0){
            // long update_date = model.getUpdated_at().toDate().getTime();
            String updated = DateUtils.getRelativeTimeSpanString(update_date).toString();
            holder.messageTime.setText(updated);
        }

       // roomStatusTV.setText(roomsList.get(position).getStatus());
       // roomSpaceFilledTV.setText(""+roomsList.get(position).getSpace_filled().intValue());

        if(roomsList.get(position).getSenderImage() !=null){
            Picasso.get().load(roomsList.get(position).getSenderImage()).into(holder.userImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.recyclerviewClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(position,v);
                return  true;
            }
        });


    }

    public void filterList(ArrayList<ChatModel> filteredList) {
        roomsList = filteredList;
        notifyDataSetChanged();
    }

    public void setPlaceObjects(List<ChatModel> AlertList) {
        this.roomsList = AlertList;
        Log.d("checkid",String.valueOf(roomsList.size())+"adpter");
        notifyDataSetChanged();
    }

    public void clearList() {
        this.roomsList.clear();
        Log.d("clearList Done: size",String.valueOf(roomsList.size())+"in adapter");
        notifyDataSetChanged();
    }

    public void mergeAlertList(List<ChatModel> AlertList) {
        this.roomsList.addAll(AlertList);
        Log.d("appendedlistsize",String.valueOf(roomsList.size()));
        notifyDataSetChanged();
    }

    public List<ChatModel> getPlaceObjects() {
        Log.d("returning List in adptr",String.valueOf(roomsList.size()));
        return roomsList;
    }

    public ChatModel getPlace(int index) {
        Log.i(TAG,"returning place from adapter :"+String.valueOf(roomsList.get(index).message));
        return roomsList.get(index);
    }

    @Override
    public int getItemCount() {
        if(roomsList != null){
            return roomsList.size();
        }
        return 0;
    }

    public void doNotifyDataChanged() {
        Log.i(TAG,"Adapter notified: notifyDataSetChanged()");
        notifyDataSetChanged();
    }


}
