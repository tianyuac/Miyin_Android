package com.start.zmcy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.service.bean.NewsCategory;
import com.start.widget.ColumnHorizontalScrollView;
import com.start.zmcy.adapter.ContentFragmentPagerAdapter;
import com.start.zmcy.content.NewsContentFragment;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {

	public static final int REQUEST_LOGIN_CODE=111;
	public static final int CHANNELRESULT=123;
	
	private static List<NewsCategory> mNewsCategoryes=new ArrayList<NewsCategory>();
	private List<BaseFragment> nBaseFragments = new ArrayList<BaseFragment>();

	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	private LinearLayout ll_more_columns;
	private RelativeLayout rl_column;
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	public ImageView button_more_columns;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	
	private ViewPager mViewPager;
	private ScrollView mMainMenu;
	private TranslateAnimation mShowAction, mHiddenAction;

	static{
		NewsCategory nc=new NewsCategory();
		nc.setKey("1");
		nc.setTitle("头条");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("2");
		nc.setTitle("资讯");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("3");
		nc.setTitle("会讯");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("4");
		nc.setTitle("政策法规");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("5");
		nc.setTitle("标准检测");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("6");
		nc.setTitle("国内展");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("7");
		nc.setTitle("国外展");
		mNewsCategoryes.add(nc);
		nc=new NewsCategory();
		nc.setKey("8");
		nc.setTitle("工程招标");
		mNewsCategoryes.add(nc);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMainHeadTitle(getString(R.string.app_name));

		mMainMenu = (ScrollView) findViewById(R.id.main_menu);
		mScreenWidth = getWindowsWidth(this);
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		button_more_columns.setOnClickListener(this);
		
		for(int i=0;i<mNewsCategoryes.size();i++){
			nBaseFragments.add(new NewsContentFragment(this, mNewsCategoryes.get(i)));
		}
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mViewPager.setAdapter(new ContentFragmentPagerAdapter(getSupportFragmentManager(), nBaseFragments));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mViewPager.setCurrentItem(position);
				selectTab(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		//显示动画从左向右滑
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);

		//隐藏动画从右向左滑
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f, 
				Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 
				0.0f, Animation.RELATIVE_TO_SELF,0.0f);
		mHiddenAction.setDuration(500);
		
		initTabColumn();
		
		PushManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.head_sliding) {
			if (mMainMenu.isShown()) {
				mMainMenu.startAnimation(mHiddenAction);   
				mMainMenu.setVisibility(View.GONE);
			} else {
				mMainMenu.startAnimation(mShowAction);   
				mMainMenu.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.head_login) {
			// 登录
			if(getAppContext().currentUser().isLogin()){
				startActivity(new Intent(this, MemberActivity.class));
			}else{
				goLoginResult(REQUEST_LOGIN_CODE,getString(R.string.nologin));
			}
		} else if (v.getId() == R.id.txtResources) {
			// 资源
			startActivity(new Intent(this, ResourceActivity.class));
		} else if (v.getId() == R.id.txtActivities) {
			// 活动
			startActivity(new Intent(this, ActivitiesActivity.class));
		} else if (v.getId() == R.id.txtExperts) {
			// 专家
			startActivity(new Intent(this, ExpertsActivity.class));
		} else if (v.getId() == R.id.txtApp) {
			// 应用
			startActivity(new Intent(this, AppActivity.class));
		} else if (v.getId() == R.id.txtMember) {
			// 会员
			startActivity(new Intent(this, MemberActivity.class));
		}else if(v.getId()==R.id.button_more_columns){
			startActivity(new Intent(this, ChannelActivity.class));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_LOGIN_CODE){
			if(resultCode==LoginActivity.RESULT_LOGIN_SUCCESS){
				
			}
		}
	}

	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  mNewsCategoryes.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(mNewsCategoryes.get(i).getTitle());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				        	  mViewPager.setCurrentItem(i);
				          }
			          }
				}
			});
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}
	
	/** 获取屏幕的宽度 */
	public  int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	
}