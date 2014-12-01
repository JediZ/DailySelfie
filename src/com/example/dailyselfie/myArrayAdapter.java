package com.example.dailyselfie;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class myArrayAdapter extends ArrayAdapter<Selfie> {
//	private static final float FONTSIZE = 18;
	private final int resource;
	private static LayoutInflater mLayoutInflater = null;
	private Context mContext;
	private List<Selfie> mSelfies = new ArrayList<Selfie>();

	private List<String> mString = new ArrayList<String>();

	public myArrayAdapter(Context context, int resource, List<Selfie> list) {
		super(context, resource, list);

		this.mSelfies = list;
		this.mContext = context;
		this.resource = resource;

		mLayoutInflater = LayoutInflater.from(mContext);

		BitmapUtil.initStoragePath(mContext);
		
		updateStrings();
	}

	public myArrayAdapter(Context context, int resource) {
		super(context, resource);

		this.mContext = context;
		this.resource = resource;

		mLayoutInflater = LayoutInflater.from(mContext);
		BitmapUtil.initStoragePath(mContext);
	}
	public void addSelfiesFromStrings(List<String> list){
		this.mString = list;
		updateSelfies();
	}

	public void updateStrings() {
		mString.clear();
		for(Selfie sf : mSelfies){
			mString.add(sf.ToString());
		}
	}
	public void updateSelfies() {
		mSelfies.clear();
		for(String str : mString){
			Selfie sf = new Selfie();
			sf.FromString(str);
			mSelfies.add(sf);
		}
	}

	static class ViewHolder {
		public TextView text;
		public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		View rowView = convertView; 
		if (rowView == null){

			rowView = mLayoutInflater.inflate(resource, null);
			
			ViewHolder vh = new ViewHolder();
			vh.text= (TextView) rowView.findViewById(R.id.selfie_name);
			vh.image= (ImageView) rowView.findViewById(R.id.selfie_bitmap);
			rowView.setTag(vh);
		}
		
		
		ViewHolder vh = (ViewHolder)rowView.getTag();
				
		Selfie sf = mSelfies.get(position);
		
			vh.image.setImageBitmap(BitmapUtil.getBitmapFromFile(sf.getThumbPath()));
			
//			vh.text.setTextSize(FONTSIZE);
//			vh.text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
			vh.text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			vh.text.setText(sf.getLastModDate());
		
		return rowView;
	}

	public void addSelfie(Selfie sf) {
		mSelfies.add(sf);
	}
	public void deleteallSelfie(){
		mSelfies.clear();
		notifyDataSetChanged();
	}

}
