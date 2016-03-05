package com.example.wenzhao.helpinghand.ble.pro.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;


public class ActivityChoiceFragment extends Fragment {
    TextView textViewName;
    TextView textViewWeakArm;
    TextView textViewRead;
    TextView textViewChoice;
    RadioGroup radioGroup;
    RadioButton radioButton;
    //public String text;
    public static String TableActivity;

    Button btnNextChoice;

    public static ActivityChoiceFragment newInstance(){
        ActivityChoiceFragment fragment = new ActivityChoiceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_activity_choice, container, false);

        textViewName = (TextView)view.findViewById(R.id.textView12);
        textViewWeakArm = (TextView)view.findViewById(R.id.textView14);
        textViewRead = (TextView)view.findViewById(R.id.textView15);
        textViewChoice = (TextView)view.findViewById(R.id.textView16);
        btnNextChoice = (Button)view.findViewById(R.id.btn_next_choice);
        radioGroup = (RadioGroup)view.findViewById(R.id.myRadioGroup);
        radioButton = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
        TableActivity = radioButton.getText().toString();
        Log.i("radioGroup",TableActivity);




        textViewName.setText("For "+InputFragment.ChildName+":");
        textViewWeakArm.setText("Weak arm: "+InputFragment.WeakArm);

        String temp = null;
        if(InputFragment.AbleToRead){
            temp = "Yes";
        }else{
            temp = "No";
        }
        textViewRead.setText("Able to read: "+ temp);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //在这个函数里面用来改变选择的radioButton的数值，以及与其值相关的 //任何操作，详见下文
                selectRadioBtn(view);
            }
        });


        btnNextChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });

        return view;
    }


    private void selectRadioBtn(View view){
        radioButton = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
        TableActivity = radioButton.getText().toString();
        Log.i("radioGroup",TableActivity);

    }
}
