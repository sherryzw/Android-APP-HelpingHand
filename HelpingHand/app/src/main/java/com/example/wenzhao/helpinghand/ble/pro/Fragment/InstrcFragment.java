package com.example.wenzhao.helpinghand.ble.pro.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;

public class InstrcFragment extends Fragment {
    Button btnBegin;

    public static InstrcFragment newInstance() {
        InstrcFragment fragment = new InstrcFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instrc, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SimpleLife.ttf");
        TextView t0 = (TextView) view.findViewById(R.id.textView);
        t0.setTypeface(font);
        TextView t1 = (TextView) view.findViewById(R.id.textView1);
        t1.setTypeface(font);


        btnBegin = (Button)view.findViewById(R.id.btn_begin);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Fragment inputFragment = InputFragment.newInstance();
                ft.replace(R.id.InsContainer, inputFragment);
                ft.addToBackStack("Switch to Input Fragment");
                ft.commit();
            }
        });
        return view;
    }
}
