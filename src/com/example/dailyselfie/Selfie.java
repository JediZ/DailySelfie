package com.example.dailyselfie;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Selfie {
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	private int _id;
	private String name;
	private String path;
	private String thumbPath;
	private String lastmodDate;
	

	public String ToString(){
		String str= 
				"_id@"+Integer.toString(_id)+
				"name@"+name+
				"path@"+path+
				"thumbPath@"+thumbPath+
				"lastmodDate@"+lastmodDate;
		return str;
	}
	public void FromString(String str){
		String []  part= str.split("@");
		
				_id = Integer.parseInt(part[1]);
				name =part[3];
				path = part[5];
				thumbPath = part[7];
				lastmodDate = part[9];
	}
	
	public Selfie(String lastModDate, String absolutePath) {
		// TODO Auto-generated constructor stub
		this.path =absolutePath;
		this.lastmodDate = lastModDate;
	}
	public Selfie() {}
	public Selfie(Date lastModDate, String absolutePath) {
		Format df = new SimpleDateFormat(DATE_FORMAT);
		this.path =absolutePath;
		this.lastmodDate = df.format(lastModDate);
	}
	public void setLastModDate(Date lastModDate) {
		// TODO Auto-generated method stub
		Format df = new SimpleDateFormat(DATE_FORMAT);
		this.lastmodDate = df.format(lastModDate);		
	}

	public void setLastModDate(String lastmodDate){
		this.lastmodDate = lastmodDate;
	}

	public String getLastModDate(){
		return lastmodDate;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}


	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}



}
