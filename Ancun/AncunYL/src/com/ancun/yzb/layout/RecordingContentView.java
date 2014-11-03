package com.ancun.yzb.layout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.DownloadRunnable;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.RefreshListServer;
import start.service.RefreshListServer.RefreshListServerListener;
import start.service.Response;
import start.utils.MD5;
import start.utils.NetConnectManager;
import start.widget.xlistview.XListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;

import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.widget.PlayerView;
import com.ancun.yzb.R;
import com.ancun.yzb.RecordedDetailActivity;
import com.ancun.yzb.layout.RecordingAdapter.HolderView;

public class RecordingContentView extends BaseScrollContent implements RefreshListServerListener {

	private final String DOWNLOADDIRECTORY=Environment.getExternalStorageDirectory().getPath()+"/ancun/record/";
	
	private XListView mListView;
	private RefreshListServer mRefreshListServer;
	private RecordingAdapter mRecordingAdapter;
	private PlayerView playerView;

	public RecordingContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_recording);
		playerView=(PlayerView)findViewById(R.id.playerview);
		mListView = (XListView) findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (id >= 0) {
					int i=position-1;
					mRecordingAdapter.setSelectedPosition(i);
					HolderView v=(HolderView)view.getTag();
					if(v.file.exists()){
						playerView.initPlayerFile(v.file.getAbsolutePath());
						playerView.startPlayer();
					}else{
						final String fileno=v.fileno;
						if(NetConnectManager.isMobilenetwork(getCurrentActivity())){
							new AlertDialog.Builder(getCurrentActivity())
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage(R.string.mobiledownloadtip)
							.setPositiveButton(R.string.cancle, null)
							.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									download(fileno);
								}
							}).show();
						}else{
							download(fileno);
						}
					}
				} else {
					mRefreshListServer.getCurrentListView().startLoadMore();
				}
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				if (id >= 0) {
					int i=position-1;
					mRecordingAdapter.setSelectedPosition(i);
					HolderView v=(HolderView)view.getTag();
					final String fileno=v.fileno;
					new AlertDialog.Builder(getCurrentActivity())
					.setMessage(R.string.suredeleterecording)
					.setPositiveButton(R.string.cancle, null)
					.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							final EditText input = new EditText(getCurrentActivity());
							input.setTransformationMethod(PasswordTransformationMethod.getInstance());
							new AlertDialog.Builder(getCurrentActivity())  
			                .setMessage(R.string.deleterecordinginputpasswordtip)
			                .setView(input)  
			                .setPositiveButton(R.string.cancle,null)
			                .setNegativeButton(R.string.sure, new DialogInterface.OnClickListener(){

												@Override
												public void onClick(DialogInterface dialog,int which) {
													 final String value = input.getText().toString();
						                                if(TextUtils.isEmpty(value)){
						                                	getHandlerContext().makeTextShort(getCurrentActivity().getString(R.string.pwdemptytip));
						                                	return;
						                                }
						                                HttpServer hServer=new HttpServer(Constant.URL.v4recAlter, getCurrentActivity().getHandlerContext());
						                        		Map<String,String> headers=new HashMap<String,String>();
						                        		headers.put("sign", User.ACCESSKEY);
						                        		hServer.setHeaders(headers);
						                        		Map<String,String> params=new HashMap<String,String>();
						                        		params.put("accessid", User.ACCESSID);
						                        		params.put("fileno", fileno);
						                        		params.put("alteract", "1");
						                        		params.put("password", MD5.md5(value));
						                        		hServer.setParams(params);
						                        		hServer.get(new HttpRunnable() {
						                        			
						                        			@Override
						                        			public void run(Response response) throws AppException {
						                    					for(Map<String,String> content:mRefreshListServer.getItemDatas()){
						        									if(fileno.equals(content.get("fileno"))){
						        										mRefreshListServer.getItemDatas().remove(content);
						        										getCurrentActivity().runOnUiThread(new Runnable() {
						        											@Override
						        											public void run() {
						        												mRecordingAdapter.notifyDataSetChanged();
						        											}
						        										});
						        										break;
						        									}
						        								}
						                        			}
						                        			
						                        		});
												}
			                        }).show();
						}
					}).show();
				}
				return false;
			}
		});
		
		mRecordingAdapter=new RecordingAdapter(getCurrentActivity());
		mRefreshListServer = new RefreshListServer(getCurrentActivity(), mListView,mRecordingAdapter);
		mRefreshListServer.setCacheTag(TAG);
		mRefreshListServer.setListTag("reclist");
		mRefreshListServer.setInfoTag("recinfo");
		mRefreshListServer.setRefreshListServerListener(this);

		mRefreshListServer.initLoad();
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.v4recQry,mRefreshListServer.getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid",User.ACCESSID);
		params.put("rectype","3");
		params.put("calltype","1");
		params.put("oppno","");
		params.put("callerno","");
		params.put("calledno","");
		params.put("begintime","");
		params.put("endtime","");
		params.put("remark","");
		params.put("durmin","");
		params.put("durmax","");
		params.put("licno","");
		params.put("status","1");
		params.put("ordersort","desc");
		params.put("currentpage",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
		params.put("pagesize", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mRefreshListServer.resolve(response);
				mRefreshListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}

	public void download(String fileNo){
		HttpServer hServer=new HttpServer(Constant.URL.v4recDown, getCurrentActivity().getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
		params.put("accessid", User.ACCESSID);
		params.put("fileno", fileNo);
		hServer.setParams(params);
		hServer.download(new DownloadRunnable() {
			
			@Override
			public void run(final File file) throws AppException {
				getCurrentActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mRecordingAdapter.notifyDataSetChanged();
						playerView.initPlayerFile(file.getAbsolutePath());
						playerView.startPlayer();
					}
				});
			}
			
		},DOWNLOADDIRECTORY,fileNo);
	}
	
	public void onPause() {
		playerView.pause();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null&&mRecordingAdapter!=null){
			if(requestCode==RecordingAdapter.REMARKREQUESTCODE){
				if(resultCode==RecordedDetailActivity.REMARKRESULTCODE){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RecordingAdapter.RECORDED_FILENO);
						Integer cerflag=bundle.getInt(RecordingAdapter.RECORDED_CEFFLAG);
						Integer accstatus=bundle.getInt(RecordingAdapter.RECORDED_ACCSTATUS);
						String remark=bundle.getString(RecordingAdapter.RECORDED_REMARK);
						for(Map<String,String> content:mRefreshListServer.getItemDatas()){
							if(content.get(RecordingAdapter.RECORDED_FILENO).equals(fileno)){
								content.put(RecordingAdapter.RECORDED_FILENO, fileno);
								content.put(RecordingAdapter.RECORDED_CEFFLAG, cerflag+"");
								content.put(RecordingAdapter.RECORDED_ACCSTATUS, accstatus+"");
								content.put(RecordingAdapter.RECORDED_REMARK, remark);
								mRecordingAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				}else if(resultCode==RecordedDetailActivity.REMARKMODIFYCODE){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RecordingAdapter.RECORDED_FILENO);
						String remark=bundle.getString(RecordingAdapter.RECORDED_REMARK);
						for(Map<String,String> content:mRefreshListServer.getItemDatas()){
							if(content.get(RecordingAdapter.RECORDED_FILENO).equals(fileno)){
								content.put(RecordingAdapter.RECORDED_FILENO, fileno);
								content.put(RecordingAdapter.RECORDED_REMARK, remark);
								mRecordingAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				}
			}
		}
	}
}