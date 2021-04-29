package com.my.AndroidPatientTracker.ui.Rooms;

import com.google.firebase.database.IgnoreExtraProperties;

//@IgnoreExtraProperties
public  class RoomObject {

	private String id;
	private String name;
	private String status;
	private Double space_total;
	private Double space_filled;
	private long added_at;
	private long modified_at;

	public RoomObject() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public String getId() {
		return id;
	}

	public void setId(String Id) {
		this.id = Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String Name) {
		name = Name;
	}

	public String getStatus() {
		return status;
	}


	public Double getSpace_filled() {
		return space_filled;
	}

}