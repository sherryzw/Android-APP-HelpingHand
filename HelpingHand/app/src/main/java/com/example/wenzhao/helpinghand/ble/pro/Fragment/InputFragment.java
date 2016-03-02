package com.example.wenzhao.helpinghand.ble.pro.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ti.ble.sensortag.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputFragment extends Fragment {
    Button btnNextInput;

    public static InputFragment newInstance(){
        InputFragment fragment = new InputFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        btnNextInput = (Button)view.findViewById(R.id.btn_next_input);
        btnNextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Fragment activityChoiceFragment = ActivityChoiceFragment.newInstance();
                ft.replace(R.id.InsContainer, activityChoiceFragment);
                ft.addToBackStack("Switch to Activity Choice Fragment");
                ft.commit();
            }
        });
        return view;
    }

}
