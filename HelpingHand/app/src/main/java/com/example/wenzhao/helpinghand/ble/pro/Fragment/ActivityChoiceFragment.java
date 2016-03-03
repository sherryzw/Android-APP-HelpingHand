package com.example.wenzhao.helpinghand.ble.pro.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;


public class ActivityChoiceFragment extends Fragment {
    TextView textViewName;
    TextView textViewWeakArm;
    TextView textViewRead;
    TextView textViewChoice;

    Button btnNextChoice;

    public static ActivityChoiceFragment newInstance(){
        ActivityChoiceFragment fragment = new ActivityChoiceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_choice, container, false);

        textViewName = (TextView)view.findViewById(R.id.textView12);
        textViewWeakArm = (TextView)view.findViewById(R.id.textView14);
        textViewRead = (TextView)view.findViewById(R.id.textView15);
        textViewChoice = (TextView)view.findViewById(R.id.textView16);
        btnNextChoice = (Button)view.findViewById(R.id.btn_next_choice);

        textViewName.setText("For "+InputFragment.curChild.getName()+":");
        textViewWeakArm.setText("Weak arm: "+InputFragment.curChild.getWeakArm());

        String temp = null;
        if(InputFragment.curChild.isAbleToTalk()){
            temp = "Yes";
        }else{
            temp = "No";
        }
        textViewRead.setText("Able to read: "+ temp);

        btnNextChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

}
