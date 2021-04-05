package com.my.AndroidPatientTracker.ui.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.my.AndroidPatientTracker.OldMainActivity;
import com.my.AndroidPatientTracker.R;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.github.zagum.switchicon.SwitchIconView;
import com.shawnlin.numberpicker.NumberPicker;
import java.util.Calendar;
import java.util.Locale;

public class SecondFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd1,tpd2;
    TextView time_1st_noti,time_2end_noti;
    Boolean Isfirst_tpd=false;

    private static String TAG = "SecondFragment";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Find our View instances
        time_1st_noti =  getView().findViewById(R.id.time_1st_notificatio);
        time_2end_noti =  getView().findViewById(R.id.time_2end_notificatio);

        time_1st_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Isfirst_tpd=true;
                Calendar now = Calendar.getInstance();
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
                if (tpd1 == null) {
                    tpd1 = TimePickerDialog.newInstance(
                            SecondFragment.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );
                } else {
                    tpd1.initialize(
                            SecondFragment.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            now.get(Calendar.SECOND),
                            false
                    );
                }
                //tpd.setThemeDark();
                //tpd.vibrate();
                //tpd.dismissOnPause();
                //tpd.enableSeconds();
                tpd1.setVersion(TimePickerDialog.Version.VERSION_2);
                tpd1.setAccentColor(Color.parseColor("#9C27B0"));
                tpd1.setTitle("Pic Time");
                tpd1.setOnCancelListener(dialogInterface -> {
                    Log.d("TimePicker", "Dialog was cancelled");
                    tpd1 = null;
                });
                tpd1.show(requireFragmentManager(), "Timepickerdialog");

            }
        });


        time_2end_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
                if (tpd2 == null) {
                    tpd2 = TimePickerDialog.newInstance(
                            SecondFragment.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );
                } else {
                    tpd2.initialize(
                            SecondFragment.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            now.get(Calendar.SECOND),
                            false
                    );
                }
                //tpd.setThemeDark();
                //tpd.vibrate();
                //tpd.dismissOnPause();
                //tpd.enableSeconds();
                tpd2.setVersion(TimePickerDialog.Version.VERSION_2);
                tpd2.setAccentColor(Color.parseColor("#9C27B0"));
                tpd2.setTitle("Pic Time");
                tpd2.setOnCancelListener(dialogInterface -> {
                    Log.d("TimePicker", "Dialog was cancelled");
                    tpd2 = null;
                });
                tpd2.show(requireFragmentManager(), "Timepickerdialog");
            }
        });


        AlphaAnimation animation=new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(1500);
        TextView txt_w=getView().findViewById(R.id.txt_walk);
        txt_w.setAnimation(animation);

        view.findViewById(R.id.back_clk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        TextView Text_DN_switch= getView().findViewById(R.id.txt_DN_switch);
        SwitchCompat sw_DN= getView().findViewById(R.id.SW_DN);
        sw_DN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if(state){
                    Text_DN_switch.setText("ON");
                }else{
                    Text_DN_switch.setText("OFF");
                }
            }
        });

        TextView Text_PN_switch= getView().findViewById(R.id.txt_PN_Sw);
        SwitchCompat sw_PN= getView().findViewById(R.id.SW_PN);
        sw_PN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if(state){
                    Text_PN_switch.setText("ON");
                }else{
                    Text_PN_switch.setText("OFF");
                }
            }
        });

        view.findViewById(R.id.btn_vibrate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchIconView btn1 = (SwitchIconView) getView().findViewById(R.id.btn_vibrate1);
                btn1.switchState(true);
            }
        });
        view.findViewById(R.id.btn_ringer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchIconView btn1 = (SwitchIconView) getView().findViewById(R.id.btn_ringer1);
                btn1.switchState(true);
            }
        });
        view.findViewById(R.id.btn_flash1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchIconView btn1 = (SwitchIconView) getView().findViewById(R.id.btn_flash1);
                btn1.switchState(true);
            }
        });
        view.findViewById(R.id.btn_vibrate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchIconView btn1 = (SwitchIconView) getView().findViewById(R.id.btn_vibrate2);
                btn1.switchState(true);
            }
        });
        view.findViewById(R.id.btn_ringer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchIconView btn1 = (SwitchIconView) getView().findViewById(R.id.btn_ringer2);
                btn1.switchState(true);
            }
        });
        view.findViewById(R.id.btn_flash2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchIconView btn1 = (SwitchIconView) getView().findViewById(R.id.btn_flash2);
                btn1.switchState(true);
            }
        });

        Button btn1 = (Button) getView().findViewById(R.id.button_save);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OldMainActivity)getActivity()).do_stuff();
            }
        });




        final NumberPicker numberPicker = getView().findViewById(R.id.number_picker);

        // Set divider color
     //   numberPicker.setDividerColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
     //   numberPicker.setDividerColorResource(R.color.colorPrimary);

        // Set formatter
     //   numberPicker.setFormatter(getString(R.string.number_picker_formatter));
      //  numberPicker.setFormatter(R.string.number_picker_formatter);

        // Set selected text color
     //   numberPicker.setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
     //   numberPicker.setSelectedTextColorResource(R.color.colorPrimary);

        // Set selected text size
     //   numberPicker.setSelectedTextSize(getResources().getDimension(R.dimen.selected_text_size));
     //   numberPicker.setSelectedTextSize(R.dimen.selected_text_size);

        // Set selected typeface
     //   numberPicker.setSelectedTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
     //   numberPicker.setSelectedTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
     //   numberPicker.setSelectedTypeface(getString(R.string.roboto_light));
     //   numberPicker.setSelectedTypeface(R.string.roboto_light, Typeface.NORMAL);
     //   numberPicker.setSelectedTypeface(R.string.roboto_light);

        // Set text color
     //   numberPicker.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_grey));
     //   numberPicker.setTextColorResource(R.color.dark_grey);

        // Set text size
       // numberPicker.setTextSize(getResources().getDimension(R.dimen.text_size));
       // numberPicker.setTextSize(R.dimen.text_size);

        // Set typeface
        numberPicker.setTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setTypeface(getString(R.string.roboto_light));
        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setTypeface(R.string.roboto_light);

        // Set value
     //   numberPicker.setMaxValue(59);
     //   numberPicker.setMinValue(0);
     //   numberPicker.setValue(3);

        // Set string values
       String[] custom_data = {"500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500","5000"};
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(custom_data.length);
        numberPicker.setDisplayedValues(custom_data);
        numberPicker.setValue(3500);
        //Log.e(TAG, "onViewCreated: array_value:"+ Arrays.toString(custom_data).charAt(3) );



        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);

        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);

        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(false);

        // Set accessibility description enabled
        numberPicker.setAccessibilityDescriptionEnabled(true);

        // OnClickListener
        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click on current value");
            }
        });

        // OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        // OnScrollListener
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    Log.d(TAG, String.format(Locale.US, "newVal: %d", picker.getValue()));
                }
            }
        });
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay%12;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = ""+hourString+":"+minuteString;

        if(Isfirst_tpd){
            if(hourOfDay>12){
                time_1st_noti.setText(time+" PM");
            }else{
                time_1st_noti.setText(time+" AM");
            }
            Isfirst_tpd=false;
        }else{
            if(hourOfDay>12){
                time_2end_noti.setText(time+" PM");
            }else{
                time_2end_noti.setText(time+" AM");
            }

        }
        tpd1 = null;
        tpd2 = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tpd1 = null;
        tpd2 = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) requireFragmentManager().findFragmentByTag("Timepickerdialog");
        if(tpd != null) tpd.setOnTimeSetListener(this);
    }




}