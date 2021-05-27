package com.my.AndroidPatientTracker.viewholder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.AndroidPatientTracker.Interface.ItemClickListener;
import com.my.AndroidPatientTracker.R;


public class UserRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView userName;
    public TextView user_phone;
    public TextView user_id;
    public ImageView user_image;

    public TextView time;
    public TextView reqStatus;

    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;


    }
    public UserRequestViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = (TextView) itemView.findViewById(R.id.tv_user_name);
        user_phone = (TextView) itemView.findViewById(R.id.user_mob);
        user_image = (ImageView) itemView.findViewById(R.id.imagetask);
        reqStatus = (TextView) itemView.findViewById(R.id.request_status);
        time= (TextView) itemView.findViewById(R.id.time_of_transection);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }
    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view, getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onLongClick(view, getAdapterPosition(),true);
        return true;
    }



}

