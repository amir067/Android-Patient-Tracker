package com.my.AndroidPatientTracker.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.FindDoctors.RecycleDoctorsActivity;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;
import com.my.AndroidPatientTracker.utils.Tools;

public class HomeActivity extends AppCompatActivity {
    int backpress = 0;
    PreferenceHelperDemo preferenceHelperDemo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "HomeActivity";
    CardView card1,card2,card3,card4,card5,card6,card7,card8,botcrd,facerecognizationcard;

   TextView user_name;
   String usr_name,imageURL;

   private CardView card1CV;
   private ImageView closeCard1IV;
   ImageView user_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferenceHelperDemo = new PreferenceHelperDemo(this);
        user_dp=findViewById(R.id.iv_user_dp);
        user_name = findViewById(R.id.tv_logged_user_name);
        card1CV = findViewById(R.id.cv_card1);
        closeCard1IV = findViewById(R.id.iv_close_card1);

        closeCard1IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card1CV.setVisibility(View.GONE);
            }
        });

        Window window = getWindow();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(this);

        AlphaAnimation dp_alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        dp_alphaAnimation.setDuration(2500);
        user_dp.startAnimation(dp_alphaAnimation);

        //  gifimage2.setOnClickListener(view -> startActivity(new Intent(this, InformationActivity.class)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        profile();
    }


    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            this.finish();
        }
    }

    private void intent() {


        //startActivity(new Intent(this,StatementActivity.class));
    }

    private void profile() {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Log.d(TAG, "onSuccess: LIST EMPTY");
                return;
            }
            UserModel types = documentSnapshot.toObject(UserModel.class);
            String gender = types.getGender();
            usr_name= types.getUsername();
            user_name.setText("Hi, "+usr_name);
            imageURL = types.getImageURL();

            Glide.with(getApplicationContext())
                    .load(imageURL)
                    .placeholder(R.drawable.picture_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.ic_avatar)
                    .into(user_dp);
            preferenceHelperDemo.setKey("gender", gender);

        }).addOnFailureListener(e -> {
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
        });
    }


    public void btn_all_doctor_click(View v){
        this.startActivity(new Intent(v.getContext(), RecycleDoctorsActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }



}
