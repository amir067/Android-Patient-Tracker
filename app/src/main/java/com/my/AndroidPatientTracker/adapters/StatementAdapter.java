package com.my.AndroidPatientTracker.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.StatementModel;
import com.my.AndroidPatientTracker.utils.FirestoreAdapter;


public class StatementAdapter extends FirestoreAdapter<StatementAdapter.MyRecyclerViewHolder> {

    private OnItemClickListener listener;
    private static final String TAG = "StatementAdapter";


    public StatementAdapter(Query query, OnItemClickListener listener) {
        super(query);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        Button statement;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            statement = itemView.findViewById(R.id.btn_statement);


        }

        void bind(final DocumentSnapshot snapshot, final OnItemClickListener listener) {

            StatementModel statementModel = snapshot.toObject(StatementModel.class);


            String question = statementModel.getQuestion();
            Log.i(TAG, "Adapter in bind viewHolder: "+question);
            int scores = statementModel.getScores();

            statement.setText(question);

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
