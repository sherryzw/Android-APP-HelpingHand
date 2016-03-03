package com.example.wenzhao.helpinghand.ble.pro.HelpingHand;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;
import com.example.wenzhao.helpinghand.ble.pro.Fragment.InputFragment;

public class ResultActivity extends Activity {
    private Button btnReplay;
    private double finalRatio = 0;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultText = (TextView)findViewById(R.id.textView17);
        btnReplay = (Button)findViewById(R.id.btn_replay);
        for(int i = 1;i < DeviceActivity.ratioOverTime.size();i++) finalRatio += DeviceActivity.ratioOverTime.get(i);
        finalRatio /= (DeviceActivity.ratioOverTime.size() - 1);

        resultText.setText("Finished in " + String.valueOf(DeviceActivity.time) + " s. "
                +"Your "+ InputFragment.curChild.getWeakArm() + " hand did "
                + String.valueOf(finalRatio) + "% of the work! Try again to beat your" +
                " score.");

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
