package com.my.AndroidPatientTracker.ui.Activities;

import android.content.Intent;
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
import com.my.AndroidPatientTracker.models.DoctorsModel;
import com.my.AndroidPatientTracker.ui.FindDoctors.RecycleDoctorsActivity;
import com.my.AndroidPatientTracker.viewholder.DoctorsViewHolder;


//import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class InfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView recycle_all_doctors;
    private RecyclerView.LayoutManager layoutManager;
   private FirebaseRecyclerAdapter<DoctorsModel, DoctorsViewHolder> adapter;
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



    private void loadlist() {
        adapter = new FirebaseRecyclerAdapter<DoctorsModel, DoctorsViewHolder>(
                DoctorsModel.class,
                R.layout.doctor_item,
                DoctorsViewHolder.class,
                Doctors_references.orderByChild("category").equalTo(doctor_category)
        ) {
            @Override
            protected void populateViewHolder(DoctorsViewHolder doctorsViewHolder, DoctorsModel doctorsModel, int position) {

                doctorsViewHolder.doctor_name.setText(doctorsModel.getName());
                doctorsViewHolder.doctor_Category.setText(doctorsModel.getCategory());
                doctorsViewHolder.doctor_experence.setText(doctorsModel.getExperence());
                doctorsViewHolder.doctor_address.setText(doctorsModel.getAddress());
                doctorsViewHolder.doctor_rating.setRating(doctorsModel.getRating());


                final DoctorsModel current_doctor;
                doctorsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                       // adapter.getRef(position).getKey()
                        Toast.makeText(InfoActivity.this, " clicked ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(InfoActivity.this, "long clicked ", Toast.LENGTH_SHORT).show();
                    }
                });


            }



        };

        recycle_all_doctors.setAdapter(adapter);
    }



    public void btn_home_click(View v){
        this.startActivity(new Intent(v.getContext(), HomeActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }

    public void btn_all_doctor_click(View v){
        this.startActivity(new Intent(v.getContext(), RecycleDoctorsActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }

    public void btn_seting_click(View v){
        startActivity(new Intent(this, InfoActivity.class) );
        finish();
    }
    public void btn_profile_click(View v){
        // startActivity(new Intent(this, ProfileActivity.class) );
        this.startActivity(new Intent(v.getContext(), ProfileActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }


}