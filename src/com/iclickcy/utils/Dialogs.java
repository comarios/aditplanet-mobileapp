package com.iclickcy.utils;

import com.iclickcy.main.ValidateByQRCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Dialogs {

	public void createDialogINTERNET(final Activity activity,
			final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Cannot connect to Aditplanet")
				.setMessage("No Internet connection available.")
				.setCancelable(true)
				.setPositiveButton("Turn on Wi-Fi",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Intent networkSettings = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
								// Flag is required due to calling of new
								// activity outside of the MainActivity's
								// context
								networkSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(networkSettings);

								// context.startActivity(new
								// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void createDialogLoginERROR(final Activity activity,
			final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Invalid login credentials")
				.setCancelable(true)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	public void createDialogValidation(final Activity activity,
			final Context context, String validationOutput) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(validationOutput)
				.setCancelable(true)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void createDialogQRValidation(final Activity activity,
			final Context context, String validationOutput, final ValidateByQRCode fragment) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(validationOutput)
				.setCancelable(true)
				.setNegativeButton("Press to Scan again",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

								//fragment.releaseCamera();
								fragment.unblockCamera();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
