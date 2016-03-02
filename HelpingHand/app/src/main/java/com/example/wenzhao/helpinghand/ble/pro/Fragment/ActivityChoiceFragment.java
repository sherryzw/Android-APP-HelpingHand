package com.example.wenzhao.helpinghand.ble.pro.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ti.ble.sensortag.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityChoiceFragment extends Fragment {
    public static ActivityChoiceFragment newInstance(){
        ActivityChoiceFragment fragment = new ActivityChoiceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_activity_choice, container, false);
    }

}
