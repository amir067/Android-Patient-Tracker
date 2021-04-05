package com.my.AndroidPatientTracker.adapters;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.RemediesModel;
import com.my.AndroidPatientTracker.utils.FirestoreAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RemediesAdapter extends FirestoreAdapter<RemediesAdapter.MyRecyclerViewHolder> {

    private OnItemClickListener listener;

    protected RemediesAdapter(Query query, OnItemClickListener listener) {
        super(query);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyRecyclerViewHolder(inflater.inflate(R.layout.user_blog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.blog_title)
        TextView blog_title;

        @BindView(R.id.blog_content)
        TextView blog_content;

        @BindView(R.id.blog_created_at)
        TextView blog_created_at;

        @BindView(R.id.blog_image)
        ImageView blog_image;

        @BindView(R.id.blog_read)
        Button blog_read;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            blog_content.setMovementMethod(LinkMovementMethod.getInstance());

        }

        @SuppressLint("SetTextI18n")
        void bind(final DocumentSnapshot snapshot,
                  final OnItemClickListener listener) {

            RemediesModel model = snapshot.toObject(RemediesModel.class);

            assert model != null;
            long update_date = model.getUpdated_at().toDate().getTime();
            String updated = DateUtils.getRelativeTimeSpanString(update_date).toString();

            blog_created_at.setText(updated);

            blog_title.setText(model.getTitle());
            blog_content.setText(model.getContent());
            Glide.with(blog_image.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.picture_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(blog_image);


            blog_read.setOnClickListener(view -> {
                if (listener != null) listener.onReadClick(snapshot);
            });


        }
    }

    public interface OnItemClickListener {
        void onReadClick(DocumentSnapshot documentSnapshot);
   }

}