package com.linhminhoo.kukki.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linhminhoo.kukki.R;


public class Item3 extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Toast.makeText(getActivity(), "Here 3", Toast.LENGTH_SHORT).show();

		return inflater.inflate(R.layout.item3, null);
	}
}
