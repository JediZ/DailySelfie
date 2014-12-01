package com.example.dailyselfie;

import java.io.File;
import java.util.Date;

import android.os.AsyncTask;
import android.util.Log;

public class LoadFilesTask extends AsyncTask<Void,Void,Void> {

	private static final String TAG = "load files async task";
	private String storagePath;
	private String suffix;
	int width,height;
	private myArrayAdapter mAdapter;
	
	public LoadFilesTask(myArrayAdapter mAdapterS, String path){
		this.mAdapter= mAdapterS; 
		this.storagePath = path;
		this.suffix = ".jpg";
		setThumbSize(30,30);
	}
	
	public void setThumbSize(int w,int h){
		this.width= w;
		this.height = h;
	}
	public void setThumbSize(int size){
		this.width= size;
		this.height = size;
	}

	@Override
	protected Void doInBackground(Void... params) {
		File storage = new File(storagePath);
		File files[] = storage.listFiles();
		
		if(files != null){
			
			Log.d(TAG,"Files found:"+files.length);
			for(File f : files){
				if (f.getName().endsWith(suffix)){
					Date lastModDate = new Date(f.lastModified());
					Selfie sf = new Selfie(lastModDate, f.getAbsolutePath());
						mAdapter.addSelfie(sf);
				}
			}
			
		}
		return null;
	}

}
