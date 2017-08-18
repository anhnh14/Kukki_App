package com.linhminhoo.kukki.ViewModel;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.linhminhoo.kukki.FunctionCommon.PacketItem;
import com.linhminhoo.kukki.R;



public class BaseActivity extends SlidingFragmentActivity {
	
	private int mTitleRes;
	private Fragment mFrag;

    public BaseActivity() {
    }

    public BaseActivity(int titleRes){
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(mTitleRes);
		
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		//mFrag = new RandomList();
        mFrag=new Slidemenu();
		ft.replace(R.id.menu_frame, mFrag);
		ft.commit();
		
		final SlidingMenu sm = getSlidingMenu();
	//	sm.setShadowWidth(15);
	//	sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(60);
		sm.setFadeDegree(0.35f);
        sm.setBehindWidth((new PacketItem().ReturnScreenWidth(this)));
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer(){
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@SuppressLint("NewApi")
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// TODO Auto-generated method stub
				boolean layer = percentOpen > 0.0f && percentOpen < 1.0f;
				int layerType = layer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
				
				if (layerType != sm.getContent().getLayerType()){
					sm.getContent().setLayerType(layerType, null);
					sm.getMenu().setLayerType(layerType, null);
				}
			}
		});



		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setIcon(R.drawable.ic_launcher);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			toggle();
			return true;
		}
		return onOptionsItemSelected(item);
	}
/*
	
	public class BasePagerAdapter extends FragmentPagerAdapter{
		private List<Fragment> mFragments = new ArrayList<Fragment>();
		private ViewPager mPager;
		
		public BasePagerAdapter(FragmentManager fm, ViewPager vp){
			super(fm);
			mPager = vp;
			mPager.setAdapter(this);
			for (int i = 0; i < 3; i++){
				addTab(new RandomList());
			}
		}
		
		public void addTab(Fragment frag){
			mFragments.add(frag);
		}
		
		@Override
		public Fragment getItem(int position){
			return mFragments.get(position);
		}
		
		@Override
		public int getCount(){
			return mFragments.size();
		}
	}
	*/

}
