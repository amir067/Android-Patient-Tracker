package com.my.AndroidPatientTracker.ui.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.adapters.SearchBarAdapterPatient;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Activities.AdminActivity;
import com.my.AndroidPatientTracker.ui.Patients.PatientDetailActivity;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment implements RecyclerviewOnClickListener {

    public enum USER_LIST_TYPE{
        DOCTOR,
        PATIENT
    }

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
    protected List<UserModel> patientsList = new ArrayList<>();
    protected PatientsListAdapter patientsListAdapter ;
    //private RoomsSuggestionsAdapter patientsSuggestionsAdapter;

    //UI
    private Button barButton1,barButton2,barButton3,barButton4,barButton5;
    private BottomNavigationView bottomNavigationView;
    private Button adminPanel;
    private Button chatsButton;

    private FrameLayout mainDataFrameLayout;
    private ImageView noInternetIV;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Serach bar
    private MaterialSearchBar searchBar;
    private SearchBarAdapterPatient searchBarAdapterPatient;

    // DB
    private DatabaseReference reference;
    //Firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // user
    private TextView user_name;
    private TextView user_address;
    private String usr_name,imageURL;
    private ImageView userImageIV;
    private CardView userDpCV;
    private String userProfileUrl;
    private LinearLayout userProfileInfoBoxLL;

    RecyclerviewOnClickListener recyclerviewOnClickListener ;

    private UserModel userModel  = new UserModel();

    @BindView(R.id.lable_patients)
    TextView lable_patients;

    USER_LIST_TYPE userListType = USER_LIST_TYPE.DOCTOR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());
        ButterKnife.bind(this,root);
        bottomNavigationView = ((HomeActivity) getActivity()).findViewById(R.id.bottom_nav_view);
        //bottomNavigationView.setVisibility(View.VISIBLE);

        searchBarAdapterPatient = new SearchBarAdapterPatient(inflater,this);
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
        user_address = view.findViewById(R.id.tv_logged_user_address);
        userImageIV = view.findViewById(R.id.iv_user_dp);
        userDpCV = view.findViewById(R.id.cv_user_dp);
        userProfileInfoBoxLL = view.findViewById(R.id.ll_user_info_box);
        mainDataFrameLayout = view.findViewById(R.id.main_data_farme_layout);
        noInternetIV = view.findViewById(R.id.iv_no_connection);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        adminPanel = view.findViewById(R.id.adminPanel);

        searchBar = (MaterialSearchBar) view.findViewById(R.id.sb_places);

        //searchBar.setSpeechMode(true);

        roomsRV= view.findViewById(R.id.rv_rooms);
        patientsRV= view.findViewById(R.id.rv_patients);
        progressbar= view.findViewById(R.id.pb_home_frag);

        chatsButton = view.findViewById(R.id.btn_all_chats);
        barButton1 = view.findViewById(R.id.btn_bar_btn1);
        barButton2 = view.findViewById(R.id.btn_bar_btn2);
        barButton3 = view.findViewById(R.id.btn_bar_btn3);
        barButton4 = view.findViewById(R.id.btn_bar_btn4);

        barButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_fragment_to_patientsFragment);
            }
        });

        barButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_fragment_to_roomsFragment);
            }
        });

        adminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), AdminActivity.class));
            }
        });

        chatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ConversationActivity.class);
                startActivity(intent);
            }
        });



        //profile();

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
                searchBarAdapterPatient.getFilter().filter(searchBar.getText());

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

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        patientsListAdapter = new PatientsListAdapter(requireContext(),patientsList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: patient clicked" );
                Bundle placeId = new Bundle();
                // navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);


                Intent intent = new Intent(requireContext(), PatientDetailActivity.class);
                intent.putExtra("id",(patientsList.get(position).getId()));
                intent.putExtra("name", patientsList.get(position).getName() );
               // intent.putExtra("address",machinicList.get(position).getAddress() );
               // intent.putExtra("phone",patientsList.get(position).get() );
                intent.putExtra("desc",patientsList.get(position).getUserBio() );
                intent.putExtra("img",patientsList.get(position).getProfileImageUrl() );
               // intent.putExtra("lat",patientsList.get(position).getLatMAP() );
                //intent.putExtra("long",patientsList.get(position).getLongMAP() );

                intent.putExtra("item",( patientsList.get(position)) );
                startActivity(intent);


            }

            @Override
            public void onItemLongClick(int position, View v) {

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

        getUserData();

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

    private void loadList(USER_LIST_TYPE user_list_type) {
        // loading_dialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    List<RoomObject> temp = new ArrayList<>();

                    Log.e(TAG, "onDataChange: "+snapshot.getValue().toString() );

                    /*for (DataSnapshot data :  snapshot.getChildren())
                    {
                        UserModel temObject = data.getValue(UserModel.class);
                        patientsList.add(temObject);
                    }*/


                    for (DataSnapshot data :  snapshot.getChildren())
                    {

                        if(data.child("userType").exists()){

                            if(user_list_type == USER_LIST_TYPE.DOCTOR){
                                if(data.child("userType").getValue().equals("doctor")){
                                    UserModel temObject = data.getValue(UserModel.class);
                                    patientsList.add(temObject);
                                }
                            }
                            else if(user_list_type == USER_LIST_TYPE.PATIENT){

                                if(data.child("userType").getValue().equals("patient")){
                                    UserModel temObject = data.getValue(UserModel.class);
                                    patientsList.add(temObject);
                                }
                            }else{
                                Log.e(TAG, "during userers loop user type not found skiping this item " );
                            }

                        }

                    }

                    patientsListAdapter.setPlaceObjects(patientsList);
                    searchBarAdapterPatient.setSuggestions(patientsList);
                    searchBar.setCustomSuggestionAdapter(searchBarAdapterPatient);

                    updateSerachBarUI(user_list_type);


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

    private void updateSerachBarUI(USER_LIST_TYPE user_list_type) {
        Log.e(TAG, "updateSerachBarUI: called" );

        if(user_list_type== USER_LIST_TYPE.DOCTOR){
            searchBar.setHint("Search Doctors");
            searchBar.setPlaceHolder("Search Doctors");
        }else{
            searchBar.setHint("Search Patients");
            searchBar.setPlaceHolder("Search Patients");
        }
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

        Log.e(TAG, "recyclerviewClick: position: "+ searchBarAdapterPatient.getItemId(position) );

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
    public void onItemLongClick(int position, View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(getActivity());
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
            usr_name= types.getName();
            user_name.setText("Hi, "+usr_name);
            imageURL = types.getProfileImageUrl();

            Glide.with(requireContext())
                    .load(imageURL)
                    .placeholder(R.drawable.picture_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.ic_doctor_mask)
                    .into(userImageIV);

            userDpCV.setAlpha(0f);
            userDpCV.setVisibility(View.VISIBLE);
            AlphaAnimation dp_alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            dp_alphaAnimation.setDuration(2500);
            userDpCV.startAnimation(dp_alphaAnimation);

            preferenceHelperDemo.setKey("gender", gender);

        }).addOnFailureListener(e -> {
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
        });
    }


    private void getUserData() {
        Log.e(TAG, "getUserData: gettingg. home fragment.." );

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser ==null){
            Log.e(TAG, "getUserData: firebase user null, try sign in again" );

        }else{

            String userid = firebaseUser.getUid();
            Log.e(TAG, "userid: "+userid);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
            rootRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.e(TAG, "snapshot: exists" );

                        try {

                            userModel = snapshot.getValue(UserModel.class);

                            if (!userModel.getName().isEmpty()) {

                                user_name.setText("Welcome, " + userModel.getName());
                                Log.i(TAG, "parsing-getName: " + userModel.getName());
                            }
                            if (userModel.getAddress() != null) {
                                user_address.setText(userModel.getAddress());
                                Log.i(TAG, "parsing-getAddress: " + userModel.getAddress());
                            }else{
                                user_address.setVisibility(View.GONE);
                            }

                            updateUIaccordingToUser(userModel);

                            Log.e(TAG, "parsing-isAdmin: " + userModel.isAdmin());

                            if(snapshot.child("isAdmin").exists() ){

                                Log.e(TAG, "isAdmin: if isAdmin true ok" );

                                if((boolean)snapshot.child("isAdmin").getValue()){
                                    adminPanel.setVisibility(View.VISIBLE);
                                }

                            }else{
                                Log.e(TAG, "isAdmin: else isAdmin true ok" );
                                adminPanel.setVisibility(View.GONE);
                            }

                            if (userModel.getProfileUrl() != null) {

                                //userDpCV.setAlpha(0f);
                                userDpCV.setVisibility(View.VISIBLE);
                                AlphaAnimation dp_alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                dp_alphaAnimation.setDuration(2500);
                                userDpCV.startAnimation(dp_alphaAnimation);


                                Log.i(TAG, "parsing-getProfileUrl: ");
                                userProfileUrl = userModel.getProfileUrl();
                                Log.e(TAG, "onDataChange: userprofile url: " + userProfileUrl);
                                Glide.with(requireContext())
                                        .load(userProfileUrl)////getResources().getDrawable(R.drawable.ic_std_avatar_male)
                                        .fitCenter()
                                        .placeholder(R.drawable.ic_wait_white)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                        .error(R.drawable.ic_std_avatar_male)
                                        .into(userImageIV);
                                // Picasso.get().load(userProfileUrl).into(userProfileIV);



                            } else {

                                if (userModel.getGender() != null) {
                                    if (userModel.getGender().equals("Male")) {
                                        userImageIV.setImageResource(R.drawable.ic_std_avatar_male);
                                    } else {
                                        userImageIV.setImageResource(R.drawable.ic_std_avatar_female);
                                    }
                                } else {
                                    userImageIV.setImageResource(R.drawable.ic_std_avatar_male);
                                }
                            }
                        }
                        catch (Exception e){
                            Log.e(TAG, "Exception Error: "+e.getLocalizedMessage() );

                        }
                        // Exist! Do something.
                        // Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "snapshot value : "+snapshot.getValue());

                        userProfileInfoBoxLL.setAlpha(0);
                        userProfileInfoBoxLL.setVisibility(View.VISIBLE);
                        userProfileInfoBoxLL.animate().alpha(1).setDuration(1000).start();
                        userProfileInfoBoxLL.animate().translationYBy(30).setDuration(1500).start();
                    } else {

                        userProfileInfoBoxLL.setVisibility(View.GONE);
                        // Don't exist! Do something.
                        Toast.makeText(requireContext(), "Profile Error ", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.getDetails() );
                }

            });
        }


    }

    private void updateUIaccordingToUser(UserModel userModel) {
        Log.e(TAG, "updateUIaccordingToUser: " );
        if(userModel.getUserType() !=null){

            Log.e(TAG, "updateUIaccordingToUser: user type is not null" );

            if(userModel.getUserType().equals("patient")){
                Log.e(TAG, "user is:  patient" );
                lable_patients.setText("Doctors");
                loadList(USER_LIST_TYPE.DOCTOR);
            }else{
                Log.e(TAG, "user is:  doctor" );
                loadList(USER_LIST_TYPE.PATIENT);
                Log.e(TAG, " leave old ui " );
            }

        }else{
            Log.e(TAG, "updateUIaccordingToUser: user type is null" );
        }

    }


}