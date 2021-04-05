package com.my.AndroidPatientTracker.ui.StartTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.my.AndroidPatientTracker.R;


public class FaceRecFragment extends Fragment {

    String TAG = "FaceRecFragment";
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_face_rec, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


//        ivProfile.setVisibility(View.INVISIBLE);
//        scanimage.setVisibility(View.INVISIBLE);
//        //scanimage.setFreezesAnimation(false);
//
//       // btntxt.findViewById(R.id.textView6);
//       // ivProfile.setImageBitmap(file);
//        //btntxt.setText("completed"+);
//        ivProfile.setVisibility(View.VISIBLE);
//        scanimage.setVisibility(View.VISIBLE);
//        btnscan.setVisibility(View.INVISIBLE);
//        waitfoter.setVisibility(View.VISIBLE);

        ///////////////////////////
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity


               // startActivity(new Intent(ProfileActivity.this, ResultActivity.class));
                //finish();

            }
        }, 5000); // for 3 second


        //ivProfile.setImageBitmap(updatedBitmap);
        //Toast.makeText(ProfileActivity.this, "We detected a face", Toast.LENGTH_SHORT).show();



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        try {
            super.onActivityResult(requestCode, resultCode, data);


        } catch (Exception ex) {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



}
