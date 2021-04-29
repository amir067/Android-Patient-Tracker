package com.my.AndroidPatientTracker.ui.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment2 extends Fragment {

    private static final String TAG = "ProfileFragment2";


    //Binding
    @BindView(R.id.cv_profile)
    CardView userProfileCV;

    @BindView(R.id.iv_user_dp)
    ImageView userProfileIV;

    //UI
    private TextView userName;
    private TextView email;
    private TextView phone;
    private TextView address;
    private TextView bio;
    private ImageView userImage;
    private Button logoutButton;

    //PermissionsCode
    public static final int IMAGE_SELECT_CODE = 1001;
    //StoreCodes
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Helpers
    private PreferenceHelperDemo preferenceHelperDemo;
    private NavController navController;
    private int backPress = 0;

    // user
    private String usr_name;
    private String usr_phone;
    private String usr_Address;
    private String usr_biography;
    private String imageURL;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    //Edit Profile
    private EditText et_Name;
    private EditText et_Phone;
    private EditText et_Address;
    private EditText et_biography;

    private ImageView update_imageView;
    private Uri image_uri = null;
    private BottomSheetDialog bottomSheetDialog;

    //Strings
    private String s_email ;
    private String s_name ;
    private String s_phone ;
    private String s_address ;
    private String s_biography ;

    //Loading Containers
    LottieAnimationView loadingAnimationViewDots;
    LottieAnimationView loadingAnimationViewBio;
    private ProgressBar progressBar;
    private AlertDialog loading_dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
       // getActivity().setTheme(R.style.AppTheme);
        super.onViewCreated(view, savedInstanceState);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        loading_dialog = MyUtils.getLoadingDialog(requireActivity());
        logoutButton = view.findViewById(R.id.btn_sign_out);
        loadingAnimationViewDots = view.findViewById(R.id.loading_animation_view_full);
        loadingAnimationViewBio = view.findViewById(R.id.loading_animation_view_bio);

        progressBar= view.findViewById(R.id.pb_profile);
        userName= view.findViewById(R.id.tv_user_name);
        userImage= view.findViewById(R.id.iv_user_dp);
        bio= view.findViewById(R.id.tv_user_bio);
        email= view.findViewById(R.id.tv_email);
        phone= view.findViewById(R.id.tv_phone);
        address= view.findViewById(R.id.tv_address);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

         preferenceHelperDemo = new PreferenceHelperDemo(requireContext());
         navController = Navigation.findNavController(view);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        getUserData();
    }

    private void getUserData() {

        loadingAnimationViewDots.setVisibility(View.VISIBLE);
        //loading_dialog.show();
        //progressBar.setVisibility(View.VISIBLE);


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        Log.e(TAG, "userid: "+userid);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");

            rootRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        progressBar.setVisibility(View.GONE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnimationViewDots.setVisibility(View.GONE);
                            }
                        }, 1000); // for 3 second

                        try{

                            UserModel userModel = snapshot.getValue(UserModel.class);

                            if(!userModel.getName().isEmpty()){
                                userName.setText(userModel.getName());
                                usr_name= userModel.getName();
                                s_name= userModel.getName();
                                Log.i(TAG, "parsing-Check1: "+usr_name);
                            }
                            if(userModel.getEmail() !=null){
                                email.setText(userModel.getEmail());
                                s_email= userModel.getEmail();
                                Log.i(TAG, "parsing-Check2: "+s_email);
                            }
                            if(userModel.getPhone() !=null){
                                phone.setText(userModel.getPhone());
                                s_phone= userModel.getPhone();
                                Log.i(TAG, "parsing-Check3: "+s_phone);
                            }
                            if(userModel.getAddress() !=null){
                                address.setText(userModel.getAddress());
                                s_address= userModel.getAddress();
                                Log.i(TAG, "parsing-Check4: "+s_address);
                            }


                            if(userModel.getUserBio() != null){
                                s_biography= userModel.getUserBio();
                                Log.i(TAG, "parsing-Check5: "+s_biography);

                                loadingAnimationViewBio.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingAnimationViewBio.setVisibility(View.GONE);
                                        bio.setText(userModel.getUserBio());
                                    }
                                }, 1000); // for 3 second


                            }else{
                                loadingAnimationViewBio.setVisibility(View.GONE);
                            }

                            if(userModel.getProfileUrl() != null){
                                Log.i(TAG, "parsing-Check5: "+s_biography);

                                imageURL=userModel.getProfileUrl();

                                userProfileCV.setAlpha(0);
                                userProfileCV.setVisibility(View.VISIBLE);
                                userProfileCV.animate().alpha(1).setDuration(1000).start();

                                    Glide.with(requireContext())
                                            .load( userModel.getProfileUrl() )////getResources().getDrawable(R.drawable.ic_std_avatar_male)
                                            .placeholder(R.drawable.ic_wait_white)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .error(R.drawable.ic_doctor_mask)
                                            .into(userProfileIV);

                            }else{

                                userProfileCV.setVisibility(View.VISIBLE);

                                if(userModel.getGender() !=null){

                                    if(userModel.getGender().equals("Male")){
                                        userProfileIV.setImageResource(R.drawable.ic_std_avatar_male);
                                    }
                                    else{
                                        userProfileIV.setImageResource(R.drawable.ic_std_avatar_female);
                                    }
                                }else{
                                    userProfileIV.setImageResource(R.drawable.ic_std_avatar_male);
                                }


                            }



                        }
                        catch (Exception e){
                            Log.e(TAG, "Exception onDataChange: "+e.getLocalizedMessage() );

                        }

                        // Exist! Do something.
                        // Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "snapshot value : "+snapshot.getValue());


                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Don't exist! Do something.
                        Log.e(TAG, "onDataChange: user profile not exits in DB" );
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnimationViewBio.setVisibility(View.GONE);
                                loadingAnimationViewDots.setVisibility(View.GONE);
                            }
                        }, 1000); // for 3 second
                        Toast.makeText(requireContext(), "user profile not exits ", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.getDetails() );
                }

            });

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        navController.navigate(R.id.LoginActivity);

    }

    @OnClick(R.id.btn_et)
    public void editProfile() {

        Log.i(TAG, "editProfile: ");

        @SuppressLint("InflateParams") View view1 = this.getLayoutInflater().inflate(R.layout.frag_edit_profile2, null);

        et_Name = view1.findViewById(R.id.et_Name);
        et_Name.setText(s_name);

        et_Address = view1.findViewById(R.id.et_address);
        et_Address.setText(s_address);

        et_Phone = view1.findViewById(R.id.et_Phone);
        et_Phone.setText(s_phone);

        et_biography = view1.findViewById(R.id.et_bio);
        et_biography.setText(s_biography);

        TextView tv_email = view1.findViewById(R.id.tv_email);
        tv_email.setText(s_email);

        update_imageView = view1.findViewById(R.id.update_imageView);
        update_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        if(imageURL !=null){
            Glide.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.ic_wait_white)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.bg_no_image)
                    .into(update_imageView);
        }


        Button button = view1.findViewById(R.id.btnSaveButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_name = et_Name.getText().toString();
                s_phone = et_Phone.getText().toString();
                s_address = et_Address.getText().toString();
                s_biography = et_biography.getText().toString();

                if (TextUtils.isEmpty(s_name)) {
                    et_Name.setError("Enter Name");
                    et_Name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_phone)) {
                    et_Phone.setError("Enter Phone Number");
                    et_Phone.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_address)) {
                    et_Address.setError("Enter Address");
                    et_Address.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_biography)) {
                    et_biography.setError("Enter some about yourself");
                    et_biography.requestFocus();
                    return;
                }

                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String userid = firebaseUser.getUid();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                loading_dialog.show();
                loading_dialog.setTitle("Please wait");
                loading_dialog.setCancelable(false);

                if (image_uri != null) {

                    mStorageRef.child("users/" + userid).putFile(image_uri)

                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(TAG, "onViewClicked: " + "photo upload");

                                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()

                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Map<String, Object> blog_update = new HashMap<>();
                                                    blog_update.put("name", s_name);
                                                    blog_update.put("phone", s_phone);
                                                    blog_update.put("address", s_address);
                                                    blog_update.put("userBio", s_biography);
                                                    blog_update.put("profileImageUrl", uri.toString());
                                                    blog_update.put("updated_at", new Date().getTime());

                                                    rootRef.updateChildren(blog_update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            loading_dialog.dismiss();
                                                            bottomSheetDialog.dismiss();
                                                            Log.d(TAG, "onSuccess: update ");
                                                            et_Name.setText("");
                                                            et_Phone.setText("");
                                                            et_Address.setText("");
                                                            et_biography.setText("");

                                                            getUserData();

                                                            Toast.makeText(requireContext(), "Update Profile Success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    loading_dialog.dismiss();
                                                                    if (e instanceof IOException)
                                                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                                                    else
                                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                                }
                                                            });

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loading_dialog.dismiss();
                                                    if (e instanceof IOException)
                                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                }
                                            });
                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });

                } else {

                    Map<String, Object> blog_update = new HashMap<>();
                    blog_update.put("name", s_name);
                    blog_update.put("phone", s_phone);
                    blog_update.put("address", s_address);
                    blog_update.put("userBio", s_biography);
                    blog_update.put("updated_at", new Date().getTime());

                    rootRef.updateChildren(blog_update)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loading_dialog.dismiss();
                                    bottomSheetDialog.dismiss();
                                    Log.d(TAG, "onSuccess: update ");

                                    getUserData();

                                    et_Name.setText("");
                                    et_Phone.setText("");
                                    et_Address.setText("");
                                    et_biography.setText("");

                                    Toast.makeText(requireContext(), "Profile Updated Successfully ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });

                }

            }
        });
        DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height*0.80);


        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
        bottomSheetDialog.getBehavior().setPeekHeight(maxHeight);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCancelable(true);




    }

    private void pickImage() {
        requestStoragePermission();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
            requestStoragePermission();

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Toast.makeText(requireContext(), "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    update_imageView.setImageResource(R.drawable.bg_no_image);
                    return;
                }
                image_uri = data.getData();
                {
                    Log.i(TAG, "onActivityResult: setting image");
                    update_imageView.setImageURI(image_uri);


                }
            } else {
                image_uri = null;
                update_imageView.setImageResource(R.drawable.ic_std_avatar_male);
            }
        }
    }


}