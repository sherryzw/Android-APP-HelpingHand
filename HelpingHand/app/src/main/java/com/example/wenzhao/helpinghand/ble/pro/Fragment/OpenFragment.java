package com.example.wenzhao.helpinghand.ble.pro.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ti.ble.sensortag.R;

public class OpenFragment extends Fragment {

    public static OpenFragment newInstance() {
        OpenFragment fragment = new OpenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View openView = inflater.inflate(R.layout.fragment_open, container, false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Fragment instrcFragment = InstrcFragment.newInstance();
                ft.replace(R.id.InsContainer, instrcFragment);
                ft.addToBackStack("Switch to Instrc Fragment");
                ft.commit();
            }
        },1800);
        return openView;
    }

}
