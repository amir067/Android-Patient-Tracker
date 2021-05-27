package com.my.AndroidPatientTracker.ui.Patients;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;

import java.util.ArrayList;
import java.util.List;

public class PatientsListAdapterV2 extends RecyclerView.Adapter<PatientsListAdapterV2.MyviewHolder> {
    private static final String TAG = "PatientsListAdapter";
    private static OnClickListner onClickListner;

    Context context;
    List<UserModel> patientsList;
    private RecyclerviewOnClickListener listener;

    //constructor
    public PatientsListAdapterV2(Context context, List<UserModel> RoomsList, RecyclerviewOnClickListener Listener) {
        this.context = context;
        this.patientsList = RoomsList;
        this.listener=Listener;
    }

    @Override
    public PatientsListAdapterV2.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_patients_full,parent,false);
        return new MyviewHolder(view);
    }
    public static class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{

        public TextView patientIDTV;
        public TextView patientNameTV;
        public TextView patientAgeTV;
        public TextView patientGenderTV;
        public TextView patientRoomTV;

        public MyviewHolder(View itemView) {
            super(itemView);

            patientIDTV = (TextView) itemView.findViewById(R.id.tv_p_id);
            patientNameTV = (TextView) itemView.findViewById(R.id.tv_p_name);
            patientAgeTV = itemView.findViewById(R.id.tv_p_age);
            patientGenderTV = itemView.findViewById(R.id.tv_p_gender);
            patientRoomTV = itemView.findViewById(R.id.tv_p_room);


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
        PatientsListAdapterV2.onClickListner = onclicklistner;
    }
    public interface OnClickListner {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    @Override
    public void onBindViewHolder(PatientsListAdapterV2.MyviewHolder holder, int position) {

        TextView patientIDTV = holder.patientIDTV;
        TextView patientNameTV = holder.patientNameTV;
        TextView patientAgeTV = holder.patientAgeTV;
        TextView patientGenderTV = holder.patientGenderTV;
        TextView patientRoomTV = holder.patientRoomTV;

        patientIDTV.setText(patientsList.get(position).getId());
        patientNameTV.setText(patientsList.get(position).getName());
        patientAgeTV.setText(""+patientsList.get(position).getAge());
        patientGenderTV.setText(patientsList.get(position).getGender());
        patientRoomTV.setText(patientsList.get(position).getRoomName());

        //Picasso.get().load(placeObjects.get(position).getImageURL()).into(placeImageURLIV);

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

    public void filterList(ArrayList<UserModel> filteredList) {
        patientsList = filteredList;
        notifyDataSetChanged();
    }

    public void setPlaceObjects(List<UserModel> AlertList) {
        this.patientsList = AlertList;
        Log.d("checkid",String.valueOf(patientsList.size())+"adpter");
        notifyDataSetChanged();
    }

    public void clearList() {
        this.patientsList.clear();
        Log.d("clearList Done: size",String.valueOf(patientsList.size())+"in adapter");
        notifyDataSetChanged();
    }

    public void mergeList(List<UserModel> AlertList) {
        this.patientsList.addAll(AlertList);
        Log.d("appendedlistsize",String.valueOf(patientsList.size()));
        notifyDataSetChanged();
    }

    public List<UserModel> getPatientsList() {
        Log.d("returning List in adptr",String.valueOf(patientsList.size()));
        return patientsList;
    }

    public UserModel getPatient(int index) {
        Log.i(TAG,"returning place from adapter :"+String.valueOf(patientsList.get(index).getName()));
        return patientsList.get(index);
    }

    @Override
    public int getItemCount() {
        if(patientsList != null){
            return patientsList.size();
        }
        return 0;
    }

    public UserModel getItem(int index) {
       // Log.i(TAG,"returning place from adapter :"+String.valueOf(roomsList.get(index).getName()));
        return patientsList.get(index);
    }




}
