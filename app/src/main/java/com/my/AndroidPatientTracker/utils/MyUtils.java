package com.my.AndroidPatientTracker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.my.AndroidPatientTracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MyUtils {

    public static AlertDialog getLoadingDialog(Activity context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_loading_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        return alertDialog;
    }

    public static AlertDialog getLoadingDialogWith2(Activity context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_loading_dialog2, null);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setDimAmount(0);

        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);

        return alertDialog;
    }






    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidUrl(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.WEB_URL.matcher(target).matches();
    }

    public final static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public static AlertDialog getLoadingDialog(Activity context){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = context.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.progress_dialog_layout, null);
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
//        AlertDialog alertDialog = dialogBuilder.create();
//        return alertDialog;
//    }

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title).setMessage(message).show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateFormatter(String time) {
        String inputPattern = "E MMM dd HH:mm:ss z yyyy";
        String outputPattern = "hh:mm:a dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            assert date != null;
            str = outputFormat.format(date);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }

    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensans.ttf"));
    }

}
