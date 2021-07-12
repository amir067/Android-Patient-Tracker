package com.my.AndroidPatientTracker.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.listners.AlLoginHandler;
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.my.AndroidPatientTracker.ui.Authentication.LoginActivity;
import com.my.AndroidPatientTracker.ui.Authentication.NotApprovedPageActivity;

import butterknife.ButterKnife;
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


    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        ButterKnife.bind(this);
        Applozic.init(HomeActivity.this, getResources().getString(R.string.APPLOZIC_APP_ID));
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        //Tools.setSystemBarColor(this,R.color.grey_40);
        getSupportActionBar().hide();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration
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

                                if(isApproved){
                                    checkRegisteredForChat( userModel);

                                    if(userModel.getUserType()!=null){
                                        if(userModel.getUserType().equals("patient")){
                                            Log.e(TAG, "user is patient so removing room option" );
                                            bottomNavigationView.getMenu().removeItem(R.id.navigation_room_fragment);
                                            bottomNavigationView.getMenu().getItem(1).setTitle("Doctors");
                                        }

                                    }
                                }

                            }catch (Exception e){
                                Log.e(TAG, "onDataChange: Exception: "+e.getMessage() );
                            }

                        }else{
                            Toasty.error(HomeActivity.this,"Your account is removed by admin",Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                            doLogoutOnApplozic();

                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
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

    private void doLogoutOnApplozic() {

        Applozic.logoutUser(HomeActivity.this, new AlLogoutHandler() {
            @Override
            public void onSuccess(Context context) {
                Log.e(TAG, "onSuccess: doLogoutOnApplozic" );
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure: doLogoutOnApplozic" );

            }
        });


    }

    private void checkRegisteredForChat(UserModel userModel) {
        Log.e(TAG, "checkRegisteredForChat: called" );

        if(userModel.isRegisteredForChat()){
            Log.e(TAG, "user is registed on applozic " );
            doRegistedUserOnAppolic(userModel);
        }else{
            Log.e(TAG, "user is  not registed on applozic: " );
            doRegistedUserOnAppolic(userModel);
        }

    }

    private void doRegistedUserOnAppolic(UserModel userModel) {
        Log.e(TAG, "doRegistedUserOnAppolic: called" );

        User user = new User();
        user.setUserId(userModel.getId()); //userId it can be any unique user identifier
        user.setDisplayName(userModel.getName()); //displayName is the name of the user which will be shown in chat messages
        user.setEmail(userModel.getEmail()); //optional
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());  //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password
        user.setPassword(""); //optional, leave it blank for testing purpose, read this if you want to add additional security by verifying password from your server https://www.applozic.com/docs/configuration.html#access-token-url

        if(userModel.getProfileImageUrl() !=null){
            if(userModel.getProfileImageUrl().equals("default")){
                user.setImageLink("");//optional,pass your image link
            }else{
                user.setImageLink(userModel.getProfileImageUrl());//optional,pass your image link
            }
        }else{
            user.setImageLink("");//optional,pass your image link
        }

        if (Applozic.isConnected(HomeActivity.this)) {
            Log.e(TAG, "doRegistedUserOnAppolic: user is alredy logged in" );

        }

        {
            Log.e(TAG, "doRegistedUserOn Appolizc: logging user..." );
            Applozic.connectUser(HomeActivity.this, user, new AlLoginHandler() {
                @Override
                public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                    // After successful registration with Applozic server the callback will come here
                    registerForNotification(registrationResponse);
                    UpdateUserStatusForChats();
                }

                @Override
                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                    // If any failure in registration the callback  will come here
                }
            });
        }



    }

    private void UpdateUserStatusForChats() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).child("registeredForChat").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(HomeActivity.this,"Registered for chats Successfully",Toasty.LENGTH_SHORT).show();
            }
        });

    }

    private void registerForNotification(RegistrationResponse registrationResponse) {
        if(MobiComUserPreference.getInstance(HomeActivity.this).isRegistered()) {

            Applozic.registerForPushNotification(HomeActivity.this, Applozic.getInstance(HomeActivity.this).getDeviceRegistrationId(), new AlPushNotificationHandler() {//registrationToken
                @Override
                public void onSuccess(RegistrationResponse registrationResponse) {
                    Log.e(TAG, "onSuccess: registerForPushNotification" );
                }

                @Override
                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                    Log.e(TAG, "onFailure: registerForPushNotification" );
                }
            });
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