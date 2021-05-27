package com.my.AndroidPatientTracker.ui.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.AndroidPatientTracker.Interface.ItemClickListener;
import com.my.AndroidPatientTracker.R;

import com.my.AndroidPatientTracker.viewholder.DoctorsViewHolder;


//import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class InfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView recycle_all_doctors;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase Doctors_database;
    private DatabaseReference Doctors_references;


    String doctor_category = "";

    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infowepage);
        // init Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        Doctors_database = FirebaseDatabase.getInstance();
        Doctors_references = database.getReference("Doctors");


                heading= findViewById(R.id.textView17);


        {
            // Toast.makeText(CompanyTasksShowActivity.this, " Intent receved : "+company_address, Toast.LENGTH_SHORT).show();
            heading.setText("Information");
        }


       // loadlist();
    }









}