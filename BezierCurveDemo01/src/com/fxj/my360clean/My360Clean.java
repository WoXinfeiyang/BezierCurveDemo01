package com.fxj.my360clean;

import com.fxj.beziercurvedemo.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class My360Clean extends View {
	private static final String tag="My360Clean";
	
	/**���ڵĿ��*/
	private int mWindowWidth;
	/**���ڵĸ߶�*/
	private int mWindowHeight;
	
	/**�Զ���View�Ŀ��*/
	private int mWidth;
	/**�Զ���View�ĸ߶�*/
	private int mHeight;
	
	
	/**�ߵ�Y����*/
	private int mLineY;
	
	/**��������ʼ��*/
	private Point startPoint;
	/**�������յ�*/
	private Point endPoint;
	/**������,xΪ�����ߵ��յ�*/
	private Point subPoint;

    /**����X����*/
    private int mBitmapX;
    /**����Y����*/
    private int mBitmapY;
    
    /**���еİٷֱ�(0~100)*/
    private int mFlyPercent = 100;
    
	/**�϶���־λ*/
	private boolean isDragging=false;
	
	public My360Clean(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public My360Clean(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public My360Clean(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display ds=wm.getDefaultDisplay();
		this.mWindowWidth=ds.getWidth();
		this.mWindowHeight=ds.getHeight();
		this.mLineY=this.mWindowHeight*57/100;
		
		/*��������ʼ����������ʼ��*/
		this.startPoint=new Point();
		this.startPoint.x=(int) (this.mWindowWidth*0.2);
		this.startPoint.y=this.mLineY;
		/*��������ʼ���������յ�*/
		this.endPoint=new Point();
		this.endPoint.x=(int) (this.mWindowWidth*0.8);
		this.endPoint.y=this.mLineY;
		
		/*��������ʼ��������,��ʼ��ʱ������x����λ�ڵ������е�*/
		this.subPoint=new Point();
		this.subPoint.x=(this.startPoint.x+this.endPoint.x)/2;
		this.subPoint.y=this.mLineY;
		
		this.isDragging=false;/*���϶���־λ��Ϊfalse*/
		
		Log.i(tag,"this.mWindowWidth="+this.mWindowWidth+
				",this.mWindowHeight="+this.mWindowHeight+
				",this.mLineY="+this.mLineY+
				",this.startPoint"+this.startPoint.toString()+
				",this.endPoint="+this.endPoint.toString()+
				",this.subPoint="+this.subPoint.toString());		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {		
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
		
		setMeasuredDimension(mWidth, mHeight);
		Log.i(tag,"this.mWidth="+this.mWidth+",this.mHeight="+this.mHeight);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		/**����һ֧���ʶ���*/
		Paint paint=new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(20);/*���û����������*/
		

		/**��������Bitmap����*/
		Bitmap background=BitmapFactory.decodeResource(getResources(),R.drawable.t);
		
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inJustDecodeBounds=true;
		opt.inSampleSize=background.getWidth()/(endPoint.x-startPoint.x)+1;
		opt.inJustDecodeBounds=false;
		
		Bitmap resizedBackground=BitmapFactory.decodeResource(getResources(),R.drawable.t,opt);
		/*������ͼƬ���Զ���View��*/
		canvas.drawBitmap(resizedBackground,this.startPoint.x+(this.endPoint.x-this.startPoint.x-resizedBackground.getWidth())/2,this.startPoint.y-resizedBackground.getHeight(), paint);
		
		/**��������Bitmap����*/
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.mb);
		
		Path path=new Path();
		
		if(isDragging){
			/*���Ƶ�����*/
			paint.setColor(Color.YELLOW);
			path.moveTo(this.startPoint.x, this.startPoint.y);
			path.quadTo(this.subPoint.x,this.mLineY+(this.subPoint.y-this.mLineY)*this.mFlyPercent/100,this.endPoint.x,this.endPoint.y);
			canvas.drawPath(path, paint);
			
			if(this.mFlyPercent>0){
				canvas.drawBitmap(bitmap,this.mBitmapX,this.mBitmapY*this.mFlyPercent, paint);
				this.mFlyPercent-=5;
				postInvalidateDelayed(10);
			}else{
				this.mFlyPercent=100;
				this.isDragging=false;
//				this.subPoint.y=this.mLineY;
//				invalidate();
			}
			
		}else{
			/*���Ƶ�����*/
			paint.setColor(Color.YELLOW);
			path.moveTo(this.startPoint.x, this.startPoint.y);
			path.quadTo(this.subPoint.x,this.subPoint.y,this.endPoint.x,this.endPoint.y);
			canvas.drawPath(path, paint);
			
			/*���ƾ���*/
			this.mBitmapX=this.subPoint.x-bitmap.getWidth()/2;
			this.mBitmapY=(this.subPoint.y-this.mLineY)/2+this.mLineY-bitmap.getHeight();
			canvas.drawBitmap(bitmap, mBitmapX, mBitmapY, paint);
		}
		
		/*���Ƶ����������˵�*/
		paint.setColor(Color.GRAY);/*��������ɫ����Ϊ��ɫ*/
		canvas.drawCircle(this.startPoint.x,this.startPoint.y,10, paint);
		canvas.drawCircle(this.endPoint.x,this.endPoint.y,10, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			if(event.getY()>this.mLineY){
				this.subPoint.y=(int) event.getY();
			}
			invalidate();
			
			break;
		case MotionEvent.ACTION_UP:
			this.isDragging=true;
			invalidate();
			
			break;
		}
		
		return true;
	}
		
}
