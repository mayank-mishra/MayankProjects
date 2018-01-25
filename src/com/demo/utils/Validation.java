package com.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Validation {

	public static String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_REGEX = "\\d{3}-\\d{7}";

	// call this method when you need to check email validation
	public static boolean isEmailAddress(EditText editText, boolean required,
			String errMsg) {
		//Log.i("-----Eit Text Id----", ":" + editText.getId());
		return isValid(editText, EMAIL_REGEX, errMsg, required);
	}

	public static boolean isPassword(EditText editText, boolean required,
			String errMsg) {
		//Log.i("-----Eit Text Id----", ":" + editText.getId());
		return isPasswordValid(editText, EMAIL_REGEX, errMsg, required);
	}

	// call this method when you need to check phone number validation
	public static boolean isPhoneNumber(EditText editText, boolean required,
			String errMsg) {

		return isValid(editText, PHONE_REGEX, errMsg, required);
	}

	public static boolean isPasswordValid(EditText editText, String regex,
			String errMsg, boolean required) {

		String text = editText.getText().toString().trim();
		// clearing the error, if it was previously set by some other values
		editText.setError(null);

		// text required and editText is blank, so return false
		if (required && !hasTextAll(editText, errMsg))
			return false;

		// pattern doesn't match so returning false
		if (required && text.length() < 6) {
			editText.setError(errMsg);
			editText.requestFocus();
			return false;
		}
		;

		return true;
	}

	// return true if the input field is valid, based on the parameter passed
	public static boolean isValid(EditText editText, String regex,
			String errMsg, boolean required) {

		String text = editText.getText().toString().trim();
		// clearing the error, if it was previously set by some other values
		editText.setError(null);

		// text required and editText is blank, so return false
		if (required && !hasTextAll(editText, errMsg))
			return false;

		// pattern doesn't match so returning false
		if (required && !Pattern.matches(regex, text)) {
			editText.setError(errMsg);
			editText.requestFocus();
			return false;
		}
		;

		return true;
	}

	public static boolean hasValidPhone(EditText editText, String msg) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() == 0 || text.length() < 10) {
			editText.setError(msg);
			editText.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean hasTextAll(EditText editText, String msg) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() == 0) {
			editText.setError(msg);
			editText.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean hasValidDate(TextView editText, String msg,
			Date startDate, Date endDate) {

		// String text = editText.getText().toString().trim();
		editText.setError(null);

		if (endDate.compareTo(startDate) < 0
				|| endDate.compareTo(startDate) == 0) {
			editText.requestFocus();
			editText.setError(msg);

			return false;
		}

		return true;
	}

	public static String NullChecker(String var) {

		if (var == null) {
			return "";
		} else if (var.equals("null")) {
			return "";
		} else {
			return var;
		}
	}

	public static String change_US_DateFormat(String date_string) {

		SimpleDateFormat actualformat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm a");
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		Date formetteddate = null;
		try {
			// //Log.i("-------------Actual Date-------------",":"+date_string);
			formetteddate = actualformat.parse(date_string);
		} catch (Exception e) {
			// TODO: handle exception
			// //Log.i("-------------Date Change Exception-------------",":"+e.getMessage());
			return "";
		}
		return format.format(formetteddate).toString();
	}

	public static String change_US_ONLY_DateFormat(String date_string,String eventDateFormat) {

		SimpleDateFormat actualformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone tzInAmerica = TimeZone.getTimeZone("GMT");
		actualformat.setTimeZone(tzInAmerica);
		
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		TimeZone tzInAmerica1 = TimeZone.getTimeZone(eventDateFormat);
		format.setTimeZone(tzInAmerica1);
		
		Date formetteddate = null;
		try {
			// //Log.i("-------------Actual Date-------------",":"+date_string);
			formetteddate = actualformat.parse(date_string);
			
		} catch (Exception e) {
			// TODO: handle exception
			// //Log.i("-------------Date Change Exception-------------",":"+e.getMessage());
			return "";
		}
		return format.format(formetteddate).toString();
	}

	public static String changeDateFromattWithSec(String date_time) {
		SimpleDateFormat actualformat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("MMM yyyy, dd");
		Date formetteddate = null;
		try {
			formetteddate = actualformat.parse(date_time);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		return format.format(formetteddate).toString();

	}

	public static boolean isPastEvent(String enddate) {
		boolean ispast = false;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todaydatestring = format.format(date);

		try {
			Date eventdate = format.parse(enddate);

			Date todaydate = format.parse(todaydatestring);
			if (todaydate.after(eventdate)) {
				ispast = true;
			} else {
				ispast = false;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ispast;
	}

	public static String getDateTimeForOrders(String dateInString){
		try { 
			//SimpleDateFormat timesettingsFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd,yyyy hh:mm aa");
			Date date = sdf1.parse(dateInString); 
			return sdf2.format(date);
		} 
		catch (ParseException exp) { 
			exp.printStackTrace(); }
		return null;
	}
	
}
