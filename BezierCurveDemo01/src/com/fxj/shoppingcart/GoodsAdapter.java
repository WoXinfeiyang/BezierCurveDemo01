package com.fxj.shoppingcart;

import java.util.ArrayList;

import com.fxj.beziercurvedemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsAdapter extends BaseAdapter {
	private static final String tag="GoodsAdapter";
	/**用于填充Adapter的数据*/
	private ArrayList<GoodsModel> mDatas;
	/**布局加载器对象*/
	private LayoutInflater mLayoutInflater;
	/**回调接口对象*/
	private CallBackListener mCallBackListener;
	
	public GoodsAdapter(Context context,ArrayList<GoodsModel> mDatas) {
		this.mDatas=mDatas;
		this.mLayoutInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mDatas!=null?this.mDatas.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas!=null?mDatas.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=this.mLayoutInflater.inflate(R.layout.adapter_shopping_cart_item,null);
			viewHolder=new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.mShoppingCartItemIV.setImageBitmap(this.mDatas.get(position).getGoodsBitmap());
		
//		if(position<this.mDatas.size()){
//			viewHolder.updateUI(this.mDatas.get(position));
//		}
				
		return convertView;
	}
	
	/**设置回调接口监听器*/
	public void setCallBackListener(CallBackListener mCallBackListener){
		this.mCallBackListener=mCallBackListener;
	} 
	
	class ViewHolder{
		private ImageView mShoppingCartItemIV;
		private TextView mShoppingCartItemTV;
		
		public ViewHolder(View view) {
			this.mShoppingCartItemIV=(ImageView) view.findViewById(R.id.iv_shopping_cart_item);
			this.mShoppingCartItemTV=(TextView) view.findViewById(R.id.tv_shopping_cart_item);
			
			
			
			this.mShoppingCartItemTV.setOnClickListener(new View.OnClickListener() {		
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mShoppingCartItemIV!=null&&mCallBackListener!=null){
						/*传入ListView条目中的ImageView对象*/
						mCallBackListener.callBackImg(mShoppingCartItemIV);
					}
				}
			});
		}
		
		/**更新UI*/
		public void updateUI(GoodsModel goods){
			if(goods!=null&&goods.getGoodsBitmap()!=null&&this.mShoppingCartItemIV!=null){
				this.mShoppingCartItemIV.setImageBitmap(goods.getGoodsBitmap());
			}
		}
		
	}

	public interface CallBackListener{
		/**回调处理函数,传入参数为ListView条目中ImageView对象*/
		void callBackImg(ImageView goodsImg);
	}
}
