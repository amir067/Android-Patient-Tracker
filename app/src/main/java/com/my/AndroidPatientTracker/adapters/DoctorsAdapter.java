package com.my.AndroidPatientTracker.adapters;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.DoctorsModel;
import com.my.AndroidPatientTracker.utils.FirestoreAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DoctorsAdapter extends FirestoreAdapter<DoctorsAdapter.MyRecyclerViewHolder> {

    private OnItemClickListener listener;

    protected DoctorsAdapter(Query query, OnItemClickListener listener) {
        super(query);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyRecyclerViewHolder(inflater.inflate(R.layout.doctors_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctor_name)
        TextView doctor_name;

        @BindView(R.id.doctor_address)
        TextView doctor_address;

        @BindView(R.id.doctor_image)
        ImageView doctor_image;

        @BindView(R.id.doctor_rating)
        RatingBar doctor_rating;

        @BindView(R.id.next)
        Button next;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



        }

        @SuppressLint("SetTextI18n")
        void bind(final DocumentSnapshot snapshot,
                  final OnItemClickListener listener) {

            DoctorsModel model = snapshot.toObject(DoctorsModel.class);

            assert model != null;
            long update_date = model.getCreated_at().toDate().getTime();
            String updated = DateUtils.getRelativeTimeSpanString(update_date).toString();

            doctor_name.setText(model.getName());
            doctor_address.setText(model.getAddress());
            String gender = model.getGender();

            if (gender.startsWith("M")) {
                Glide.with(doctor_image.getContext())
                        .load(R.drawable.ic_avatar)
                        .placeholder(R.drawable.picture_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(doctor_image);
            } else {
                    Glide.with(doctor_image.getContext())
                            .load(R.drawable.ic_avatar)
                            .placeholder(R.drawable.picture_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(doctor_image);

            }

            doctor_rating.setRating(Float.parseFloat(String.valueOf(model.getRating())));


            next.setOnClickListener(view -> {
                if (listener != null) listener.onReadClick(snapshot);
            });


        }
    }

    public interface OnItemClickListener {
        void onReadClick(DocumentSnapshot documentSnapshot);
   }

}