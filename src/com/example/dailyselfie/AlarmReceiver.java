package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private static final String TAG = "Alarm Receiver";
	public static final int NOTIFICATION_ID = 9999;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"received intent broadcast from alarm");
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 
				0, 
				new Intent(context.getApplicationContext(),MainActivity.class), 
				Intent.FLAG_ACTIVITY_NEW_TASK );
		sendNotification(context, pendingIntent);		
	}

	private void sendNotification(Context context, PendingIntent pendingIntent) {
		// TODO Auto-generated method stub
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		Notification notification = new Notification.Builder(context.getApplicationContext())
			.setContentTitle("Selfie")
			.setContentText("Time for a daily selfie")
			.setSmallIcon(android.R.drawable.stat_sys_warning)
			.setTicker("selfie time")
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.build();
		
		notificationManager.notify(NOTIFICATION_ID, notification);
		Log.i(TAG,"notification sent");
		
	}

}
