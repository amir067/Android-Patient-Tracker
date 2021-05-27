package com.my.AndroidPatientTracker.ui.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.ui.Dashboard.HomeActivity;
import com.my.AndroidPatientTracker.ui.Rooms.RoomObject;
import com.my.AndroidPatientTracker.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class ChatActivity extends AppCompatActivity  implements RecyclerviewOnClickListener{

    private static final String TAG = "ChatActivity";

    String userId="";
    String userName="";
    String userImg="";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    ImageButton sendMessage;
     ArrayList<ChatModel> chatList = new ArrayList<ChatModel>();
    ChatListAdapter chatListAdapter;

    RecyclerView chatRV;

    EditText messagesTextET;
    UserModel userModel = new UserModel();
    TextView lable;

    String senderName="";
    String senderImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendMessage = findViewById(R.id.sendBtn);
        messagesTextET = findViewById(R.id.et_input_text);
        lable = findViewById(R.id.textView18);
        chatRV = findViewById(R.id.recyclerView);
        getSupportActionBar().hide();


        if(getIntent() !=null) {

            userId = getIntent().getStringExtra("id");
            userName = getIntent().getStringExtra("name");
            userImg = getIntent().getStringExtra("img");

            lable.setText("Recommend Medicines to "+userName);

            Log.e(TAG, "onCreate: get intent> id received: "+userId );
            Log.e(TAG, "onCreate: get intent> name received: "+userName );
            Log.e(TAG, "onCreate: get intent> img received: "+userImg );

        }

         LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);

        chatListAdapter = new ChatListAdapter(ChatActivity.this, chatList, new RecyclerviewOnClickListener() {
            @Override
            public void recyclerviewClick(int position) {
                Log.e(TAG, "recyclerviewClick: ok" );
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        chatRV.setHasFixedSize(true);
        chatRV.setLayoutManager(layoutManager);
        //chatRV.setAdapter(chatListAdapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messagesTextET.getText().toString();
                messagesTextET.setText("");
                MyUtils.hideKeyboard(ChatActivity.this);
                sentMessages(message);
            }
        });

        getUserData();

        loadChats(userId);
    }

    private void sentMessages(String message) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //String userId = firebaseUser.getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("chat");
        int randomId = new Random().nextInt(400000) + 1000; // [0, 60] + 20 => [20, 80]



        HashMap<Object, Object> PatientMap = new HashMap<>();
        PatientMap.put("message",message);
        PatientMap.put("senderName", senderName);

        if(!senderImage.isEmpty()){
            PatientMap.put("senderImage",senderImage);
        }

        PatientMap.put("messageTime",System.currentTimeMillis());

        rootRef.child(String.valueOf(randomId)).setValue(PatientMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(ChatActivity.this,"Message send Successfully",Toasty.LENGTH_SHORT).show();
                Log.e(TAG, "onSuccess: patient admit to room success" );
              loadChats(userId);
            }
        });

    }

    private void loadChats(String userId) {
        // loading_dialog.show();
        chatList.clear();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("chat");
        Query query = rootRef.orderByChild("messageTime");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Log.e(TAG, "onDataChange: data received: " );

                    for (DataSnapshot data :  snapshot.getChildren())
                    {
                        ChatModel temRoomObject = data.getValue(ChatModel.class);
                        chatList.add(temRoomObject);

                    }

                    Log.e(TAG, "load data success: chat List " );


                    chatListAdapter.setPlaceObjects(chatList);
                    chatListAdapter.doNotifyDataChanged();
                    chatRV.setAdapter(chatListAdapter);

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
                                Log.e(TAG, "onDataChange: get user name received:"+userModel.getName() );
                                senderName = userModel.getName();
                            }

                            if(userModel.getProfileImageUrl() !=null){
                                Log.e(TAG, "onDataChange: get user img received:"+userModel.getProfileImageUrl() );
                                senderImage = userModel.getProfileImageUrl();
                            }else{
                                Log.e(TAG, "onDataChange: get user img null received:");

                            }

                        }
                        catch (Exception e){
                            Log.e(TAG, "Exception Error: "+e.getLocalizedMessage() );

                        }

                    } else {
                        Log.e(TAG, "onDataChange: snapshoot not exists!" );

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.getDetails() );
                }

            });
        }


    }



    @Override
    public void recyclerviewClick(int position) {
        Log.e(TAG, "recyclerviewClick: 66line ok" );
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}