package com.linhminhoo.kukki.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.linhminhoo.kukki.R;


public class MainView extends Fragment {

    BaseActivity parent;
    ImageView img_home_icon, img_home_add;
    Button btn_all, btn_care;

    boolean isButtonAll=true;

    // tao constructor
    public static MainView newInstance(BaseActivity activity) {
        MainView mainView = new MainView();
        mainView.parent = activity;
        return mainView;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.activity_main, container, false);
        img_home_icon= (ImageView) rootView.findViewById(R.id.img_home_icon);
        img_home_add= (ImageView) rootView.findViewById(R.id.img_home_add);
        img_home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.getSlidingMenu().showMenu();
            }
        });

        img_home_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myItent=new Intent(getActivity(), PostActivity.class);
                startActivity(myItent);
            }
        });

        btn_all= (Button) rootView.findViewById(R.id.home_btn_all);
        btn_care= (Button) rootView.findViewById(R.id.home_btn_care);

        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        Home_fragment_all fragmentAll=new Home_fragment_all();
        FT.add(R.id.fragment_content, fragmentAll);
        FT.commit();

        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_all.setBackgroundResource(R.drawable.home_btn_all_press);
                btn_care.setBackgroundResource(R.drawable.home_btn_care_normal);

                FragmentManager FM = getFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                Home_fragment_all fragmentAll=new Home_fragment_all();
                FT.add(R.id.fragment_content, fragmentAll);
                FT.commit();

            }
        });
        btn_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_care.setBackgroundResource(R.drawable.home_btn_care_press);
                btn_all.setBackgroundResource(R.drawable.home_btn_all_normal);

                FragmentManager FM = getFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                Home_fragment_care fragmentCare=new Home_fragment_care();
                FT.add(R.id.fragment_content, fragmentCare);
                FT.commit();
            }
        });

		return rootView;
	}
    public void UpdateData(){

    }

}
