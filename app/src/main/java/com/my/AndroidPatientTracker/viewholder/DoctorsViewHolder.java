package com.my.AndroidPatientTracker.viewholder;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.AndroidPatientTracker.Interface.ItemClickListener;
import com.my.AndroidPatientTracker.R;


public class DoctorsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //public TextView id;
    public TextView doctor_name;
    public TextView doctor_Category;
    public TextView doctor_experence;
    public TextView doctor_address;
    public RatingBar doctor_rating;
    public TextView doctor_Fee;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public DoctorsViewHolder(@NonNull View itemView) {
        super(itemView);

        doctor_name= (TextView) itemView.findViewById(R.id.d_name);
        doctor_Category= (TextView) itemView.findViewById(R.id.d_category);
        doctor_experence= (TextView) itemView.findViewById(R.id.d_experence);
        doctor_address= (TextView) itemView.findViewById(R.id.d_address);
        doctor_rating= (RatingBar) itemView.findViewById(R.id.d_rating);
        doctor_Fee= (TextView) itemView.findViewById(R.id.d_fee);

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

