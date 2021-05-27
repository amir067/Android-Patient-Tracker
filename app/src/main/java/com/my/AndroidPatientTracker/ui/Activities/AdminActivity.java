package com.my.AndroidPatientTracker.ui.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.my.AndroidPatientTracker.Interface.ItemClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.utils.Tools;
import com.my.AndroidPatientTracker.viewholder.UserRequestViewHolder;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AdminActivity extends AppCompatActivity {
    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.total_users)
    TextView totallUsers;

    @BindView(R.id.approved_users)
    TextView approveUser;

    @BindView(R.id.maincard)
    CardView main_card;

    private AlertDialog loading_dialog;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference user_payment;

    private long time,amount,transion_id;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RecyclerView recycle_transactions;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<UserModel, UserRequestViewHolder> adapter;

    private long update_date;
    //private UserModel usermodel;
    String user_phone_number;
    long current_balance;
    private static final String TAG = "AdminActivity";


    //Data
    int totalMechanics=0;
    int totalApprovedMechanics=0;

   private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
         ButterKnife.bind(this);

        user_name = findViewById(R.id.user_name);
        totallUsers = findViewById(R.id.total_users);
        approveUser = findViewById(R.id.approved_users);

        recycle_transactions = findViewById(R.id.recycle_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this,R.color.colorAccent);
       // String userid = firebaseUser.getUid();

        mAuth = FirebaseAuth.getInstance();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        user_name.startAnimation(alphaAnimation);



       // String userid = firebaseUser.getUid();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        //Toast.makeText(PaymentActivity.this,"Current User Id: "+userid,Toast.LENGTH_SHORT).show();

        user_payment = database.getReference("Users");
        //loading_dialog = MyUtils.getLoadingDialog(this);


        recycle_transactions.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycle_transactions.setLayoutManager(layoutManager);

        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            get_company_name.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    String u_name = (String) snapshot.getValue();
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (u_name!=null) {
                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+companyname, Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CompanyTasksShowActivity.this, firsttimeprofile.class));
                        //company_name_show.setText(value);
                        user_name.setText(u_name);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AdminActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }



        LoadAnalitysic();
        Loaddata();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void LoadAnalitysic() {
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("Users");
            get_company_name.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    //String u_name = (String) snapshot.getValue();
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()) {
                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+companyname, Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CompanyTasksShowActivity.this, firsttimeprofile.class));
                        //company_name_show.setText(value);

                        for(DataSnapshot datas: snapshot.getChildren()){

                            if(datas.child("userType").exists()){
                                if(datas.child("userType").getValue().toString().equals("doctor")){
                                    totalMechanics=(totalMechanics+1);
                                }
                            }
                        }
                        totallUsers.setText(""+totalMechanics);

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AdminActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("Users");
            get_company_name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    //String u_name = (String) snapshot.getValue();
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()) {
                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+companyname, Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CompanyTasksShowActivity.this, firsttimeprofile.class));
                        //company_name_show.setText(value);
                        approveUser.setText(""+snapshot.getChildrenCount());


                        for(DataSnapshot datas: snapshot.getChildren()){

                            if(datas.child("userType").exists()){
                                if(datas.child("userType").getValue().equals("doctor")){

                                    if(datas.child("userApproved").exists()){

                                        if((boolean)datas.child("userApproved").getValue()){
                                            totalApprovedMechanics=(totalApprovedMechanics+1);
                                        }
                                    }else{

                                    }


                                }
                            }
                        }
                        approveUser.setText(""+totalApprovedMechanics);


                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AdminActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }





    }

    private void Loaddata() {
        adapter = new FirebaseRecyclerAdapter<UserModel, UserRequestViewHolder>(
                UserModel.class,
                R.layout.item_view_user_request,
                UserRequestViewHolder.class,
                user_payment.orderByChild("userType").equalTo("doctor")
        ) {
            @Override
            protected void populateViewHolder(UserRequestViewHolder viewHolder, UserModel model, final int position) {
                update_date = model.getCreated_at();
                if(update_date!=0){
                    // long update_date = model.getUpdated_at().toDate().getTime();
                    String updated = DateUtils.getRelativeTimeSpanString(update_date).toString();
                    viewHolder.time.setText(updated);
                }

               boolean status=model.isUserApproved();
                if(status){
                    //Toast.makeText(PaymentActivity.this,"status is empty: "+status,Toast.LENGTH_SHORT).show();
                    viewHolder.reqStatus.setText("Approved");
                }
                else
                {
                    //Toast.makeText(PaymentActivity.this,"status is not empty: "+status,Toast.LENGTH_SHORT).show();
                    viewHolder.reqStatus.setText("Pending");
                }
                String imgUrl = model.getProfileUrl();
                if(TextUtils.isEmpty(imgUrl)){

//
                    Log.i(TAG, "populateViewHolder:Glide user image url"+model.getProfileUrl());
                    //Toast.makeText(PaymentActivity.this,"status is empty: "+status,Toast.LENGTH_SHORT).show();
                }else{
                    Picasso.get().load(model.getProfileUrl())
                           .into(viewHolder.user_image);
                }

                viewHolder.userName.setText(model.getName());

                if(model.getPhone() !=null){
                    viewHolder.user_phone.setText(""+model.getPhone());
                }

                if(model.getAddress() !=null){
                   // viewHolder.userAddress.setText(""+model.getAddress());
                }

               // user_number.setText(model.getUser_phone());
                final UserModel userRequestModel =  model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {

                        if(model.isUserApproved()){
                            Toasty.info( AdminActivity.this,"Already approved",Toasty.LENGTH_SHORT).show();
                        }else{
                            //Toast.makeText(PaymentActivity.this,"Long Clicked",Toast.LENGTH_SHORT).show();
                            final CharSequence[] items = {"Approve","DisApprove", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder( AdminActivity.this);
                            builder.setTitle("Select The Action");
                            builder.setCancelable(false);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if(item==0){
                                        mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        String userid = firebaseUser.getUid();

                                        //
                                        approveItem(adapter.getRef(position).getKey());

                                    }else if(item==1){
                                        dissApproveItem(adapter.getRef(position).getKey());
                                    }
                                    else if(item==2){
                                        //adapter.getRef(position).getKey())
                                        //Toast.makeText(HomeActivity.this,"item Address: "+adapter.getRef(click_position).getKey(),Toast.LENGTH_SHORT).show();
                                        Toast.makeText( AdminActivity.this,"Canceled", Toast.LENGTH_SHORT).show();
                                        // DatabaseReference  company_to_delete = FirebaseDatabase.getInstance().getReference("companies").child(adapter.getRef(click_position).toString());
                                        // company = FirebaseDatabase.getInstance().getReference("companies").child(adapter.getRef(click_position).toString()).
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        }

                    }
                    @Override
                    public void onLongClick(View view, final int click_position, boolean isLongClick) {

                        //Toast.makeText(PaymentActivity.this,"Long Clicked",Toast.LENGTH_SHORT).show();
                        final CharSequence[] items = {"Delete", "Cancel"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                        builder.setTitle("Select The Action");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if(item==0){
                                    mAuth = FirebaseAuth.getInstance();
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String userid = firebaseUser.getUid();
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                                    mDatabase.child("Users").child(adapter.getRef(click_position).getKey()).removeValue();
                                    Toast.makeText(AdminActivity.this,"Item Deleted! ", Toast.LENGTH_SHORT).show();
                                }
                                else if(item==1){
                                    //adapter.getRef(position).getKey())
                                    //Toast.makeText(HomeActivity.this,"item Address: "+adapter.getRef(click_position).getKey(),Toast.LENGTH_SHORT).show();
                                    Toast.makeText(AdminActivity.this,"Canceled", Toast.LENGTH_SHORT).show();
                                    // DatabaseReference  company_to_delete = FirebaseDatabase.getInstance().getReference("companies").child(adapter.getRef(click_position).toString());
                                    // company = FirebaseDatabase.getInstance().getReference("companies").child(adapter.getRef(click_position).toString()).
                                }
                            }
                        });
                        builder.show();

                        //return true;
                    }
                });

            }
        };
        recycle_transactions.setAdapter(adapter);
    }

    private void dissApproveItem(String key) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("Users").child(key);
            get_company_name.child("userApproved").addListenerForSingleValueEvent(new ValueEventListener() {
                String reqStatus;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    reqStatus =(String) snapshot.getValue();

                    if(snapshot.exists()){
                        snapshot.getRef().setValue(false);
                        Toasty.success(AdminActivity.this,"User Disapproved",Toasty.LENGTH_SHORT).show();

                    }else{
                        Toasty.error(AdminActivity.this,"Error While Disapproved User",Toasty.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, "approveItem: "+e.getLocalizedMessage());

        }
    }

    private void approveItem(String userid ) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            get_company_name.child("userApproved").addListenerForSingleValueEvent(new ValueEventListener() {
                Boolean reqStatus;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    reqStatus =(Boolean) snapshot.getValue();

                    if(snapshot.exists()){
                        snapshot.getRef().setValue(true);
                        Toasty.success(AdminActivity.this,"User Approved",Toasty.LENGTH_SHORT).show();

                        LoadAnalitysic();//Referesh Analitysic UI data

                    }else{
                        Toasty.error(AdminActivity.this,"Error While Approving User",Toasty.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, "approveItem: "+e.getLocalizedMessage());

        }

    }


    public void  balanct_setzero( ){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User").child(userid);
            get_company_name.child("user_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                long value;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //value =(long) snapshot.getValue();
                    value = 0;
                    snapshot.getRef().setValue(value);
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
          //  Toast.makeText(PaymentActivity.this, "try catch error: balance  set zero", Toast.LENGTH_SHORT).show();
        }


    }


    public void showAlertDialog(View view) {
        ButterKnife.bind(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Payment Request ?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            Snackbar.make(view, "Confirm clicked", Snackbar.LENGTH_SHORT).show();


        });
        builder.setNegativeButton("Cancel", null);
        builder.show();



    }







    public void showCustomDialog(View view) {
        ButterKnife.bind(this);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
       // dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



        dialog.show();
        dialog.getWindow().setAttributes(lp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity
                dialog.dismiss();
            }
        }, 5000); // for 3 second
    }
}