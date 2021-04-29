package com.my.AndroidPatientTracker.ui.Patients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.adapters.SearchBarAdapterPatient;
import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class PatientsFragment extends Fragment implements RecyclerviewOnClickListener {

    private static final String TAG = "PatientsFragment";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavController navController;
    protected ProgressBar progressbar;


    //Patients
    private RecyclerView patientsRV;
    protected List<PatientObject> patientsList = new ArrayList<>();
    protected PatientsListAdapterV2 patientsListAdapter ;


    // Serach bar
    private MaterialSearchBar searchBar;
    private SearchBarAdapterPatient searchBarAdapterPatient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_patients, container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());

        searchBarAdapterPatient = new SearchBarAdapterPatient(inflater,this);

        // Inflate the layout for this fragment
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        MyUtils.hideKeyboard(requireActivity());
        progressbar= view.findViewById(R.id.pb_patients_frag);
        patientsRV= view.findViewById(R.id.rv_patients);
        searchBar = (MaterialSearchBar) view.findViewById(R.id.sb_patients);
        searchBar.setHint("Search Patients");
        searchBar.setPlaceHolder("Search Patients");

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        patientsListAdapter = new PatientsListAdapterV2(requireContext(),patientsList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: patient clicked" );
                Bundle placeId = new Bundle();
               // placeId.putInt("patientId",Integer.parseInt(roomsList.get(position).getId()));
                //placeId.putString("patientName",(roomsList.get(position).getName()));
                // navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);
            }
        });



        patientsRV.setHasFixedSize(true);
        patientsRV.setLayoutManager(layoutManager);
        patientsRV.setAdapter(patientsListAdapter);




        loadPatientsList();
    }


    private void loadPatientsList() {
        // loading_dialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("patients");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    List<RoomObject> temp = new ArrayList<>();

                    Log.e(TAG, "onDataChange: "+snapshot.getValue().toString() );

                    for (DataSnapshot data :  snapshot.getChildren())
                    {
                        PatientObject temObject = data.getValue(PatientObject.class);
                        patientsList.add(temObject);
                    }

                    patientsListAdapter.setPlaceObjects(patientsList);
                    searchBarAdapterPatient.setSuggestions(patientsList);
                    searchBar.setCustomSuggestionAdapter(searchBarAdapterPatient);
                    //placesSuggestionsAdapter.setSuggestions(placeList);
                    //searchBar.setCustomSuggestionAdapter(placesSuggestionsAdapter);

                    progressbar.setVisibility(View.GONE);
                    //  Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "patients value : "+snapshot.getValue());
                    Log.e(TAG, "load data success: patients List " );


                } else {
                    // Don't exist! Do something.
                    Toast.makeText(requireContext(), "Reference Code Invalid", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Refer Code value : "+snapshot.getValue());
                    //code_not_accepted_do_task(code,name ,email , city,phnum ,password,gender);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: "+error.getDetails() );
            }

        });

    }

    @Override
    public void recyclerviewClick(int position) {


    }


}