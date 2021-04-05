package com.my.AndroidPatientTracker.chatbot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.models.UserModel;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLoginHandler;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.users.KMUser;

public class ChatBotActivity extends AppCompatActivity {
    public   static  String TAG="ChatBotActivity";

    FirebaseUser Firebasuser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private AlertDialog loading_dialog;
    PreferenceHelperDemo UserDataPreference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usr_name,imageURL;
    String age,email,gender,Userid,password,phone,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        mAuth = FirebaseAuth.getInstance();
        loading_dialog = MyUtils.getLoadingDialog(this);
        UserDataPreference = new PreferenceHelperDemo(this);
        Kommunicate.init(getApplicationContext(), "1266288bcc2187545d5c0dd335d674155");


        //ApplozicSetting.getInstance(ChatBotActivity.this).setParentActivity("doctors.on.hand.ui.main");

       // loading_dialog.show();
        //loading_dialog.dismiss();


        // FechUserData();
        //launchChat();
    }


    private void FechUserData() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                }
                UserModel types = documentSnapshot.toObject(UserModel.class);

                Userid = types.getId();
                username = types.getUsername();
                email = types.getEmail();
                imageURL = types.getImageURL();
                age = types.getAge();
                gender = types.getGender();
                password = types.getPassword();
                phone = types.getPhone();

                UserDataPreference.setKey("id", Userid);
                UserDataPreference.setKey("username", username);
                UserDataPreference.setKey("email", email);
                UserDataPreference.setKey("imageURL", imageURL);
                UserDataPreference.setKey("age", age);
                UserDataPreference.setKey("gender", gender);
                UserDataPreference.setKey("phone", phone);

                Log.d(TAG, "FechUserData Success  user ID:" + types.getId());

                // Toast.makeText(ChatBotActivity.this, "FechUserData"+Userid+username+imageURL, Toast.LENGTH_SHORT).show();
                ChatBotActivity.this.RegisterUserOnKommunicate();

            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(ChatBotActivity.this, "error", Toast.LENGTH_SHORT).show();
        });
    }
    protected void RegisterUserOnKommunicate() {

        //Toast.makeText(ChatBotActivity.this, "onResumed Executed", Toast.LENGTH_SHORT).show();
        KMUser kmUser = new KMUser();
        kmUser.setUserId(Userid); // Pass a unique key
        //kmUser.setUserId("5875878itsBlockedId"); // Pass a unique key


       // user.setPassword(<PASSWORD>); //Optional
        kmUser.setImageLink(imageURL); //  Pass the image URL for the user's display image
        kmUser.setDisplayName(username); //Pass the display name of the user
        kmUser.setEmail(email); //
        kmUser.setContactNumber(phone);


        //UserService.getInstance(ChatBotActivity.this).updateLoggedInUser(kmUser);


        Kommunicate.login(this, kmUser, new KMLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                // You can perform operations such as opening the conversation, creating a new conversation or update user details on success
              //  Toast.makeText(ChatBotActivity.this, "authorize a user Success", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Kommunicate.login/Register Success");

                SharedPreferences.Editor editor = getSharedPreferences("KommunicateUser", MODE_PRIVATE).edit();
                editor.putString("user_type", "old");
                editor.apply();

               LaunchConversetion(kmUser);

            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                // You can perform actions such as repeating the login call or throw an error message on failure
                Toast.makeText(ChatBotActivity.this, "authorize a user Failure on kommunicate", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Kommunicate.login/Register Failure");
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();
        Firebasuser = mAuth.getCurrentUser();

        SharedPreferences user_type = getSharedPreferences("KommunicateUser", Context.MODE_PRIVATE);
        String user_typ = user_type.getString("user_type", "");
        if(user_typ.equals("old")){
            Log.d(TAG, "Kommunicate user_type: old");
           // launchChat();
            String userid = Firebasuser.getUid();
            KMUser Kuser = new KMUser();
            Kuser.setUserId(userid);
            LaunchConversetion(Kuser);

            ////////////////testing Raw
          //  Toast.makeText(this, "old", Toast.LENGTH_SHORT).show();
         //   FechUserData();
          //  KMUser kmUser = new KMUser();
          //  kmUser.setDisplayName("Amir Sohail");
          //  kmUser.setContactNumber("03000971323");
          //  UserService.getInstance(ChatBotActivity.this).updateLoggedInUser(kmUser);



           // KMUser.isLoggedIn(ChatBotActivity.this);
            {
                // A check for identifying whether the user is logged in

            };
           // Toast.makeText(this, "LoggedIn"+ KMUser.isLoggedIn(ChatBotActivity.this), Toast.LENGTH_SHORT).show();
        //   FechUserData();
           // KMUser kmUser = KMUser.getLoggedInUser(ChatBotActivity.this);
           // Toast.makeText(ChatBotActivity.this, "Current  user psd: "+kmUser.getPassword(), Toast.LENGTH_SHORT).show();

           // KMUser kmUser = KMUser.getLoggedInUser(ChatBotActivity.this);
           //  Toast.makeText(ChatBotActivity.this, "Current  user psd: "+kmUser.getPassword(), Toast.LENGTH_SHORT).show();
           //  Log.d(TAG, "Current  user psd: "+kmUser.getPassword());

            /*
            Kommunicate.logout(this, new KMLogoutHandler() {
                @Override
                public void onSuccess(Context context) {
                    Toast.makeText(ChatBotActivity.this, "logout Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception exception) {

                }
            });


             */

        }else{
            Log.d(TAG, "Kommunicate user_type: new");
            FechUserData();
           // Toast.makeText(this, "new", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        ApplozicSetting.getInstance(ChatBotActivity.this).setParentActivity("doctors.on.hand.ui.main.MainActivity");
        //Toast.makeText(ChatBotActivity.this, "onResumed Executed", Toast.LENGTH_SHORT).show();
    }

    private void LaunchConversetion(KMUser kmUser) {
        ApplozicSetting.getInstance(ChatBotActivity.this).setParentActivity("doctors.on.hand.ui.main.MainActivity");
        new KmConversationBuilder(ChatBotActivity.this)
                .setAppId("1266288bcc2187545d5c0dd335d674155")
                .setKmUser(kmUser)
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Log.d(TAG, "Conversation Start Success : " + message);

                        finish();
                    }

                    @Override
                    public void onFailure(Object error) {
                        //  Log.d("Conversation", "Failure : " + error);
                        Log.d(TAG, "Conversation Start Failure : " + error);
                        Toast.makeText(ChatBotActivity.this, "Conversation Failure. May be user deleted or blocked!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    protected void launchChat() {
        ApplozicSetting.getInstance(ChatBotActivity.this).setParentActivity("doctors.on.hand.ui.main.MainActivity");
        new KmConversationBuilder(ChatBotActivity.this)
                .setAppId("1266288bcc2187545d5c0dd335d674155")
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Log.d("Conversation", "Success : " + message);

                        finish();

                       // KMUser kmUser = KMUser.getLoggedInUser(ChatBotActivity.this);
                       // Toast.makeText(ChatBotActivity.this, "Current  user psd: "+kmUser.getPassword(), Toast.LENGTH_SHORT).show();
                       // Log.d(TAG, "Current  user psd: "+kmUser.getPassword());
                        //resolved path e.g: kommunicate.io.sample.MainActivity
                    }

                    @Override
                    public void onFailure(Object error) {
                        Log.d("Conversation", "Failure : " + error);
                        Toast.makeText(ChatBotActivity.this, "Conversation Failure. May be user deleted or blocked!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


    }

}