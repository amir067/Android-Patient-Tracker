package com.my.AndroidPatientTracker.Events;

public class RefreshRVEvent {
    public final Boolean refresh;

    public RefreshRVEvent(Boolean doRefresh) {
        this.refresh = doRefresh;
    }
}
