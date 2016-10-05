package com.fxj.favor;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/*估值器的作用是根据当前属性改变的百分比来计算改变后的属性值*/
/**贝塞尔估值器*/
public class BezierEvaluator implements TypeEvaluator<PointF>{
	
	private PointF pointF1;
	private PointF pointF2;
	
	public BezierEvaluator(PointF pointF1, PointF pointF2) {
		this.pointF1 = pointF1;
		this.pointF2 = pointF2;
	}


	/*根据贝塞尔三阶曲线重写该方法*/
	@Override
	public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
		PointF pointF=new PointF();
		float fractionLeft=1.0f-fraction;
		
		pointF.x=fractionLeft*fractionLeft*fractionLeft*startValue.x+
				3*fraction*fractionLeft*fractionLeft*pointF1.x+
				3*fraction*fraction*fractionLeft*pointF2.x+
				fraction*fraction*fraction*endValue.x;

		pointF.y=fractionLeft*fractionLeft*fractionLeft*startValue.y+
				3*fraction*fractionLeft*fractionLeft*pointF1.y+
				3*fraction*fraction*fractionLeft*pointF2.y+
				fraction*fraction*fraction*endValue.y;
		
		return pointF;
	}

}
