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
	
	/**���ﳵ������RelativeLayout*/
	private RelativeLayout mShoppingCartRly;
	
	/**������Ʒ�б�ListView����*/
	private ListView mShoppingCartLv;
	
	/**������ĿTextView�ı���ʾ����*/
	private TextView mShoppingCartCountTv;
	/**���ﳵImageView����*/
	private ImageView mShoppingCartIv;
	
	/**���ﳵ��Ʒ����*/
	private int goodsCount=0;
	/**��Ʒ���ݼ���*/
	private ArrayList<GoodsModel> mDatas;
	
	private GoodsAdapter adapter;
	
	/**·����������*/
	private PathMeasure mPathMeasure;
	/**�����������м���̵�����*/
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
	
	/**�Ƿ���ʾ���ﳵ���ѹ���Ʒ����*/
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
				Log.i(tag,"ListView�������!");
				addGoodsToCart(goodsImg);
			}
		});
		
		this.mShoppingCartLv.setAdapter(adapter);
	}
	
	/**����Ʒ���뵽���ﳵ
	 * @param goodsImg--ListView�б���Ŀ��ImageView����
	 * */
	private void addGoodsToCart(ImageView goodsImg) {
		// TODO Auto-generated method stub
		
		/*����ִ�ж�����ImageView����*/
		final ImageView goods=new ImageView(this);
		goods.setImageDrawable(goodsImg.getDrawable());
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(100,100);
		/*��ִ�ж�����ImageView������ӵ�RelativeLayout��Բ�����*/
		this.mShoppingCartRly.addView(goods,params);
		
		/**��������ʼ������*/
		int[]parentLocation=new int[2];
		/*�õ���������ʼ������*/
		this.mShoppingCartRly.getLocationInWindow(parentLocation);
		
		/**��ƷͼƬ������*/
		int[]startLoc=new int[2];
		/*�õ���ƷͼƬ���꣬���ڼ��㶯����ʼ������*/
		goodsImg.getLocationInWindow(startLoc);
		
		/**���ﳵͼƬ����*/
		int[]endLoc=new int[2];
		this.mShoppingCartIv.getLocationInWindow(endLoc);
		
		/*������ʼ����ʼ������:��Ʒ��ʼ��-��������ʼ��+����ƷͼƬ��һ��*/
		float startX=startLoc[0]-parentLocation[0]+goodsImg.getWidth()/2;
		float startY=startLoc[1]-parentLocation[1]+goodsImg.getHeight()/2;
		
		/*�����������յ�����:���ﳵ��ʼ��-��������ʼ��+���ﳵͼƬ��1/5*/
		float endX=endLoc[0]-parentLocation[0]+this.mShoppingCartIv.getWidth()/5;
		float endY=endLoc[1]-parentLocation[1];
		
		/*��ʼ���Ʊ���������*/
		Path path=new Path();
		/*�ƶ������ߵ���ʼ��*/
		path.moveTo(startX, startY);
		/*���ƶ��ױ���������*/
		path.quadTo((startX+endX)/2,startY,endX,endY);
		
		/*����·����������*/
		this.mPathMeasure=new PathMeasure(path, false);
		
		/*����һ��ֵ��������ɽ���Ʒ��ӵ����ﳵ�Ĺ���,��ͨ�������޸�ImageView����goods����������ɶ���*/
		final ValueAnimator valueAnimator=ValueAnimator.ofFloat(this.mPathMeasure.getLength());
		valueAnimator.setDuration(500);
		/*ʹ�����Բ�ֵ��*/
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.start();/*��������*/
		
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
				/*��ִ�ж�����ͼƬ�Ӹ��������Ƴ�*/
				mShoppingCartRly.removeView(goods);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	
	}
}
