package com.my.AndroidPatientTracker.ui.Authentication;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.my.AndroidPatientTracker.R;

import com.my.AndroidPatientTracker.ui.Activities.WalkThrowActivity;
import com.my.AndroidPatientTracker.ui.Dashboard.HomeActivity;
import com.my.AndroidPatientTracker.utils.MyUtils;
import com.my.AndroidPatientTracker.utils.Tools;


public class LoginActivity extends AppCompatActivity {

    public  static  String TAG="LoginActivity";
    private int backpress = 0;
    private FirebaseUser user;
    private TextView headingTV;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText  email, password;
    private Button btn_login,btnforgetpsd;
    private AlertDialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_Eemail);
        password = findViewById(R.id.et_password);
        headingTV = findViewById(R.id.header_app_name);

        //ani_loading= findViewById(R.id@color/ThemeColorOne.anim_loading_gif);
        Window window = LoginActivity.this.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.common_google_signin_btn_text_dark_disabled));
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);

        TextView welcomeani = findViewById(R.id.header_app_name);
        AlphaAnimation welcome = new AlphaAnimation(0.0f, 1.0f);
        welcome.setDuration(4000);
        welcomeani.startAnimation(welcome);

        Shader shader = new LinearGradient(0,0,0,headingTV.getLineHeight(), Color.parseColor("#0090d0"), Color.parseColor("#33D3D2"), Shader.TileMode.REPEAT);
        headingTV.getPaint().setShader(shader);

        loading_dialog = MyUtils.getLoadingDialogWith2(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user);
    }

    public void reset_psd(View v){

        startActivity(new Intent(LoginActivity.this, ResetPsd.class));
    }



    private void updateUI(FirebaseUser user) {

        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else
        {
            SharedPreferences user_type = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String user_typ = user_type.getString("user_type", "");
            if(user_typ.equals("old")){
                Toast.makeText(this, "login to continue", Toast.LENGTH_SHORT).show();
             }else{
                startActivity(new Intent(LoginActivity.this, WalkThrowActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }






    public void onLogin(View view) {
       // startActivity(new Intent(MainActivity.this, LoginActivity.class));
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        if (TextUtils.isEmpty(txt_email) || !MyUtils.isValidEmail(txt_email)) {
            email.setError("Enter Email!");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(txt_password) ) {
            password.setError("Enter password!");
            password.requestFocus();
            return;
        }
        MyUtils.hideKeyboard(this);

        loading_dialog.show();

             //ani_loading.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loading_dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Authentication Sucess", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error:" + task.getException().getMessage());
                        loading_dialog.dismiss();
                        //ani_loading.setVisibility(View.INVISIBLE);
                    }

                }
            });
    }

    public void onRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        //finish();
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(vi, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }
}