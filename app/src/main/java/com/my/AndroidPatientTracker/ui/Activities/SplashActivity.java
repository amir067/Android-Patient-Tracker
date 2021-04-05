package com.my.AndroidPatientTracker.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.ui.Authentication.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final TextView txt1=findViewById(R.id.textView10);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.7f);
        alphaAnimation.setDuration(2000);
        txt1.startAnimation(alphaAnimation);

        //hold screen for 3 to 5 seconds and move to main screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000); // for 3 second
    }


}
