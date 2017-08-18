package com.linhminhoo.kukki.ViewModel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linhminhoo.kukki.R;

/**
 * Created by linhminhoo on 7/6/2015.
 */
public class Home_fragment_care extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_care, container,false);

        return view;
    }
}
