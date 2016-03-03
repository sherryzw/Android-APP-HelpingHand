package com.example.wenzhao.helpinghand.ble.pro.HelpingHand;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ti.ble.sensortag.R;

public class ResultActivity extends Activity {
    private Button btnReplay;
    private double finalRatio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnReplay = (Button)findViewById(R.id.btn_replay);
        for(double temp : DeviceActivity.ratioOverTime) finalRatio += temp;
        finalRatio /= DeviceActivity.ratioOverTime.size();

        Log.e("error",String.valueOf(DeviceActivity.time));
        Log.e("error",String.valueOf(finalRatio));

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
