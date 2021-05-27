package com.my.AndroidPatientTracker.ui.Patients;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Chat.ChatActivity;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;
import com.my.AndroidPatientTracker.utils.Tools;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class PatientDetailActivity extends AppCompatActivity implements RecyclerviewOnClickListener {

    private static final String TAG = "PlaceDetailActivity";
    private FirebaseAuth mAuth;
    protected PreferenceHelperDemo preferenceHelperDemo;

    protected ProgressBar progressbar;
    private NavController navController;
    private DatabaseReference rootRef;


    private UserModel mechinicObject =new UserModel();

    // latitude and longitude
    double latitude = 0;
    double longitude = 0;

    // The map object for reference for ease of adding points and such
    private GoogleMap mGoogleMap;
    private MapView mMapView ;

    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(latitude, longitude))
            .zoom(10)
            .bearing(0)
            .tilt(80)
            .build();
    // The camera position tells us where the view is on the map
    private CameraPosition mCameraPosition = cameraPosition;
    // Current latitude and longitude of user
    private LatLng mCurrentLatLong = new LatLng(latitude, longitude);
    // These are flags that allow us to selectively not move the camera and
    // instead wait for idle so the movement isn't jerky
    private boolean mCameraMoving = false;
    private boolean mPendingUpdate = false;

    //UI Layouts Containers
    private LinearLayout hotelLayoutBox;
    private LinearLayout eventLayoutBox;
    private CardView mapViewCard;




    //Sub Images
    //PlaceDetailImagesAdapter subImagesAdapter;
   // private RecyclerView imagesRecyclerView;
   // ArrayList<String> imageUriArrayList = new ArrayList<>();


    private FloatingActionButton addSubImagesButton;
    private  boolean isAdminUser=false;


    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStorageref;
    private DatabaseReference mDatabaseref;
    private Uri imageuri;

    private Uri filePath;


    private ImageButton backButton;
    private Button bookNow;
    private ImageView placeImage;
    private TextView placeName;
    private TextView placeDetail;
    private TextView placeCityTV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(this);
        setTheme(R.style.AlertsActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        //Tools.setSystemBarLight(LoginFragment.this.getActivity());
         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
        Tools.setSystemBarTransparent(this);
        //getSupportActionBar().hide();

        mStorageref = FirebaseStorage.getInstance().getReference("Places Photos");
        //mDatabaseref = FirebaseDatabase.getInstance().getReference("Uploaded Photos");

        // The google map...
        mMapView =  findViewById(R.id.mv_place_map);
        mMapView.onCreate(savedInstanceState);

        progressbar = findViewById(R.id.pb_places_detail_loading);
        preferenceHelperDemo = new PreferenceHelperDemo(this);

        backButton =  findViewById(R.id.iv_back_btn);
        bookNow =  findViewById(R.id.btn_book_now);

        placeName=  findViewById(R.id.tv_place_name);
        placeDetail=  findViewById(R.id.tv_place_detail);
        placeCityTV=  findViewById(R.id.tv_place_city_name);
        placeImage=  findViewById(R.id.iv_place_image_top);

        mapViewCard  =  findViewById(R.id.map_cv);



        //Received Intents
        //String filename = getIntent().getStringExtra("image");
        //assert getArguments() != null;
        if(getIntent() !=null){

            mechinicObject.setId(getIntent().getStringExtra("id"));
            mechinicObject.setName(getIntent().getStringExtra("name"));
            mechinicObject.setAddress(getIntent().getStringExtra("address"));
            mechinicObject.setPhone(getIntent().getStringExtra("phone"));
            mechinicObject.setProfileImageUrl(getIntent().getStringExtra("img"));
            mechinicObject.setUserBio(getIntent().getStringExtra("desc"));
           // mechinicObject.setLatMAP(getIntent().getDoubleExtra("lat",0));
           // mechinicObject.setLongMAP(getIntent().getDoubleExtra("long",0));

            Log.e(TAG, "onCreate: getIntent: Data receved: " );
            Log.e(TAG, "id: " +mechinicObject.getId());
            Log.e(TAG, "name: "+mechinicObject.getName() );
            Log.e(TAG, "address: "+ mechinicObject.getAddress());
            Log.e(TAG, "phone: " +mechinicObject.getPhone());
           // Log.e(TAG, "desc: " +mechinicObject.getDescription());
            Log.e(TAG, "img: " +mechinicObject.getProfileImageUrl());
           // Log.e(TAG, "lat: " +mechinicObject.getLatMAP());
          //  Log.e(TAG, "long: "+ mechinicObject.getLongMAP());


            bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(PatientDetailActivity.this, ChatActivity.class);
                    intent.putExtra("id",(mechinicObject.getId()));
                    intent.putExtra("name",(mechinicObject.getName()));
                    intent.putExtra("img",(mechinicObject.getProfileImageUrl()));
                    startActivity(intent);

                }
            });


            //mapViewCard.setVisibility(View.VISIBLE);

            if(mechinicObject.getName()!=null){
                placeName.setText(mechinicObject.getName());
            }else{
                placeName.setVisibility(View.GONE);
            }

            if(mechinicObject.getAddress() !=null){
                placeCityTV.setText(mechinicObject.getAddress());
            }else{
                placeCityTV.setVisibility(View.GONE);
            }

            if(mechinicObject.getUserBio()!=null){
                placeDetail.setText(mechinicObject.getUserBio());
            }else{
                placeDetail.setVisibility(View.GONE);
            }

            if(mechinicObject.getProfileImageUrl()!=null){

                //Picasso.get().load(placeData.getImageURL()).into(placeImage);

                Glide.with(PatientDetailActivity.this)
                        .load(mechinicObject.getProfileUrl())
                        .centerCrop()
                        .placeholder(R.drawable.bg_loading_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .error(R.drawable.bg_abstartct2)
                        .into(placeImage);

            }else{

                Picasso.get().load( R.drawable.bg_abstartct2).into(placeImage);

            }


            /*if((mechinicObject.getLatMAP()!=0) && (mechinicObject.getLongMAP()!=0)){

                LatLng latLng;
                latLng = new LatLng( (mechinicObject.getLatMAP()),(mechinicObject.getLongMAP()));

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        mapViewCard.setVisibility(View.VISIBLE);
                        mGoogleMap = googleMap;

                        mMapView.canScrollHorizontally(0);
                        mMapView.canScrollVertically(0);

                        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
                        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);

                        // LatLng latLng = new LatLng(31.4156498,74.2714092);

                        //String tittle = placeData.getName();
                        String tittle = mechinicObject.getName();
                        Log.i(TAG, "onMapReady: lat long :"+latLng );
                        Log.i(TAG, "onMapReady: tittle :"+tittle );
                        //gmap.setMyLocationEnabled(true);
                        //To add marker
                        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(tittle).icon(bitmapDescriptorFromVector(MechinicDetailActivity.this,R.drawable.ic_mark_map_circle)));
                        // For zooming functionality
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        //gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                    }
                });


            }
            else{
                mMapView.setVisibility(View.GONE);
            }*/


        }else{
            Log.e(TAG, "onCreate: no intent received" );
        }




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navController.navigate(R.id.action_placeDetailFragment_to_navigation_home_fragment);
                // navController.popBackStack();
                PatientDetailActivity.this.finish();
            }
        });


        mMapView.onResume();
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //MapsInitializer.initialize(this);  //for fragment

}
    private void openfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    private void uploadfile() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        int randomPIN = (int)(Math.random()*9000)+1000;

        DatabaseReference placeImagesRef = FirebaseDatabase.getInstance().getReference("place").child(mechinicObject.getId()).child("placeImages");

        if (imageuri !=null){
           // StorageReference  filereference  = mStorageref.child(System.currentTimeMillis()+ "."+getFileExtension(imageuri));
            StorageReference  filereference  = mStorageref.child(mechinicObject.getId()+"/"+mechinicObject.getId()+"-"+randomPIN+ "."+getFileExtension(imageuri));

            filereference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "StorageReference: " + "photo uploaded");

                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()

                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e(TAG, "DatabaseReference: " + "photo uploaded. Uri: "+uri.toString());
                                Uri downloadUrl = uri;
                                //UploadImage upload = new UploadImage(placeId+"-"+randomPIN,downloadUrl.toString());
                                progressDialog.show();

                                //String  uploadId = mDatabaseref.push().getKey();
                                placeImagesRef.child(mechinicObject.getId()+"-"+randomPIN).setValue(downloadUrl.toString());
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.dismiss();

                                Toasty.success(PatientDetailActivity.this,"Uploaded Successfully",Toasty.LENGTH_SHORT).show();

                                //imagesRecyclerView.setVisibility(View.VISIBLE);
                                //getImagesList(placeId);


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if (e instanceof IOException)
                                    Toast.makeText(PatientDetailActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                else
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                            }
                        });
                }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: "+e.getLocalizedMessage()+" causes: "+e.getCause().getMessage() );
                        if (e instanceof IOException)
                            Toast.makeText(PatientDetailActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                        else
                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });

        }else
            Toast.makeText(this, "Please Select a Image", Toast.LENGTH_SHORT).show();



    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null &&
                data.getData()!=null){
            imageuri = data.getData();
            //Picasso .get().load(imageuri).into(imageView);

            uploadfile();

        }
    }


        private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }








    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void recyclerviewClick(int position) {
        Log.e(TAG, "recyclerviewClick: working");

    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}