package com.fxj.beziercurvedemo;

import com.fxj.favor.FavorActivity;
import com.fxj.my360clean.My360CleanActivity;
import com.fxj.shoppingcart.ShoppingCartActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	public static final String tag="MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onButtonClick(View v){
		switch(v.getId()){
			case R.id.btn_ShoppingCart:
				Intent intentShoppingCart=new Intent(this,ShoppingCartActivity.class);
				startActivity(intentShoppingCart);
				break;
			case R.id.btn_My360Clean:
				Intent intentMy360Clean=new Intent(this,My360CleanActivity.class);
				startActivity(intentMy360Clean);
				break;
			case R.id.btn_Favor:
				Intent intentFavor=new Intent(this,FavorActivity.class);
				startActivity(intentFavor);
				break;
			default :
				break;
		}
	}

}
