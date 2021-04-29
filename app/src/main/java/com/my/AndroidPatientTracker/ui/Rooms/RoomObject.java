package com.my.AndroidPatientTracker.ui.Rooms;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
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

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getSpace_total() {
		return space_total;
	}

	public void setSpace_total(Double space_total) {
		this.space_total = space_total;
	}

	public void setSpace_filled(Double space_filled) {
		this.space_filled = space_filled;
	}

	public long getAdded_at() {
		return added_at;
	}

	public void setAdded_at(long added_at) {
		this.added_at = added_at;
	}

	public long getModified_at() {
		return modified_at;
	}

	public void setModified_at(long modified_at) {
		this.modified_at = modified_at;
	}
}