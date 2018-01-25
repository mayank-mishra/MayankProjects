package com.adani.utils;

import android.content.Context;
/**
 * Created by chandrasekar on 22/10/16.
 */

public class ScreenUtils {


    /**
     * Boolean method to keep a check on screen traction.
     * @param context
     * @return
     */
    public static boolean isMainScreenActivated(Context context){

        return SharedPrefsUtils.getBoolean(Constants.SHARED_PREFS.KEY_FLAG_MAINSCREEN, false,context);
    }
}
