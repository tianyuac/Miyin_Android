package com.start.core;

import start.core.AppException;
import start.core.HandlerContext;
import start.core.HandlerContext.HandleContextListener;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.start.zmcy.R;

public class BaseFragmentActivity extends FragmentActivity implements HandleContextListener{
	
	protected final String TAG = this.getClass().getSimpleName();
	
	private HandlerContext mHandlerContext;
	
	public HandlerContext getHandlerContext() {
		if(mHandlerContext==null){
			mHandlerContext=new HandlerContext(this);
			mHandlerContext.setListener(this);
		}
		return mHandlerContext;
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		default:
			Object message=msg.obj;
			if(message!=null){
				getHandlerContext().makeTextShort(String.valueOf(message));
			}else{
				getHandlerContext().makeTextShort(getString(R.string.error_try_again));
			}
			break;
		}
	}
	
	public void setMainHeadTitle(String title){
		TextView tvTitle=(TextView)findViewById(R.id.head_title);
		if(tvTitle!=null){
			tvTitle.setText(title);
		}
	}
	
}
