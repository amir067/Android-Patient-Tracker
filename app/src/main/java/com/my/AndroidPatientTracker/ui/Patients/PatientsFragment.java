package com.my.AndroidPatientTracker.ui.Patients;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class PatientsFragment extends Fragment implements RecyclerviewOnClickListener {

    private static final String TAG = "PatientsFragment";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavController navController;
    protected ProgressBar progressbar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Patients
    private RecyclerView patientsRV;
    protected List<UserModel> patientsList = new ArrayList<>();
    protected PatientsListAdapterV2 patientsListAdapter ;

    //RoomList for add patients
    protected ArrayList<String> roomsList = new ArrayList<String>();
    protected ArrayList<RoomObject> roomsObjectsList = new ArrayList<>();

    private FloatingActionButton addPatientFAB;

    // Serach bar
    private MaterialSearchBar searchBar;
    private SearchBarAdapterPatient searchBarAdapterPatient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_patients, container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());
        getActivity().setTheme(R.style.BasicAppTheme);
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
        addPatientFAB= view.findViewById(R.id.fab_add_patient);
        searchBar = (MaterialSearchBar) view.findViewById(R.id.sb_patients);
        searchBar.setHint("Search Patients");
        searchBar.setPlaceHolder("Search Patients");
        roomsList.add("Select Room");
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

         //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomsList);

        patientsListAdapter = new PatientsListAdapterV2(requireContext(),patientsList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: patient clicked" );
                Bundle placeId = new Bundle();
               // placeId.putInt("patientId",Integer.parseInt(roomsList.get(position).getId()));
                //placeId.putString("patientName",(roomsList.get(position).getName()));
                // navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);
            }

            @Override
            public void onItemLongClick(int position, View v) {

                Log.e("On item longclick", String.valueOf(position));
                //Snackbar.make(v, "On item longclick  "+position, Snackbar.LENGTH_LONG).show();


                final CharSequence[] items = {"Delete", "Cancel"};
                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(requireContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);//,R.style.MyRounded_MaterialComponents_MaterialAlertDialog
                builder.setTitle("Select The Action");
                builder.setCancelable(false);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item==0){
                            Log.e("delete clicked", "ok");
                            doDeletePatient(patientsListAdapter.getItem(position).getId());
                        }else {
                            Log.e("cancel clicked", "ok");
                            dialog.cancel();
                        }
                    }
                });
                builder.show();

            }
        });


        addPatientFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(requireContext(),R.style.AlertDialogCustom);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_add_patient);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setGravity(Gravity.CENTER);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                // WindowManager.LayoutParams lp = window.getAttributes();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



                final Button addRoomBtn = (Button) dialog.findViewById(R.id.add_patient_btn);
                final Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
                final EditText patientName = (EditText) dialog.findViewById(R.id.et_patient_name);

                final Spinner ganderSP = (Spinner) dialog.findViewById(R.id.sp_gender);
                final Spinner selectRoomSP = (Spinner) dialog.findViewById(R.id.sp_room);
                final Spinner selectAgeSP = (Spinner) dialog.findViewById(R.id.sp_age);

                String[] items = new String[] {"Select Gender", "Male", "Female"};
                ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items);
                adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ganderSP.setAdapter(adapterGender);

                String[] ageItems = new String[100];
                ageItems[0] = "Select Age";
                for (int a = 1; a < ageItems.length; a++) {
                    ageItems[a] = ""+a;
                }
                ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, ageItems);
                adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectAgeSP.setAdapter(adapterAge);

                //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, roomsList);
                //SimpleAdapter simpleAdapter = new SimpleAdapter(requireContext(),android.R.layout.simple_list_item_1,roomsList);

                ArrayAdapter arrayAdapterRooms = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomsList);
                arrayAdapterRooms.setDropDownViewResource(android.R.layout.simple_spinner_item);
                selectRoomSP.setAdapter(arrayAdapterRooms);

                selectRoomSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                addRoomBtn.setOnClickListener(v12 -> {

                    String pName=patientName.getText().toString();
                    String pGender=ganderSP.getSelectedItem().toString();
                    String pAge=selectAgeSP.getSelectedItem().toString();
                    String pRoomName=selectRoomSP.getSelectedItem().toString();
                    int tem= (selectRoomSP.getSelectedItemPosition()-1);
                    String pRoomNameIndexID= roomsObjectsList.get( tem ).getId();

                    if(TextUtils.isEmpty(pName)){
                        patientName.setError("Enter Name");
                        patientName.requestFocus();
                        return;
                    }

                    if(pGender.equals("Select Gender")){
                        Toasty.info(requireContext(),"Select Gender",Toasty.LENGTH_SHORT).show();
                        ganderSP.performClick();
                        return;
                    }
                    if(pRoomName.equals("Select Gender")){
                        Toasty.info(requireContext(),"Select Room",Toasty.LENGTH_SHORT).show();
                        selectRoomSP.performClick();
                        return;
                    }

                    if(pAge.equals("Select Age")){
                        Toasty.info(requireContext(),"Select Age",Toasty.LENGTH_SHORT).show();
                        selectAgeSP.performClick();
                        return;
                    }

                    Date c = Calendar.getInstance().getTime();

                    System.out.println("Current time => " + c);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    Log.e(TAG, "onClick: "+formattedDate );

                    dialog.dismiss();
                    //>>
                    doAddPatient(pName, pGender,pAge,pRoomName,pRoomNameIndexID);//Room list array for validation
                    //<<
                    Log.e(TAG, "onClick: btn add clicked" );

                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        dialog.dismiss();
                    }
                });


                Bitmap map=takeScreenShot(requireActivity());
                Bitmap fast=fastblur(map, 20);
                final Drawable draw=new BitmapDrawable(getResources(),fast);

                dialog.show();
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(draw);

            }
        });


        patientsRV.setHasFixedSize(true);
        patientsRV.setLayoutManager(layoutManager);
        patientsRV.setAdapter(patientsListAdapter);


        loadPatientsList();
        loadRoomsList();
    }

    private void doAddPatient(String pName,String pGender, String pAge,String pRoom,String pRoomNameIndexID) {
        //doAddPatient(pName, pGender,pAge,pRoomName);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        int randomId = new Random().nextInt(400000) + 1000; // [0, 60] + 20 => [20, 80]
        Log.e(TAG, "do Adding Patient.. " );

        HashMap<Object, Object> PatientMap = new HashMap<>();
        PatientMap.put("id",String.valueOf(randomId));
        PatientMap.put("name",pName);
        PatientMap.put("age",Double.valueOf(pAge));
        PatientMap.put("gender",pGender);
        PatientMap.put("RoomID",pRoomNameIndexID);
        PatientMap.put("RoomName",pRoom);
        PatientMap.put("added_at",new Date().getTime());
        PatientMap.put("modified_at",new Date().getTime());

        mDatabase.child("patients").child(String.valueOf(randomId)).setValue(PatientMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
           Toasty.success(requireContext(),"Patient Added Successfully",Toasty.LENGTH_SHORT).show();

           doAddmitPatientToRoom(randomId,pRoomNameIndexID,pName,pAge,pGender);
            }
        });

        //Toasty.success(requireContext(), "Added successfully ", Toast.LENGTH_SHORT).show();
        patientsListAdapter.notifyDataSetChanged();

    }

    private void doAddmitPatientToRoom(int patientId,String pRoomNameIndexID,String pName,String pAge,String Gender) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        Log.e(TAG, "do Addmitting Patient to room.. " );

        HashMap<Object, Object> PatientMap = new HashMap<>();
        PatientMap.put("id",String.valueOf(patientId));
        PatientMap.put("name",pName);
        PatientMap.put("age",Double.valueOf(pAge));
        PatientMap.put("gender",Gender);
       // PatientMap.put("RoomID",pRoomNameIndexID);
        PatientMap.put("added_at",new Date().getTime());
        PatientMap.put("modified_at",new Date().getTime());

        mDatabase.child("rooms").child(pRoomNameIndexID).child("admitPatients").child(String.valueOf(patientId)).setValue(PatientMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(requireContext(),"Patient Added Successfully",Toasty.LENGTH_SHORT).show();
                Log.e(TAG, "onSuccess: patient admit to room success" );
                updateRoomStatus(pRoomNameIndexID);
            }
        });

    }

    private void doDeletePatient(String id) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Log.e(TAG, "do Delete patient: address: patient."+(id) );

        mDatabase.child("patients").child(id).removeValue();
        Toasty.success(requireContext(), "Deleted successfully ", Toast.LENGTH_SHORT).show();
        //roomsListAdapter.doNotifyDataChanged();

    }

    private void updateRoomStatus( String pRoomNameIndexID){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(pRoomNameIndexID);//space_filled
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value;

                if(dataSnapshot.exists()){
                    Log.e(TAG, "onDataChange: data exits" );

                    value =(long) dataSnapshot.child("space_filled").getValue();
                   // Log.e(TAG, "onDataChange: wate value on server : "+value );
                    value = value + 1;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("space_filled",value);
                    hashMap.put("modified_at", new Date().getTime());
                    dataSnapshot.getRef().updateChildren(hashMap);
                    //Toasty.success(HomeActivity.this,"Update Success",Toasty.LENGTH_SHORT).show();
                }
                else{
                    Log.e(TAG, "onDataChange: data not exits" );
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("space_filled",1);
                    hashMap.put("modified_at",new Date().getTime());
                    dataSnapshot.getRef().setValue(hashMap);
                    //Toasty.success(HomeActivity.this,"Add Success",Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

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
                        RoomObject temRoomObject = data.getValue(RoomObject.class);
                        roomsList.add(temRoomObject.getName());
                        roomsObjectsList.add(temRoomObject);

                    }


                    Log.e(TAG, "load data success: Rooms List " );


                } else {
                    // Don't exist! Do something.
                    //Toast.makeText(requireContext(), "Reference Code Invalid", Toast.LENGTH_SHORT).show();
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

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {



                    Log.e(TAG, "onDataChange: "+snapshot.getValue().toString() );

                    for (DataSnapshot data :  snapshot.getChildren())
                    {

                        if(data.child("userType").exists()){

                            if(data.child("userType").getValue().equals("patient")){
                                UserModel temObject = data.getValue(UserModel.class);
                                patientsList.add(temObject);
                            }

                        }

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

    @Override
    public void onItemLongClick(int position, View v) {

    }


    private static Bitmap takeScreenShot(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


}