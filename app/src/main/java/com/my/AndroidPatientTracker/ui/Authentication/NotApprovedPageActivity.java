package com.my.AndroidPatientTracker.ui.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.ui.Activities.ProfileActivity;
import com.my.AndroidPatientTracker.ui.Dashboard.HomeActivity;
import com.my.AndroidPatientTracker.utils.Tools;


public class NotApprovedPageActivity extends AppCompatActivity {
    private static final String TAG = "NotApprovedPageActivity";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notapproved_page);
        getSupportActionBar().hide();
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("Users").child(userid);

            get_company_name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."

                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()) {
                        Log.e(TAG, "onDataChange: user data exits" );
                        //String u_name = (String) snapshot.getValue();

                        if(snapshot.child("userApproved").exists()){
                            Log.e(TAG, "userApproved value exits? value:" +snapshot.child("userApproved").getValue());
                            if((Boolean) snapshot.child("userApproved").getValue()){
                                startActivity(new Intent(NotApprovedPageActivity.this, HomeActivity.class));
                                NotApprovedPageActivity.this.finish();
                            }
                        }

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, "onCreate: Exception: "+e.getMessage() );
        }

    }


    public void onBackPressed(){
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void onExit(View v){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void onLogOutClicked(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent( NotApprovedPageActivity.this, LoginActivity.class));
         NotApprovedPageActivity.this.finish();
    }

    public void viewProfile(View v){
        startActivity(new Intent( NotApprovedPageActivity.this, ProfileActivity.class));
       // NotApprovedPageActivity.this.finish();
    }

}
