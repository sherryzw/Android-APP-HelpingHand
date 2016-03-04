package com.example.wenzhao.helpinghand.ble.pro.HelpingHand;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;
import com.example.wenzhao.helpinghand.ble.pro.Fragment.InputFragment;

public class ResultActivity extends Activity {
    private Button btnReplay;
    private Button btnExit;
    private double sum1;
    private double sum2;
    private double forsum1;
    private double forsum2;
    private double finalRatio = 0;
    TextView resultText;
    RatingBar ratingBar;
    ImageView imageView;

    public static int Finish = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultText = (TextView)findViewById(R.id.textView17);
        btnReplay = (Button)findViewById(R.id.btn_replay);
        btnExit = (Button)findViewById(R.id.btn_exit);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        imageView = (ImageView)findViewById(R.id.imageView2);
        sum1 = 0;
        sum2 = 0;
        forsum1 = 0;
        forsum2 = 0;
        for (int i = 1;i < DeviceActivity.M1OverTime.size();i++){
            forsum1+= DeviceActivity.M1OverTime.get(i);
        }
        Log.i("~~ average sum1~~"," = "+forsum1/DeviceActivity.M1OverTime.size());

        for (int i = 1;i < DeviceActivity.M2OverTime.size();i++){
            forsum2+= DeviceActivity.M2OverTime.get(i);
        }
        Log.i("~~ average sum2~~"," = "+forsum2/DeviceActivity.M2OverTime.size());

        for(int i = 1;i < Math.min(DeviceActivity.M2OverTime.size(), DeviceActivity.M1OverTime.size());i++) {
            sum2 += DeviceActivity.M2OverTime.get(i);
            sum1 += DeviceActivity.M1OverTime.get(i);
        }
        //finalRatio /= (DeviceActivity.ratioOverTime.size() - 1);
        if (sum1<sum2){
            finalRatio = 100*sum1/(sum2+sum1);
        }else{
            finalRatio = 100*sum2/(sum1+sum2);
        }
        setImage(finalRatio);
        setRating(finalRatio);

        resultText.setText("Finished in " + String.format("%.1f", DeviceActivity.time) + " s. "
                + "Your " + InputFragment.WeakArm + " hand did "
                + String.format("%.2f", finalRatio) + "% of the work! Try again to beat your" +
                " score.");

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Finish = 1;
                finish();
            }
        });
    }

    private void setRating(double rating){
        if((rating >=0 && rating <5)||(rating >95 && rating <=100)) ratingBar.setRating((float)0.5);
        if((rating >=5 && rating <10)||(rating >90 && rating <=95)) ratingBar.setRating((float)1);
        if((rating >=10 && rating <15)||(rating >85 && rating <=90)) ratingBar.setRating((float)1.5);
        if((rating >=15 && rating <20)||(rating >80 && rating <=85)) ratingBar.setRating((float)2);
        if((rating >=20 && rating <25)||(rating >75 && rating <=80)) ratingBar.setRating((float)2.5);
        if((rating >=25 && rating <30)||(rating >70 && rating <=75)) ratingBar.setRating((float)3);
        if((rating >=30 && rating <35)||(rating >65 && rating <=70)) ratingBar.setRating((float)3.5);
        if((rating >=35 && rating <40)||(rating >60 && rating <=65)) ratingBar.setRating((float)4);
        if((rating >=40 && rating <45)||(rating >55 && rating <=60)) ratingBar.setRating((float)4.5);
        if((rating >=45 && rating <=50)||(rating >50 && rating <=55)) ratingBar.setRating((float)5);
    }

    private void setImage(double rating){
        if((rating >=0 && rating <25)||(rating >75 && rating <=100)) imageView.setImageResource(R.drawable.sadface);
        if((rating >=25 && rating <50)||(rating >=50 && rating <=75)) imageView.setImageResource(R.drawable.smileface);
    }
}
