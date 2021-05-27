package com.my.AndroidPatientTracker.ui.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.AndroidPatientTracker.Interface.RecyclerviewOnClickListener;
import com.my.AndroidPatientTracker.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendMessage = findViewById(R.id.sendBtn);
        messagesTextET = findViewById(R.id.et_input_text);
        chatRV = findViewById(R.id.recyclerView);
        getSupportActionBar().hide();


        if(getIntent() !=null) {

            userId = getIntent().getStringExtra("id");
            userName = getIntent().getStringExtra("name");
            userImg = getIntent().getStringExtra("img");
            Log.e(TAG, "onCreate: get intent> id received: "+userId );
            Log.e(TAG, "onCreate: get intent> name received: "+userName );
            Log.e(TAG, "onCreate: get intent> img received: "+userImg );

        }

         LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

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
        PatientMap.put("senderName",userName);
        PatientMap.put("senderImage",userImg);
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
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
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




    @Override
    public void recyclerviewClick(int position) {
        Log.e(TAG, "recyclerviewClick: 66line ok" );
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}