package com.my.AndroidPatientTracker.ui.StartTest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.utils.Constants;


public class ColorFragment extends Fragment {

    private static final String TAG = "ColorFragment";
    private int scores = 0;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_colors, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        imageView1 = view.findViewById(R.id.clr1);
        imageView2 = view.findViewById(R.id.clr2);
        imageView3 = view.findViewById(R.id.clr3);
        imageView4 = view.findViewById(R.id.clr4);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 15;
                Constants.colors_score = scores;
                intent();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 5;
                Constants.colors_score = scores;
                intent();

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 10;
                Constants.colors_score = scores;
                intent();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 15;
                Constants.colors_score = scores;
                intent();
            }
        });

    }

    private void intent() {


        //startActivity(new Intent(this, PetsActivity.class));
        //finish();
        navController.navigate(R.id.action_colorFragment_to_petsFragment);

    }
}
