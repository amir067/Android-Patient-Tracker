package com.my.AndroidPatientTracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.ImageModel;
import com.my.AndroidPatientTracker.utils.FirestoreAdapter;



public class NatureAdapter extends FirestoreAdapter<NatureAdapter.MyRecyclerViewHolder> {

    private OnItemClickListener listener;

    public NatureAdapter(Query query, OnItemClickListener listener) {
        super(query);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rv_image);

            final AlphaAnimation alphaAnimation4 = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation4.setDuration(3000);

            imageView.startAnimation(alphaAnimation4);


        }

        void bind(final DocumentSnapshot snapshot, final OnItemClickListener listener) {

            ImageModel imageModel = snapshot.toObject(ImageModel.class);

            String image = imageModel.getImage();
            int scores = imageModel.getScores();

            Glide.with(imageView.getContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(snapshot);

                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

}
