package com.my.AndroidPatientTracker.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;

import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;

import java.util.ArrayList;
import java.util.List;


public class SearchBarAdapterRoom extends SuggestionsAdapter<RoomObject, SearchBarAdapterRoom.SuggestionHolder> {

    private RecyclerviewOnClickListener listener1;

    public SearchBarAdapterRoom(LayoutInflater inflater, RecyclerviewOnClickListener Listener) {
        super(inflater);
        this.listener1=Listener;
    }

    public List<RoomObject> getPlacesList() {
        Log.d("returning suggestions",String.valueOf(suggestions.size()));
        return  suggestions ;
    }

    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_patient_name, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(RoomObject suggestion, SuggestionHolder holder, int position) {
        holder.title.setText(suggestion.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.recyclerviewClick(position);
            }
        });

    }

    /**
     * <b>Override to customize functionality</b>
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link RecyclerView.Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (RoomObject item: suggestions_clone)
                        if(item.getName().toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<RoomObject>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class SuggestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView title;
        public SuggestionHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.town_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
    }


}
