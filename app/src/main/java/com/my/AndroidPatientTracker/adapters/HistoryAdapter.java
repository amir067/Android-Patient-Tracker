package com.my.AndroidPatientTracker.adapters;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.HistoryModel;
import com.my.AndroidPatientTracker.utils.FirestoreAdapter;


public class HistoryAdapter extends FirestoreAdapter<HistoryAdapter.MyRecyclerViewHolder> {

    private OnItemClickListener listener;

    public HistoryAdapter(Query query, OnItemClickListener listener) {
        super(query);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView score, time;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.score);
            time = itemView.findViewById(R.id.created_at);

        }

        void bind(final DocumentSnapshot snapshot, final OnItemClickListener listener) {

            HistoryModel historyModel = snapshot.toObject(HistoryModel.class);

            assert historyModel != null;
            String scores = historyModel.getScore();

            score.setText(scores);

            long update_date = historyModel.getUpdated_at().toDate().getTime();
            String updated = DateUtils.getRelativeTimeSpanString(update_date).toString();
            time.setText(updated);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(snapshot);

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

}
