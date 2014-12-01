package com.example.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;


public class EnlargeActivity extends Activity {
	private static final String TAG = "EnlargedActivity";
	private ImageView mBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		Log.i(TAG,"Entering enlarged activity");
		
		setContentView(R.layout.enlarge_activity);		
		Log.i(TAG,"content view set");
		
		mBitmap = (ImageView)findViewById(R.id.enlarged_view);
		Log.i(TAG,"found bitmap");
		
		String selfieName = getIntent().getStringExtra("name");
		String filePath = getIntent().getStringExtra("path");
		
		Log.i(TAG,"got name and path");
		
		setTitle(selfieName);
		
		Log.i(TAG,"title set");
		new LoadBitmapTask(this,mBitmap).execute(filePath);
		Log.i(TAG,"starting loading task");
		
		// set listener on imageview
		mBitmap.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				finish(); // close if click on the image
			}
		});
//		setProgressBarIndeterminateVisibility(true);
	}
	
	
	static class LoadBitmapTask extends AsyncTask<String, String, Bitmap> {

		private ImageView mImageView;
		private Activity mActivity;
		
		public LoadBitmapTask(Activity activity,ImageView imageView) {
			mImageView = imageView;
			mActivity = activity;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			String selfiePath = params[0];
			
			return BitmapUtil.getBitmapFromFile(selfiePath);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			mImageView.setImageBitmap(result);
//			mActivity.setProgressBarIndeterminateVisibility(false);
			super.onPostExecute(result);
		}
	}

}
