package com.my.AndroidPatientTracker.ui.FindDoctors;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.AndroidPatientTracker.Interface.ItemClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.DoctorsModel;
import com.my.AndroidPatientTracker.ui.Activities.HomeActivity;
import com.my.AndroidPatientTracker.ui.Activities.ProfileActivity;
import com.my.AndroidPatientTracker.viewholder.DoctorsViewHolder;


public class RecycleDoctorsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView recycle_all_doctors;
    private RecyclerView.LayoutManager layoutManager;
   private FirebaseRecyclerAdapter<DoctorsModel, DoctorsViewHolder> adapter;
    private FirebaseDatabase Doctors_database;
    private DatabaseReference Doctors_references;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_doctors);
        // init Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        Doctors_database = FirebaseDatabase.getInstance();
        Doctors_references = database.getReference("Doctors");
        recycle_all_doctors = findViewById(R.id.recyclerView);
        recycle_all_doctors.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycle_all_doctors.setLayoutManager(layoutManager);

        loadlist();
    }



    private void loadlist() {
        adapter = new FirebaseRecyclerAdapter<DoctorsModel, DoctorsViewHolder>(
                DoctorsModel.class,
                R.layout.doctor_item,
                DoctorsViewHolder.class,
                Doctors_references
        ) {
            @Override
            protected void populateViewHolder(DoctorsViewHolder doctorsViewHolder, DoctorsModel doctorsModel, int position) {

                doctorsViewHolder.doctor_name.setText(doctorsModel.getName());
                doctorsViewHolder.doctor_Category.setText(doctorsModel.getCategory());
                doctorsViewHolder.doctor_experence.setText(doctorsModel.getExperence());
                doctorsViewHolder.doctor_address.setText(doctorsModel.getAddress());
                doctorsViewHolder.doctor_Fee.setText("Rs: "+doctorsModel.getFee());
                doctorsViewHolder.doctor_rating.setRating(doctorsModel.getRating());


                final DoctorsModel current_doctor;
                doctorsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                       // adapter.getRef(position).getKey()
                        //Toast.makeText(RecycleDoctorsActivity.this, " clicked ", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecycleDoctorsActivity.this);
                        final CharSequence[] items = {"Book Appointment", "Cancel"};
                        builder.setTitle("Select The Action");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if(item==0){
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+doctorsModel.getPhone()));
                                    startActivity(intent);
                                    //adapter.getRef(position).getKey())
                                    //Toast.makeText(HomeActivity.this,"item Address: "+adapter.getRef(click_position).getKey(),Toast.LENGTH_SHORT).show();
                                   //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                   // mDatabase.child("companies").child(adapter.getRef(click_position).getKey()).removeValue();
                                   // Toast.makeText(HomeActivity.this,"Company Deleted! ",Toast.LENGTH_SHORT).show();
                                    // DatabaseReference  company_to_delete = FirebaseDatabase.getInstance().getReference("companies").child(adapter.getRef(click_position).toString());
                                    // company = FirebaseDatabase.getInstance().getReference("companies").child(adapter.getRef(click_position).toString()).
                                }
                                else  if(item==1){
                                    Toast.makeText(RecycleDoctorsActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onLongClick(View view, int position, boolean isLongClick) {
                       // Toast.makeText(RecycleDoctorsActivity.this, "long clicked ", Toast.LENGTH_SHORT).show();
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

    public void btn_guide_click(View v){
      //  this.startActivity(new Intent(v.getContext(), GuidePageActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }

    public void btn_seting_click(View v){
        startActivity(new Intent(this, RecycleDoctorsActivity.class) );
        finish();
    }
    public void btn_profile_click(View v){
        // startActivity(new Intent(this, ProfileActivity.class) );
        this.startActivity(new Intent(v.getContext(), ProfileActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }


}