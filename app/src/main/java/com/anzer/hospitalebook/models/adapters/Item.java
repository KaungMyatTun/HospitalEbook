package com.anzer.hospitalebook.models.adapters;

import android.graphics.Bitmap;

public class Item {
	 Bitmap image;
	 String caption;
	
	public  Item(Bitmap image, String caption) {
		super();
		this.image = image;
		this.caption = caption;
	}
	public Bitmap getImage() {
		return image;
	}
	public  void setImage(Bitmap image) {
		this.image = image;
	}
	public String getCaption() {
		return caption;
	}
	public  void setCaption(String caption) {
		this.caption = caption;
	}
}
