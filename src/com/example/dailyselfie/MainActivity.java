package com.example.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity  {
	private static final String TAG = "MainActivity";

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int THUMB_SIZE = 100;

	// for alarm related
	private static final long TO_MILLISECONDS = 60*1000;
	private static final long INITIAL_DELAY = 2*TO_MILLISECONDS;
	private static final long REPEAT_DELAY = 2*TO_MILLISECONDS;

	private MenuItem itemAlarm;
	private PendingIntent mAlarmOperation;

	private SharedPreferences mSharedPreferences;
	
	private String mCurrentPhotoPath; 
	private myArrayAdapter mAdapterS;
	private List<Selfie> selfieNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			showMessage("External Storage is not available.");
			finish();
		}

		if (savedInstanceState != null) {
			mCurrentPhotoPath = savedInstanceState.getString("selfiePath");
		}

		// setup action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | 
				ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		
		selfieNames = new LinkedList<Selfie>();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mCurrentPhotoPath = mSharedPreferences.getString("selfiePath",mCurrentPhotoPath);
		
		Log.i(TAG,"onResume mCurrentPhotoPath: "+mCurrentPhotoPath);
		
		// initialize adapter here	
		mAdapterS = new myArrayAdapter(this, R.layout.row_view, selfieNames);

		setListAdapter(mAdapterS);

	}
	protected void onPause() {
		// TODO - save stuff for next resume
		Log.i(TAG,"onPause mCurrentPhotoPath: "+mCurrentPhotoPath);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString("selfiePath",mCurrentPhotoPath);
		editor.commit();
		super.onPause();
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i(TAG,"list item clicked");
		Selfie selfie = (Selfie)mAdapterS.getItem(position);
		Log.i(TAG,"selfie selected");
		if (selfie != null){
			Log.i(TAG,"selfie exist");
			Intent intent = new Intent(this,EnlargeActivity.class);
			intent.putExtra("name",selfie.getName());
			intent.putExtra("path",selfie.getPath());
			
			Log.i(TAG,"ready to show selfie");
			startActivity(intent);
		}
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		itemAlarm = menu.findItem(R.id.action_alarm);
		//Setting the original enable/disable value for alarms
		setAlarm(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
			case R.id.action_picture:
				Log.d(TAG,"click on take picture");
				dispatchTakePictureIntent();
				break;
			
			case R.id.action_clear:
				Log.d(TAG,"click on clear all");
				deleteAllPictures();
				break;
				
			case R.id.action_alarm:
				Log.d(TAG,"click on toggle alarm");
				setAlarm(true);
				break;

			default:
				break;
		}
		return true;
	}
	
	private void deleteAllPictures() {
		// TODO delete all pictures
		mAdapterS.deleteallSelfie();
		showMessage("All pictures deleted.");
	}


	private void dispatchTakePictureIntent(){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if ( takePictureIntent.resolveActivity(getPackageManager())!= null){
			// create the file where the photo should go
			File photoFile = null;
			try {
				// successfully create file
				photoFile = BitmapUtil.createImageFile();
				mCurrentPhotoPath = photoFile.getAbsolutePath();
			} catch (IOException ex){
				// error occurred while creating the file
				showMessage("Cannot create the file.");
			}
			// continue only if the file was successfully created
			if (photoFile != null){
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_IMAGE_CAPTURE == requestCode) {
			if (resultCode == RESULT_CANCELED){
				Log.i(TAG,"user canceled, deleting file...");
				new File(mCurrentPhotoPath).delete();
			}
			if (resultCode == RESULT_OK) {
				
//				Log.i(TAG,"processing selfie");
//				Log.i(TAG,"current photo path: "+mCurrentPhotoPath);
				Selfie selfie = new Selfie();
				selfie.setLastModDate(Calendar.getInstance().getTime());
				selfie.setName(new File(mCurrentPhotoPath).getName());
				selfie.setPath(mCurrentPhotoPath);
				
				Log.i(TAG,"creating thumb bitmap");
				Bitmap fullSized = BitmapUtil.getBitmapFromFile(mCurrentPhotoPath);
				Float aspectRatio = ((float)fullSized.getHeight())/(float)fullSized.getWidth();
				Bitmap thumb = Bitmap.createScaledBitmap(
						fullSized,
						THUMB_SIZE, 
						(int)(THUMB_SIZE*aspectRatio), 
						false);
				String thumbPath = BitmapUtil.getThumbPath(mCurrentPhotoPath);
		        selfie.setThumbPath(thumbPath);
		        BitmapUtil.storeBitmapToFile(thumb, thumbPath);
		        
		    //    Log.i(TAG, "photoPath: "+mCurrentPhotoPath);
		    //    Log.i(TAG,"thumPath: "+thumbPath);
		        
		        Log.i(TAG,"recycling resources");
		        fullSized.recycle();
		        thumb.recycle();				
				mCurrentPhotoPath = null;
				
				// TODO: also add to listview
				Log.i(TAG,"adding selfie to adapter");
				mAdapterS.addSelfie(selfie);
				
			}
		}

	}
	


	private void showMessage(String mesg){
		Toast.makeText(getApplicationContext(),
				mesg, Toast.LENGTH_LONG)
				.show();
	}

	// TODO
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG,"configuration is changing, saving instance state");
		outState.putString("selfiePath", mCurrentPhotoPath);
	}
	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		Log.d(TAG,"configuration is changing, getting instance state");
		mCurrentPhotoPath = inState.getString("selfiePath");
	}
	
	
	// set alarm, done.
	protected void setAlarm(boolean toggle) {
		//Setting the alarm
		if (mAlarmOperation == null) {
			Log.d(TAG,"initiating alarm operation");
			mAlarmOperation = PendingIntent.getBroadcast(
				getApplicationContext(), 
				0, 
				new Intent(getApplicationContext(),AlarmReceiver.class), 
				0);
		}
		
		boolean alarmEnabled = mSharedPreferences.getBoolean("alarms", false);
		if (toggle) {
			Log.d(TAG,"requesting alarm toggle");
			alarmEnabled = !alarmEnabled;
			mSharedPreferences.edit().putBoolean("alarms", alarmEnabled).commit();
		}
		
		AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		if (alarmEnabled) {
			Log.i(TAG,"programming alarm");
			alarm.setRepeating(
					AlarmManager.ELAPSED_REALTIME_WAKEUP, 
					SystemClock.elapsedRealtime()+INITIAL_DELAY, 
					REPEAT_DELAY, mAlarmOperation);
			showMessage("Alarm is enabled.");
		} else {
			Log.i(TAG,"alarm disabled, canceling");
			alarm.cancel(mAlarmOperation);
			showMessage("Alarm is disabled.");
		}
		
		
		if (itemAlarm != null) {
			if (alarmEnabled) {
				itemAlarm.setTitle(R.string.action_disable_alarm);
				itemAlarm.setIcon(android.R.drawable.ic_media_pause);
			}	else {
				itemAlarm.setTitle(R.string.action_enable_alarm);
				itemAlarm.setIcon(android.R.drawable.ic_media_play);
			}
		}
	}



}
