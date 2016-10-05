package com.fxj.favor;

import com.fxj.beziercurvedemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FavorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favor_layout);
		
		final FavorLayout favor=(FavorLayout) findViewById(R.id.favor);
		
		Button btnFavor=(Button) findViewById(R.id.btn_Favor);
		
		favor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				favor.addHeart();
			}
		});
		
		btnFavor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				favor.addHeart();
			}
		});
		
	}
		
}
