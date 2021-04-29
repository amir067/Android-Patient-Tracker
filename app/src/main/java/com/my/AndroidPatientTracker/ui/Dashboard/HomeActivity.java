package com.my.AndroidPatientTracker.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.my.AndroidPatientTracker.R;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseUser user;
    private  NavController navController;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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