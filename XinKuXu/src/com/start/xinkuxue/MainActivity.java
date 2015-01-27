package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.xinkuxue.strange.StrangeWordTypeSwitchActivity;
import com.start.xinkuxue.vocabulary.VocabularyTypeSwitchActivity;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.learnwords){
			Intent intent=new Intent(this,LearnWordSwitchActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.testvocabulary){
			Intent intent=new Intent(this,VocabularyTypeSwitchActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.newwords){
			Intent intent=new Intent(this,StrangeWordTypeSwitchActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.specialtraining){
			getHandlerContext().makeTextShort("特训营,即将上线");
		}
	}
	
}
