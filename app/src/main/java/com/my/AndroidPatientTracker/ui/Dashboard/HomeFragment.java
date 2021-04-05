package com.my.AndroidPatientTracker.ui.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;
import com.my.AndroidPatientTracker.utils.Tools;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private int backPressed = 0;
    private PreferenceHelperDemo preferenceHelperDemo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavController navController;

    // user
    private TextView user_name;
    private String usr_name,imageURL;

    //UI
    private Button startTestBTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Window window = getWindow();
        //Tools.setSystemBarColor(this,R.color.grey_40);
        //getSupportActionBar().hide();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceHelperDemo = new PreferenceHelperDemo(requireContext());
        startTestBTN = view.findViewById(R.id.btn_start_test);
        navController = Navigation.findNavController(view);


        startTestBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_fragment_to_startTestFragment);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(getActivity());
    }
}