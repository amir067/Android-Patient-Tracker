package com.my.AndroidPatientTracker.ui.Dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.ui.Patients.PatientObject;
import com.my.AndroidPatientTracker.ui.Patients.PatientsListAdapter;
import com.my.AndroidPatientTracker.ui.Rooms.RoomsSuggestionsAdapter;
import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;
import com.my.AndroidPatientTracker.ui.Rooms.RoomsListAdapter;
import com.my.AndroidPatientTracker.utils.AppStatus;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;
import com.my.AndroidPatientTracker.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment implements RecyclerviewOnClickListener {

    private static final String TAG = "HomeFragment";
    private int backPressed = 0;
    private PreferenceHelperDemo preferenceHelperDemo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavController navController;
    protected ProgressBar progressbar;

    //Rooms
    private RecyclerView roomsRV;
    protected List<RoomObject> roomsList = new ArrayList<>();
    protected RoomsListAdapter roomsListAdapter ;
    private RoomsSuggestionsAdapter roomsSuggestionsAdapter;


    //Patients
    private RecyclerView patientsRV;
    protected List<PatientObject> patientsList = new ArrayList<>();
    protected PatientsListAdapter patientsListAdapter ;
    //private RoomsSuggestionsAdapter patientsSuggestionsAdapter;

    //UI
    private Button barButton1,barButton2,barButton3,barButton4,barButton5;
    private BottomNavigationView bottomNavigationView;

    private FrameLayout mainDataFrameLayout;
    private ImageView noInternetIV;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Serach bar
    private MaterialSearchBar searchBar;

    // DB
    private DatabaseReference reference;

    // user
    private TextView user_name;
    private String usr_name,imageURL;

    RecyclerviewOnClickListener recyclerviewOnClickListener ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());
        bottomNavigationView = ((HomeActivity) getActivity()).findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);

        //placesSuggestionsAdapter = new RoomsSuggestionsAdapter(inflater,this);
        // Inflate the layout for this fragment
        return root;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyUtils.hideKeyboard(requireActivity());
        navController = Navigation.findNavController(view);
        preferenceHelperDemo = new PreferenceHelperDemo(requireContext());
        navController = Navigation.findNavController(view);

        user_name = view.findViewById(R.id.tv_logged_user_name);
        mainDataFrameLayout = view.findViewById(R.id.main_data_farme_layout);
        noInternetIV = view.findViewById(R.id.iv_no_connection);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        searchBar = (MaterialSearchBar) view.findViewById(R.id.sb_places);
        searchBar.setHint("Search Places");
        //searchBar.setSpeechMode(true);

        roomsRV= view.findViewById(R.id.rv_rooms);
        patientsRV= view.findViewById(R.id.rv_patients);
        progressbar= view.findViewById(R.id.pb_home_frag);



        Tools.clearSystemBarLight(requireActivity());
        Tools.setSystemBarTransparent(requireActivity());


        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());
                // send the entered text to our filter and let it manage everything
        //        placesSuggestionsAdapter.getFilter().filter(searchBar.getText());

            }
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " afterTextChanged  " + searchBar.getText());
            }
        });

        searchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                Log.i(TAG, "OnItemClickListener: working");
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        if (!AppStatus.getInstance(requireContext()).isOnline()) {
            Toast.makeText(requireContext(), "Internet not available!", Toast.LENGTH_SHORT).show();
            mainDataFrameLayout.setVisibility(View.GONE);
            noInternetIV.setVisibility(View.VISIBLE);

        }
        else {
            //   mainDataFrameLayout.setVisibility(View.VISIBLE);
            //   noInternetIV.setVisibility(View.GONE);
            Log.e(TAG, "internetCheck: available "+ AppStatus.getInstance(requireContext()).isOnline() );
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");

                if (!AppStatus.getInstance(requireContext()).isOnline()) {
                    Toast.makeText(requireContext(), "Internet not available!", Toast.LENGTH_SHORT).show();
                    mainDataFrameLayout.setVisibility(View.GONE);
                    noInternetIV.setVisibility(View.VISIBLE);
                    Log.e(TAG, "on refresh internet : not available" );
                }
                else {
                    Toasty.success(requireContext(), "Online", Toast.LENGTH_SHORT).show();
                    mainDataFrameLayout.setVisibility(View.VISIBLE);
                    noInternetIV.setVisibility(View.GONE);
                    Log.e(TAG, "on refresh internet: available " );
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });


        roomsListAdapter = new RoomsListAdapter(requireContext(),roomsList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: room clicked" );
                Bundle placeId = new Bundle();
                placeId.putInt("roomId",Integer.parseInt(roomsList.get(position).getId()));
                placeId.putString("roomName",(roomsList.get(position).getName()));
               // navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);
            }
        });


        patientsListAdapter = new PatientsListAdapter(requireContext(),patientsList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: patient clicked" );
                Bundle placeId = new Bundle();
                placeId.putInt("patientId",Integer.parseInt(roomsList.get(position).getId()));
                placeId.putString("patientName",(roomsList.get(position).getName()));
                // navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);
            }
        });




        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        //LinearLayoutManager liniearLayoutManager = ((LinearLayoutManager)roomsRV.getLayoutManager());
        //placesRV.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true));

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        roomsRV.setHasFixedSize(true);
        roomsRV.setLayoutManager(layoutManager);
        roomsRV.setAdapter(roomsListAdapter);

        patientsRV.setHasFixedSize(true);
        patientsRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        patientsRV.setAdapter(patientsListAdapter);


        loadRoomsList();
        loadPatientsList();

        if(navController!=null){
            // navController.navigate();
            //navController.popBackStack();
        }


    }


    private void loadRoomsList() {
        // loading_dialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("rooms");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    List<RoomObject> temp = new ArrayList<>();

                    Log.e(TAG, "onDataChange: "+snapshot.getValue().toString() );

                    for (DataSnapshot data :  snapshot.getChildren())
                    {
                        RoomObject temPlaceObject = data.getValue(RoomObject.class);
                        roomsList.add(temPlaceObject);
                    }

                    roomsListAdapter.setPlaceObjects(roomsList);

                    //placesSuggestionsAdapter.setSuggestions(placeList);
                    //searchBar.setCustomSuggestionAdapter(placesSuggestionsAdapter);

                    //  Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "places value : "+snapshot.getValue());
                    Log.e(TAG, "load data success: places List " );

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mainDataFrameLayout.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.GONE);

                        }
                    },1500);


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

                    //placesSuggestionsAdapter.setSuggestions(placeList);
                    //searchBar.setCustomSuggestionAdapter(placesSuggestionsAdapter);

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

    private void navigate(int destination) {
        //Nav Controller Actions
        if(navController!=null){
            //findNavController(view).navigate(R.id.action_first_to_second);
            navController.navigate(destination);
        }
    }

    @Override
    public void recyclerviewClick(int position) {

        Log.e(TAG, "recyclerviewClick: position: "+roomsSuggestionsAdapter.getPlacesList().get(position).getName() );

        //searchBar.setPlaceHolder(townObjectList.get(position).getTownName());
        searchBar.closeSearch();

        Log.e(TAG, "recyclerviewClick: place in search bar clicked" );
        Bundle placeId = new Bundle();
     //   placeId.putInt("placeId",Integer.parseInt( placesSuggestionsAdapter.getPlacesList().get(position).getId() ));
     //   placeId.putString("placeName",( placesSuggestionsAdapter.getPlacesList().get(position).getName()  ));
     //   placeId.putString("placeType",(  placesSuggestionsAdapter.getPlacesList().get(position).getCategoryType() ));

     //   navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(getActivity());
    }


}