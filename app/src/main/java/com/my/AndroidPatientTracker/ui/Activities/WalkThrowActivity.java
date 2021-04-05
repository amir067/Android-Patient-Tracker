package com.my.AndroidPatientTracker.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.adapters.Adapter_walkthrough;
import com.my.AndroidPatientTracker.utils.Tools;

import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

public class WalkThrowActivity extends AppCompatActivity {

    private static final String TAG = "WalkThrowActivity";

    public ViewPager viewpager;
    private int[] layouts;
    private  Button getStartButton;

    Adapter_walkthrough adapter_walkthrough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_throw);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //ani_loading= findViewById(R.id@color/ThemeColorOne.anim_loading_gif);
         Window window = WalkThrowActivity.this.getWindow();
         //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
         window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

       //  window.setStatusBarColor(ContextCompat.getColor(WalkThrowActivity.this, R.color.common_google_signin_btn_text_dark_disabled));
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);

        viewpager = findViewById(R.id.viewpager);

        getStartButton = findViewById(R.id.btn_get_start);

        CircleIndicator indicator = findViewById(R.id.indicator);

        adapter_walkthrough = new Adapter_walkthrough(getSupportFragmentManager());


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.fragment_walkthroughone,
                R.layout.fragment_walkthroughtwo,
                R.layout.fragment_walkthroughthree};

        viewpager.setAdapter(adapter_walkthrough);

        indicator.setViewPager(viewpager);

        adapter_walkthrough.registerDataSetObserver(indicator.getDataSetObserver());




        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT

                    getStartButton.setText("Get Started");

                } else {
                    // still pages are left
                    getStartButton.setText("Next");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewpager.addOnPageChangeListener(viewPagerPageChangeListener);

        getStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if last page home screen will be launched
                int current = getItem(+1);
                if(getStartButton.getText().equals("Next")){
                    // move to next screen
                    viewpager.setCurrentItem(current);

                }else{
                    brn_get_start();
                   // Toasty.success(WalkThrowActivity.this,"Finished called",Toasty.LENGTH_SHORT).show();

                }

            }
        });


    }

    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }

    public  void brn_get_start( ){
        SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
        editor.putString("user_type", "old");
        editor.apply();
        finish();

    }
}