package com.my.AndroidPatientTracker.Interface;

import android.view.View;

public interface RecyclerviewOnClickListener {
    void recyclerviewClick(int position);
    void onItemLongClick(int position, View v);
}
