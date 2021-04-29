package com.my.AndroidPatientTracker.ui.Rooms;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.my.AndroidPatientTracker.adapters.SearchBarAdapterRoom;
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

public class RoomsFragment extends Fragment implements RecyclerviewOnClickListener {

    private static final String TAG = "PatientsFragment";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavController navController;
    protected ProgressBar progressbar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //Rooms
    private RecyclerView roomsRV;
    protected List<RoomObject> roomsList = new ArrayList<>();
    protected RoomsListAdapter roomsListAdapter ;
    private RoomsSuggestionsAdapter roomsSuggestionsAdapter;

    private FloatingActionButton addRoomFAB;


    // Serach bar
    private MaterialSearchBar searchBar;
    private SearchBarAdapterRoom searchBarAdapterRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());
        getActivity().setTheme(R.style.BasicAppTheme);

        searchBarAdapterRoom = new SearchBarAdapterRoom(inflater,this);

        // Inflate the layout for this fragment
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        MyUtils.hideKeyboard(requireActivity());
        progressbar= view.findViewById(R.id.pb_rooms_frag);
        roomsRV= view.findViewById(R.id.rv_rooms);
        addRoomFAB= view.findViewById(R.id.fab_add_room);
        searchBar = (MaterialSearchBar) view.findViewById(R.id.sb_rooms);
        searchBar.setHint("Search Rooms");
        searchBar.setPlaceHolder("Search Rooms");

        LinearLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3,LinearLayoutManager.VERTICAL, false);
       // layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);

        GridLayoutManager layoutManagerGrid=new GridLayoutManager(requireContext(),2);

        roomsRV.setHasFixedSize(true);
        roomsRV.setLayoutManager(layoutManagerGrid);
        roomsRV.setAdapter(roomsListAdapter);


        roomsListAdapter = new RoomsListAdapter(requireContext(),roomsList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: room clicked" );
                Bundle placeId = new Bundle();
                placeId.putString("roomId",(roomsList.get(position).getId()));
                placeId.putString("roomName",(roomsList.get(position).getName()));
                // navController.navigate(R.id.action_navigation_home_fragment_to_placeDetailFragment,placeId);
            }

            @Override
            public void onItemLongClick(int position, View v) {


                //position = position+1;//As we are adding header
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
                                doDeleteRoom(roomsListAdapter.getPlace(position).getId());
                            }else {
                                Log.e("cancel clicked", "ok");
                                dialog.cancel();
                            }
                        }
                    });
                    builder.show();

               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setTitle("Select The Action");
                alertDialog.setCancelable(false);
                alertDialog.set(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item==0){
                            Log.e("delete clicked", "ok");
                            doDeleteRoom(roomsListAdapter.getPlace(position).getId());
                        }else {
                            Log.e("cancel clicked", "ok");
                            dialog.cancel();
                        }
                    }
                });
                builder.show();*/


            }
        });






        addRoomFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(requireContext(),R.style.AlertDialogCustom);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_add_room);
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



                final Button addRoomBtn = (Button) dialog.findViewById(R.id.add_room_btn);
                final Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
                final EditText roomNameET = (EditText) dialog.findViewById(R.id.et_room_name);
                final EditText roomBedsET = (EditText) dialog.findViewById(R.id.et_room_beds);

                addRoomBtn.setOnClickListener(v12 -> {

                    String rName=roomNameET.getText().toString();
                    String rNoBeds=roomBedsET.getText().toString();

                    if(TextUtils.isEmpty(rName)){
                        roomNameET.setError("Enter Name");
                        roomNameET.requestFocus();
                        return;
                    }
                    if(TextUtils.isEmpty(rNoBeds)){
                        roomNameET.setError("Enter Name");
                        roomNameET.requestFocus();
                        return;
                    }
                    Date c = Calendar.getInstance().getTime();

                    System.out.println("Current time => " + c);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    Log.e(TAG, "onClick: "+formattedDate );

                    dialog.dismiss();
                    doAddRoom(rName,rNoBeds );
                    Log.e(TAG, "onClick: btn add clicked" );

                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                         dialog.dismiss();
                    }
                });


                Bitmap map=takeScreenShot(requireActivity());
                Bitmap fast=fastblur(map, 30);
                final Drawable draw=new BitmapDrawable(getResources(),fast);

                dialog.show();
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(draw);
            }
        });


        loadRoomsList();
    }

    private void doDeleteRoom(String id) {



            mAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String userid = firebaseUser.getUid();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            Log.e(TAG, "do Delete Room: address: room."+(id) );

            mDatabase.child("rooms").child(id).removeValue();
            Toasty.success(requireContext(), "Deleted successfully ", Toast.LENGTH_SHORT).show();
            roomsListAdapter.doNotifyDataChanged();




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
                        roomsList.add(temRoomObject);
                    }

                    roomsListAdapter.setPlaceObjects(roomsList);
                    roomsRV.setAdapter(roomsListAdapter);

                    searchBarAdapterRoom.setSuggestions(roomsList);
                    searchBar.setCustomSuggestionAdapter(searchBarAdapterRoom);
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


    private void doAddRoom(String roomName,String totalBeds) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        DatabaseReference reference;

        int randomRoomId = new Random().nextInt(400000) + 1000; // [0, 60] + 20 => [20, 80]

        reference = FirebaseDatabase.getInstance().getReference("rooms");

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("id",String.valueOf(randomRoomId));
        hashMap.put("name",roomName);
        hashMap.put("status","free");
        hashMap.put("space_total",Double.valueOf(totalBeds));
        hashMap.put("space_filled",Double.valueOf(0));
        hashMap.put("added_at",new Date().getTime());
        hashMap.put("modified_at",new Date().getTime());

        reference.child(String.valueOf(randomRoomId)).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(requireContext(),"Room Added Successfully",Toasty.LENGTH_SHORT).show();
                roomsListAdapter.doNotifyDataChanged();

            }
        });

    }

}