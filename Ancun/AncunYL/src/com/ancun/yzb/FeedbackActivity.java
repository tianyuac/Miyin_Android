package com.ancun.yzb;

import android.os.Bundle;

import com.ancun.core.BaseActivity;

public class FeedbackActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setMainHeadTitle(getString(R.string.feedback));
	}
}
