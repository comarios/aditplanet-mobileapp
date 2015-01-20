package com.iclickcy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.util.Log;

public class AutoLogin {

	public static String filenameLoginInternal_1 = "login_internal.txt";
	public static String filenameLoginInternal = "/login_internal.txt";
	public static String outputFolder = "/Login";
	public static String IS_LOGGED_IN_YES = "YES";
	public static String IS_LOGGED_IN_NO = "NO";
	public static String confFile = "login_internal.txt";

	public static void saveToFileLoggedIN(Activity activity, File getFilesDir,
			String what) {
		try {
			String filename = AutoLogin.confFile;
			File myFile = new File(getFilesDir + filename);
			if (myFile.exists())
				myFile.delete();
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.write(what);
			myOutWriter.close();
			fOut.close();
			// Toast.makeText(activity, "Done saving file -->" + what,
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean getAnswer(File getFilesDir) {
		String filename = AutoLogin.confFile;
		File myFile = new File(getFilesDir + filename);
		String loggedIN = null;
		if (myFile.exists()) {
			FileInputStream fIn;

			try {
				fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				loggedIN = myReader.readLine();
				System.out.println("Get answer: " + loggedIN);
				Log.e("value get answer", loggedIN);
				myReader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (loggedIN.equals(AutoLogin.IS_LOGGED_IN_YES))
			return true;
		else
			return false;

	}
}
