package com.my.AndroidPatientTracker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OldMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);



    }


    public void do_stuff(){
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();


    }

}