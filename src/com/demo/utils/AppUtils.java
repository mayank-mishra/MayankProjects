package com.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class AppUtils {

	public static final boolean isLogEnabled = true;
	public static final String INTENT_KEY = "intent_key";

	public static void displayLog(String tag, String message) {
		if (isLogEnabled) {
			Log.i(tag, message);
		}
	}

	public static void showError(final Activity ctxt, String message,
			final boolean isFinish) {
		if (ctxt != null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					ctxt);

			alertDialogBuilder.setTitle("Alert");
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									if (isFinish) {
										ctxt.finish();
									}
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	public static Boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;
		else
			return false;
	}

	public static String getQuery(List<NameValuePair> params) {
		StringBuilder result = new StringBuilder();
		try {
			boolean first = true;
			for (NameValuePair pair : params) {
				// Log.i("-----------------Name And Value----------------",
				// ":"+pair.getName()+":"+pair.getValue());
				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			// Log.i("-----------------Name And Value Exception----------------",
			// ":"+e.getMessage());
		}
		return result.toString();
	}

	public static String NullChecker(String var) {

		if (var == null || var.equals("null")) {
			return "";
		} else {
			return var;
		}

	}

	public static String getUTFData(String input) {
		String utfString = "";
		try {
			utfString = URLEncoder.encode(input, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utfString;
	}

	/**
	 * Returns a list with all links contained in the input
	 */
	public static List<String> extractUrls(String text) {
		List<String> containedUrls = new ArrayList<String>();
		String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(text);

		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0),
					urlMatcher.end(0)));
		}

		return containedUrls;
	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email) {
		boolean isValidEmail = false;
		try {

			String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			if (Pattern.matches(emailPattern, email)) {
				isValidEmail = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValidEmail;
	}

	/**
	 * @author babu
	 * @param context
	 * @return
	 */

	public static boolean isAppOnForeground(Context context) {
		try {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}
			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/*@SuppressLint("NewApi")
	public static Date getDate(String strdate) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(strdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}*/
	
	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	public static String getStringDate(Date date) {
		
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String data = df.format(new Date());
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//return dateFormat.format(date);
}
}
