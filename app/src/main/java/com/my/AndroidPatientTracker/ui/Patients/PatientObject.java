package com.my.AndroidPatientTracker.ui.Patients;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public  class PatientObject {

	private String Id;
	private String Name;
	private String Gender;
	private String RoomID;

	private Double Age;

	public PatientObject() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public String getID() {
		return Id;
	}

	public void setID(String ID) {
		this.Id = ID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Double getAge() {
		return Age;
	}

	public void setAge(Double age) {
		Age = age;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getRoomID() {
		return RoomID;
	}

	public void setRoomID(String roomID) {
		RoomID = roomID;
	}
}