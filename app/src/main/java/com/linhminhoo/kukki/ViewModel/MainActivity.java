package com.linhminhoo.kukki.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linhminhoo.kukki.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.InflaterInputStream;

public class MainActivity extends BaseActivity {
	
	private Fragment mContent;
    TextView tv_hello;

	public MainActivity(){
		super(R.string.app_name);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

        //Check Fragment
		if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
		if (mContent == null) {
            mContent = MainView.newInstance(this);
        }


        //Init Fragment
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent).commit();

        //Init Slidemenu
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
                .replace(R.id.menu_frame, new Slidemenu()).commit();
	//	.replace(R.id.menu_frame, new RandomList()).commit();
		
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setSlidingActionBarEnabled(true);
        //////////////
        // Init Directory Kukki
        if(isAvailableExternalStorage()){
            String folder_main = "Kukki";
            File f = new File(Environment.getExternalStorageDirectory(),
                    folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
        }else{
            Log.e("Detail", "Noo");
        }
        /////////////


	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}


    public void switchContent(Fragment fragment){
		mContent = fragment;
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}
    public void showSlidingMneu(Activity activity){
         getSlidingMenu().showMenu();
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("Thông báo !")
                .setMessage("Bạn có muốn Thoát ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                      //  uDAO.updateUser(1,"No name","");
                      //  ulDAO.isCLearProfile();
                        Intent myItent=new Intent(MainActivity.this, LoginActivity.class);
                        myItent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myItent);
                        finish();
                    }})
                .setNegativeButton("No", null).show();
    }
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
    public boolean isAvailableExternalStorage(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
        }
        return mExternalStorageAvailable;
    }
}
