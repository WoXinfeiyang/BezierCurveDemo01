package com.fxj.shoppingcart;

import android.graphics.Bitmap;

/**商品实体类*/
public class GoodsModel {
	Bitmap mGoodsBitmap;
	
	public Bitmap getGoodsBitmap(){
		return this.mGoodsBitmap;
	}
	
	public void setGoodsBitmap(Bitmap mGoodsBitmap){
		this.mGoodsBitmap=mGoodsBitmap;
	}
}
