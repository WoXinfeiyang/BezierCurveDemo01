package com.fxj.shoppingcart;

import java.util.ArrayList;

import com.fxj.beziercurvedemo.R;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoppingCartActivity extends Activity {

	private static final String tag="ShoppingCartActivity";
	
	/**购物车父布局RelativeLayout*/
	private RelativeLayout mShoppingCartRly;
	
	/**购物商品列表ListView对象*/
	private ListView mShoppingCartLv;
	
	/**购物数目TextView文本显示对象*/
	private TextView mShoppingCartCountTv;
	/**购物车ImageView对象*/
	private ImageView mShoppingCartIv;
	
	/**购物车商品数量*/
	private int goodsCount=0;
	/**商品数据集合*/
	private ArrayList<GoodsModel> mDatas;
	
	private GoodsAdapter adapter;
	
	/**路径测量对象*/
	private PathMeasure mPathMeasure;
	/**贝赛尔曲线中间过程点坐标*/
	private float[] mCurrentPosition=new float[2];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppingcart_layout);
		
		this.mShoppingCartRly=(RelativeLayout) findViewById(R.id.rly_shopping_cart);
		this.mShoppingCartLv=(ListView) findViewById(R.id.lv_shopping_cart);
		this.mShoppingCartCountTv=(TextView) findViewById(R.id.tv_shopping_cart_count);
		this.mShoppingCartIv=(ImageView) findViewById(R.id.iv_shopping_cart);
		
		isShowCartGoodsCount();
		addData();
		setAdapter();
		
	}
	
	/**是否显示购物车中已购商品数量*/
	private void isShowCartGoodsCount(){
		if(this.mDatas==null||this.mDatas.size()==0||this.goodsCount==0){
			this.mShoppingCartCountTv.setVisibility(View.GONE);
		}else{
			this.mShoppingCartCountTv.setVisibility(View.VISIBLE);
		}
	}
	
	private void addData(){
		this.mDatas=new ArrayList<GoodsModel>();
		
		GoodsModel goods=new GoodsModel();
		goods.setGoodsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goods_one));
		mDatas.add(goods);
		
		goods=new GoodsModel();
		goods.setGoodsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goods_two));
		mDatas.add(goods);	
		
		goods=new GoodsModel();
		goods.setGoodsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goods_three));
		mDatas.add(goods);
	}
	
	private void setAdapter(){
		this.adapter=new GoodsAdapter(this,this.mDatas);
		
		this.adapter.setCallBackListener(new GoodsAdapter.CallBackListener() {			
			
			@Override
			public void callBackImg(ImageView goodsImg) {
				// TODO Auto-generated method stub
				Log.i(tag,"ListView被点击了!");
				addGoodsToCart(goodsImg);
			}
		});
		
		this.mShoppingCartLv.setAdapter(adapter);
	}
	
	/**将商品加入到购物车
	 * @param goodsImg--ListView列表条目中ImageView对象
	 * */
	private void addGoodsToCart(ImageView goodsImg) {
		// TODO Auto-generated method stub
		
		/*创建执行动画的ImageView对象*/
		final ImageView goods=new ImageView(this);
		goods.setImageDrawable(goodsImg.getDrawable());
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(100,100);
		/*将执行动画的ImageView对象添加到RelativeLayout相对布局中*/
		this.mShoppingCartRly.addView(goods,params);
		
		/**父布局起始点坐标*/
		int[]parentLocation=new int[2];
		/*得到父布局起始点坐标*/
		this.mShoppingCartRly.getLocationInWindow(parentLocation);
		
		/**商品图片的坐标*/
		int[]startLoc=new int[2];
		/*得到商品图片坐标，用于计算动画开始的坐标*/
		goodsImg.getLocationInWindow(startLoc);
		
		/**购物车图片坐标*/
		int[]endLoc=new int[2];
		this.mShoppingCartIv.getLocationInWindow(endLoc);
		
		/*动画开始的起始点坐标:商品起始点-父布局起始点+该商品图片的一半*/
		float startX=startLoc[0]-parentLocation[0]+goodsImg.getWidth()/2;
		float startY=startLoc[1]-parentLocation[1]+goodsImg.getHeight()/2;
		
		/*动画结束的终点坐标:购物车起始点-父布局起始点+购物车图片的1/5*/
		float endX=endLoc[0]-parentLocation[0]+this.mShoppingCartIv.getWidth()/5;
		float endY=endLoc[1]-parentLocation[1];
		
		/*开始绘制贝塞尔曲线*/
		Path path=new Path();
		/*移动到曲线的起始点*/
		path.moveTo(startX, startY);
		/*绘制二阶贝塞尔曲线*/
		path.quadTo((startX+endX)/2,startY,endX,endY);
		
		/*创建路径测量对象*/
		this.mPathMeasure=new PathMeasure(path, false);
		
		/*创建一个值动画来完成将商品添加到购物车的过程,即通过不断修改ImageView对象goods的属性来完成动画*/
		final ValueAnimator valueAnimator=ValueAnimator.ofFloat(this.mPathMeasure.getLength());
		valueAnimator.setDuration(500);
		/*使用线性插值器*/
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.start();/*启动动画*/
		
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float value=(Float) valueAnimator.getAnimatedValue();
				mPathMeasure.getPosTan(value, mCurrentPosition, null);
				goods.setTranslationX(mCurrentPosition[0]);
				goods.setTranslationY(mCurrentPosition[1]);
			}
		});
		
		valueAnimator.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				goodsCount++;
				isShowCartGoodsCount();
				mShoppingCartCountTv.setText(String.valueOf(goodsCount));
				/*把执行动画的图片从父布局中移除*/
				mShoppingCartRly.removeView(goods);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	
	}
}
