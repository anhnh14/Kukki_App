package com.linhminhoo.kukki.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linhminhoo.kukki.R;


public class Item2 extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

     //   Toast.makeText(getActivity(), "Here 2", Toast.LENGTH_SHORT).show();

		return inflater.inflate(R.layout.item2, null);
	}

}
