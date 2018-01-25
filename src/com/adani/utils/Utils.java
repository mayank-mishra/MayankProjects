package com.adani.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chandru-MacBook on 5/22/15.
 */
public class Utils {
	
	private static final boolean isLogEnable=true;
	
	public static interface OnDialogClickListener{
		public void OnPositiveButtonClick(AlertDialog alertDialog);
		public void OnNegativeButtonClick(AlertDialog alertDialog);
	}


    /**
     * Method for the typeface used in the app.
     * @param context
     * @return
     */
    public static Typeface getTypeface(Context context) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/font_regular.otf");
        return tf;

    }

    @SuppressLint("NewApi")
	public static void setTextAppearance(Context context, TextView textView, int resId) {

        if (Build.VERSION.SDK_INT < 23) {

            textView.setTextAppearance(context, resId);

        } else {

            textView.setTextAppearance(context, resId);
        }
    }


    @SuppressLint("NewApi")
	public static int getColor(Context context,int color ){

        if(Build.VERSION.SDK_INT >= 23){
            return getColor(context, color);
        }

        return context.getResources().getColor(color);
    }

    /**
     * Method to display Snacbar with short duration
     * @param view - View type (Eg: TextView, Button, EditText)
     * @param text - The text that should be shown on the Snackbar
     */
    /*public static void showSnackShort(View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }*/


    /**
     * Method to show toast message
     * @param context
     * @param message
     */
    public static void showToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    
	public static void Log(String string, String string2) {
		if(isLogEnable)
			Log.i(string, string2);
	}

}
