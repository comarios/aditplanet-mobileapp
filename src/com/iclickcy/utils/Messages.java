package com.iclickcy.utils;


import com.iclickcy.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.widget.Toast;
/**
 * This class is used globally to print messages like pop-up messages or toast messages.
 * 
 * @author Silouanos
 *
 */
public class Messages
{
	/** Main activity. */
	private static Activity mainActivity;
	
	final private static String informationMsg = "This application was created during Silouanos' MSc dissertation at the University of Kent.\nIf you need more information regarding the current application, feel free to contact me by sending an e-mail to sn269@kent.ac.uk.\nAuthor: Silouanos Nicolaou\nSupervisor: Nick Ryan\nUniversity of Kent\nSchool of computing";
	final private static String helpMsg = "Add new profile from the menu by pressing the 'plus' button.\nChange your settings from settings menu.\nChoose your default profile from Default Profile menu.\nRecommended settings: Calibration Samples: 80, Operation Samples: 10\n";
	final private static String authError = "Incorrect username and/or password";
	final private static String networkError = "Network error has occurred. Please try again.";
	/**
	 * Creates a new instance of Messages.
	 * 
	 * @param activity
	 * 					main activity.
	 */
	public Messages(Activity activity)
	{
		mainActivity = activity;
	}
	
	
	public static void showNetworkError(){
		Messages.popup_msg(networkError, "Network Error", R.drawable.info);
	}
	

	public static void showAuthError(){
		Messages.popup_msg(authError, "Authentication Error", R.drawable.error);
	}
	/**
	 * Pop-up message.
	 * 
	 * @param msg
	 *            message.
	 * @param title
	 *            title of dialog.
	 * @param imageID
	 *            image in title.
	 */
	public static void popup_msg(String msg, String title, int imageID)
	{
		AlertDialog.Builder alert_box = new AlertDialog.Builder(mainActivity);		
		alert_box.setTitle(title);
		alert_box.setMessage(Html.fromHtml("<small>"+msg+"</small>"));
		alert_box.setIcon(imageID);

		alert_box.setNeutralButton("Hide", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = alert_box.create();
		alert.show();
	}
	
	public static void showToash(String message)
	{
		Toast.makeText(mainActivity.getApplicationContext(),
				message, Toast.LENGTH_LONG)
				.show();
	}
}
