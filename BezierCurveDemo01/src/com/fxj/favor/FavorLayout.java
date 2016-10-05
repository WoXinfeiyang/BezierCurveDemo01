package com.fxj.favor;

import java.util.Random;

import com.fxj.beziercurvedemo.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FavorLayout extends RelativeLayout {

	/*����ʱ���ֵ��,ʱ���ֵ�������Ǹ���ʱ�����ŵİٷֱ������㵱ǰ����ֵ�ı�İٷֱ�*/
	/**���Բ�ֵ��*/
	private Interpolator line=new LinearInterpolator();
	/**���ٲ�ֵ��*/
	private Interpolator acc=new AccelerateInterpolator();
	/**���ٲ�ֵ��*/
	private Interpolator dec=new DecelerateInterpolator();
	/**�ȼ��ٺ���ٲ�ֵ��*/
	private Interpolator accdec=new AccelerateDecelerateInterpolator();
	/**��ֵ������*/
	private Interpolator[] interpolator;
	
	/**�Զ��岼�ֵĿ�*/
	private int mWidth;
	/**�Զ��岼�ֵĸ�*/
	private int mHeight;
	
	/**����Drawable��������*/
	private Drawable[] mDrawables;
	/**����Drawable����Ĳ��ֲ���*/
	private LayoutParams lp;
	/**����Drawable����Ŀ�*/
	private int dWidth;
	/**����Drawable����ĸ�*/
	private int dHeight;
	
	private Random random=new Random();
	
	public FavorLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FavorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FavorLayout(Context context) {
		super(context);
		init();
	}

	private void init(){
		this.mDrawables=new Drawable[3];
		Drawable red=getResources().getDrawable(R.drawable.pl_red);
		Drawable yellow=getResources().getDrawable(R.drawable.pl_yellow);
		Drawable blue=getResources().getDrawable(R.drawable.pl_blue);
		this.mDrawables[0]=red;
		this.mDrawables[1]=yellow;
		this.mDrawables[2]=blue;
		
		/*��ȡ����Drawable�Ŀ��,ע������3��ͼƬһ����С��ֻ��ȡһ������*/
		this.dWidth=red.getIntrinsicWidth();
		this.dHeight=red.getIntrinsicHeight();
		
		/*���������ð���Drawable���󲼾ֲ�������*/
		this.lp=new LayoutParams(dWidth, dHeight);
		this.lp.addRule(CENTER_HORIZONTAL,TRUE);
		this.lp.addRule(ALIGN_PARENT_BOTTOM,TRUE);
		
		/*��ʼ����ֵ��*/
		this.interpolator=new Interpolator[4];
		this.interpolator[0]=line;
		this.interpolator[1]=acc;
		this.interpolator[2]=dec;
		this.interpolator[3]=accdec;
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthSize=MeasureSpec.getSize(widthMeasureSpec);
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		int heightSize=MeasureSpec.getSize(heightMeasureSpec);
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);
		
		if(widthMode==MeasureSpec.EXACTLY){
			this.mWidth=widthSize;
		}else{
			this.mWidth=200;
		}
		if(heightMode==MeasureSpec.EXACTLY){
			this.mHeight=heightSize;
		}else{
			this.mHeight=200;
		}
		setMeasuredDimension(this.mWidth,this.mHeight);
	}
	
	public void addHeart(){
		ImageView imageView=new ImageView(getContext());
		/*�������ͼƬ*/
		imageView.setImageDrawable(this.mDrawables[random.nextInt(mDrawables.length)]);
		imageView.setLayoutParams(lp);
		
		addView(imageView);
		
		Animator set=getAnimator(imageView);
		set.addListener(new AnimEndListener(imageView));
		set.start();
	}

	private Animator getAnimator(View targetView) {
		
		AnimatorSet enterSet=getEnterAnimator(targetView);
		ValueAnimator bezierAnimator=getBezierValueAnimator(targetView);
		
		AnimatorSet set=new AnimatorSet();
		set.playSequentially(enterSet);
		set.playSequentially(enterSet,bezierAnimator);
		set.setInterpolator(interpolator[random.nextInt(interpolator.length)]);
		set.setTarget(targetView);
		return set;
	}
	
	/**��ȡĿ��View�ؼ����볡����*/
	private AnimatorSet getEnterAnimator(View targetView){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(targetView, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 0.2f, 1f);
		
        /**�����������϶���*/
        AnimatorSet enter=new AnimatorSet();
        enter.setDuration(5000);/*���ö�������ִ�е�ʱ��*/
        enter.setInterpolator(new LinearInterpolator());/*���ò�ֵ��*/
        enter.playTogether(alpha,scaleX,scaleY);
        enter.setTarget(targetView);
        
		return enter;
	}
	
	private ValueAnimator getBezierValueAnimator(View targetView){
		
		/*����һ����������ֵ������*/
		BezierEvaluator evaluator=new BezierEvaluator(getPointF(2),getPointF(1));
		
		ValueAnimator animator=ValueAnimator.ofObject(evaluator,new PointF((mWidth-dWidth)/2,mHeight-dHeight),new PointF(random.nextInt(getWidth()), 0));
		
		animator.addUpdateListener(new BezierListener(targetView));
		
		animator.setTarget(targetView);
		animator.setDuration(3000);
		
		return animator;
	}
	
	private PointF getPointF(int scale){
		PointF pointF=new PointF();
		
		pointF.x=random.nextInt(this.mWidth-100);
		pointF.y=random.nextInt(this.mHeight-100)/scale;
		
		return pointF;
	}
	
	private class BezierListener implements ValueAnimator.AnimatorUpdateListener {
		private View targetView;	
		
		public BezierListener(View targetView) {
			this.targetView = targetView;
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			/*��ȡ�����������е�ǰʱ�̵ĵ�*/
			PointF pointF=(PointF) animation.getAnimatedValue();
			/*��Ŀ��View�ؼ��ƶ�����ǰ�����������ϵĵ㣬������ȷ��Ŀ��View�ؼ����ű����������ƶ�*/
			this.targetView.setX(pointF.x);
			this.targetView.setY(pointF.y);
			
			this.targetView.setAlpha(1-animation.getAnimatedFraction());			
		}
		
	}
	
	private class AnimEndListener extends AnimatorListenerAdapter{
		private View targetView;

		public AnimEndListener(View targetView) {
			this.targetView = targetView;
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			removeView(targetView);
		}		
	}
	
}
