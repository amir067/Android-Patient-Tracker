package com.my.AndroidPatientTracker.ui.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.ui.Dashboard.HomeActivity;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.PreferenceHelperDemo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static com.my.AndroidPatientTracker.utils.MyUtils.isValidEmail;


//import static my.personal.psychiatrist.ui.auth.LoginActivity.isValidEmail;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;

    DatabaseReference reference;

    @BindView(R.id.r_username)
    EditText rUsername;

    @BindView(R.id.r_PhoneNumber)
    EditText rPhoneNumber;

    @BindView(R.id.r_Age)
    EditText rAge;

    @BindView(R.id.r_email)
    EditText rEmail;

    @BindView(R.id.r_password)
    EditText rPassword;

    @BindView(R.id.r_gender)
    Spinner rGender;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef = db.collection("users");

    private static final String TAG = "RegisterActivity";
    private AlertDialog loading_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        loading_dialog = MyUtils.getLoadingDialog(this);

       // loading.setVisibility(View.INVISIBLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rGender.setAdapter(adapter);


    }


    public void onRegisterClick(View view) {

        String username = rUsername.getText().toString();
        String email = rEmail.getText().toString();
        String age = rAge.getText().toString();
        String password = rPassword.getText().toString();
        String phone = rPhoneNumber.getText().toString();
        String gender = rGender.getSelectedItem().toString();

       // loading.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(username)) {

            rUsername.setError("Enter you Name");
            rUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {

            rPhoneNumber.setError("Enter you Phone No.");
            rPhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(age)) {

            rAge.setError("Enter you age");
            rAge.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {

            rEmail.setError("Please Enter a Valid Email");
            rEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {

            rPassword.setError("Enter you Password");
            rPassword.requestFocus();
            return;
        }

        if (rGender.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceHelperDemo preferenceHelperDemo = new PreferenceHelperDemo(this);

        preferenceHelperDemo.setKey("gender", gender);

        loading_dialog.show();
       // loading.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                loading_dialog.hide();
                Toasty.success(RegisterActivity.this,"Sign Up Success. Please wait..",Toasty.LENGTH_SHORT).show();

               // loading.setVisibility(View.GONE);

                FirebaseUser firebaseUser = auth.getCurrentUser();

                String userid = firebaseUser.getUid();

                 DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                Map<String, Object> register_user = new HashMap<>();
                register_user.put("id", userid);
                register_user.put("email", email);
                register_user.put("password", password);
                register_user.put("name", username);
                register_user.put("phone", phone);
                register_user.put("age", age);
                register_user.put("gender", gender);
                register_user.put("created_at", new Date().getTime());
                register_user.put("updated_at", new Date().getTime());
                register_user.put("imageURL", "default");

                reference.setValue(register_user).addOnSuccessListener(aVoid -> {
                    loading_dialog.dismiss();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();

                }).addOnFailureListener(e -> {
                    Toasty.warning(RegisterActivity.this,"Profile update failed!",Toasty.LENGTH_SHORT).show();
                    // loading.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    loading_dialog.dismiss();
                });

            } else {
                Log.d(TAG, "onComplete: " + task.getException().getMessage().toString());
                Toasty.error(RegisterActivity.this, "You Can't register with this email /E-mail already register", Toast.LENGTH_SHORT).show();
               // loading.setVisibility(View.INVISIBLE);
                loading_dialog.dismiss();
            }

        });

    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }


    public void onBackPressed(View v) {
        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }


}



