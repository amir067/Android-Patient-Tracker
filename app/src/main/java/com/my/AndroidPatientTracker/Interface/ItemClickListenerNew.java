package com.my.AndroidPatientTracker.Interface;

import android.view.View;

public interface ItemClickListenerNew {
    void OnClick(View view, int position, boolean isLongClick);

    void onLongClick(View view, int position, boolean isLongClick);


}
