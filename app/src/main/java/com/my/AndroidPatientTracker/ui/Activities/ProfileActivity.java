package com.my.AndroidPatientTracker.ui.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Authentication.LoginActivity;

import com.my.AndroidPatientTracker.utils.MyUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ProfileActivity extends AppCompatActivity {

    public static final int IMAGE_SELECT_CODE = 1001;
    @BindView(R.id.profile_pic_imageView)
    ImageView profilePicImageView;
    @BindView(R.id.profile_username)
    TextView profileUsername;
    @BindView(R.id.profile_email)
    TextView profileEmail;
    @BindView(R.id.profile_phone)
    TextView profilePhone;
    @BindView(R.id.profile_gender)
    TextView profileGender;

    private FirebaseAuth auth;

    private Uri image_uri = null;

    private BottomSheetDialog bottomSheetDialog;

    String age,email,gender,id,imageURL,password,phone,username;

    private static final int STORAGE_PERMISSION_CODE = 123;

    private ImageView update_imageView;

    private AlertDialog loading_dialog;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private static final String TAG = "ProfileActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        loading_dialog = MyUtils.getLoadingDialog(this);
        bottomSheetDialog = new BottomSheetDialog(this);

        profile();

    }


    public void onLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onEditProfileClick(View view) {

        @SuppressLint("InflateParams") View view1 = this.getLayoutInflater().inflate(R.layout.activity_edit_profile, null);

        EditText et_Name = view1.findViewById(R.id.et_Name);
        et_Name.setText(username);

        EditText et_Phone = view1.findViewById(R.id.et_Phone);
        et_Phone.setText(phone);

        TextView tv_email = view1.findViewById(R.id.tv_email);
        tv_email.setText(email);

        update_imageView = view1.findViewById(R.id.update_imageView);
        update_imageView.setOnClickListener(v -> pickImage());

        Glide.with(this)
                .load(imageURL)
                .placeholder(R.drawable.picture_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.defavatar)
                .into(update_imageView);

        Button button = view1.findViewById(R.id.btnSaveButton);

        button.setOnClickListener(v -> {

            String s_name = et_Name.getText().toString();
            String s_phone = et_Phone.getText().toString();

            if (TextUtils.isEmpty(s_name)) {
                et_Name.setError("");
                et_Name.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(s_phone)) {
                et_Phone.setError("");
                et_Phone.requestFocus();
                return;
            }

            DocumentReference blogRef = db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            loading_dialog.show();
            loading_dialog.setTitle("Please wait");
            loading_dialog.setCancelable(false);

            if (image_uri != null) {

                mStorageRef.child("users/" + UUID.randomUUID()).putFile(image_uri)

                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d(TAG, "onViewClicked: " + "photo upload");

                            Objects.requireNonNull(Objects.requireNonNull(taskSnapshot
                                    .getMetadata()).getReference()).getDownloadUrl()

                                    .addOnSuccessListener(uri -> {

                                        Map<String, Object> blog_update = new HashMap<>();
                                        blog_update.put("username", s_name);
                                        blog_update.put("phone", s_phone);
                                        blog_update.put("imageURL", uri.toString());
                                        blog_update.put("updated_at", new Date());

                                        blogRef.update(blog_update)
                                                .addOnSuccessListener(aVoid -> {
                                                    loading_dialog.dismiss();
                                                    bottomSheetDialog.dismiss();
                                                    Log.d(TAG, "onSuccess: update ");
                                                    et_Name.setText("");
                                                    et_Phone.setText("");
                                                    profile();
                                                    Toast.makeText(this, "Update Profile Success", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    loading_dialog.dismiss();
                                                    if (e instanceof IOException)
                                                        Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                });

                                    })
                                    .addOnFailureListener(e -> {
                                        loading_dialog.dismiss();
                                        if (e instanceof IOException)
                                            Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                        else
                                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                    });
                        })

                        .addOnFailureListener(e -> {
                            loading_dialog.dismiss();
                            if (e instanceof IOException)
                                Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                            else
                                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        });

            } else {

                Map<String, Object> blog_update = new HashMap<>();
                blog_update.put("username", s_name);
                blog_update.put("phone", s_phone);
                blog_update.put("updated_at", new Date());

                blogRef.update(blog_update)

                        .addOnSuccessListener(aVoid -> {
                            loading_dialog.dismiss();
                            bottomSheetDialog.dismiss();
                            Log.d(TAG, "onSuccess: update ");
                            profile();
                            et_Name.setText("");
                            et_Phone.setText("");
                            Toast.makeText(this, "Update Profile Success", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            loading_dialog.dismiss();
                            if (e instanceof IOException)
                                Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                            else
                                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        });

            }

        });

        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCancelable(true);
    }

    private void pickImage() {
        requestStoragePermission();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Toast.makeText(this, "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    update_imageView.setImageResource(R.drawable.defavatar);
                    return;
                }
                image_uri = data.getData();
                update_imageView.setImageURI(image_uri);
            } else {
                image_uri = null;
                update_imageView.setImageResource(R.drawable.defavatar);
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            requestStoragePermission();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void profile() {

        DocumentReference docRef = db.collection("users").document(auth.getCurrentUser().getUid());

        docRef.get().addOnSuccessListener(documentSnapshot -> {


            if (!documentSnapshot.exists()) {
                Log.d(TAG, "onSuccess: LIST EMPTY");
                return;

            }

            UserModel types = documentSnapshot.toObject(UserModel.class);

            age = types.getAge();
            email = types.getEmail();
            gender = types.getGender();
            id = types.getId();
            imageURL = types.getProfileImageUrl();
            password = types.getPassword();
            phone = types.getPhone();
            username = types.getName();

            profileUsername.setText(username);
            profileEmail.setText(email);
            profileGender.setText(gender);
            profilePhone.setText(phone);
            Glide.with(getApplicationContext())
                    .load(imageURL)
                    .placeholder(R.drawable.picture_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.defavatar)
                    .into(profilePicImageView);

        }).addOnFailureListener(e -> {
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
        });


    }














}