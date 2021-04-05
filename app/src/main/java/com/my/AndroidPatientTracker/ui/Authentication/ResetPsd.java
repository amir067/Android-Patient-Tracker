package com.my.AndroidPatientTracker.ui.Authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.zeptosystem.R;
//import com.example.zeptosystem.ui.activities.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.utils.Tools;


public class ResetPsd extends AppCompatActivity {
    private EditText inputEmail1;
    private Button btnReset1, btnBack1;
    private FirebaseAuth auth1;
    private ProgressBar progressBar1;
    EditText et_email1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_rstpsd);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);

        inputEmail1 = (EditText) findViewById(R.id.editTextRsdpsd);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar2);

        btnReset1 = (Button) findViewById(R.id.editTextRsdpsdbtn);
        btnBack1 = (Button) findViewById(R.id.btnrsdback);

        auth1 = FirebaseAuth.getInstance();

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnReset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail1.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar1.setVisibility(View.VISIBLE);
                    auth1.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPsd.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                       // Intent loginIntent = new Intent(ResetPsd.this, MainActivity.class);
                                       // startActivity(loginIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(ResetPsd.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }

                                    progressBar1.setVisibility(View.GONE);
                                }
                            });

                }





            }
        });









    }
}
