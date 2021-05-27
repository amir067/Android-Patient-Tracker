package com.my.AndroidPatientTracker.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Authentication.NotApprovedPageActivity;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseUser user;
    private  NavController navController;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public  Boolean isAdmin = false;
    public   Boolean isApproved = false;
    public UserModel userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        //Tools.setSystemBarColor(this,R.color.grey_40);
        getSupportActionBar().hide();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.navigation_home_fragment,R.id.navigation_view_patients_fragment,R.id.navigation_room_fragment,R.id.navigation_profile_fragment).build();

        navController = Navigation.findNavController(this, R.id.navigation_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        getUserInfo();
    }

    private void getUserInfo() {
        Log.e(TAG, "getUserInfo: getting ... inside HOmeActivity" );



        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser !=null){
            String userid = firebaseUser.getUid();
            try{
                DatabaseReference get_company_name;
                get_company_name = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                get_company_name.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."

                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                        if (snapshot.exists()) {
                            Log.e(TAG, "onDataChange: user data exits" );
                            //String u_name = (String) snapshot.getValue();

                            try{

                                userModel = snapshot.getValue(UserModel.class);
                                Log.e(TAG, "onDataChange: userObjectNew: "+userModel.getName() );
                                isAdmin = userModel.isAdmin();
                                isApproved = userModel.isUserApproved();
                                doCheckVerficationStatus(isApproved);
                            }catch (Exception e){
                                Log.e(TAG, "onDataChange: Exception: "+e.getMessage() );
                            }

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            catch (Exception e){
                Toast.makeText(HomeActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toasty.error(this,"Login Again",Toasty.LENGTH_SHORT).show();
        }

    }

    private void doCheckVerficationStatus(boolean status) {

        if(status){
            Log.e(TAG, "doCheckVerficationStatus: user is approved" );
        }else{
            startActivity(new Intent(HomeActivity.this, NotApprovedPageActivity.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            super.onBackPressed();
            //Toast.makeText(this, "OnBAckPressed Works", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }






}