package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimerActivity extends AppCompatActivity
{
    CountDownTimer timer;

    @BindView(R.id.btn_download)
    Button donwloadBtn;

    @BindView(R.id.btn_countdown)
    Button countdownBtn;

    @BindView(R.id.tv_countdown)
    TextView countDownTextView;

    @OnClick(R.id.btn_countdown)
    public void countdonwHandler()
    {
        if(timer != null)
        {
            timer.cancel();
        }

        timer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countDownTextView.setText("done!");
            }
        };

        timer.start();
    }

    @OnClick(R.id.btn_download)
    public void downloadHandler()
    {
        if(timer != null)
        {
            timer.cancel();
        }
           timer = new CountDownTimer(10000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long secondsLeft = millisUntilFinished / 1000;
                    switch (secondsLeft+"")
                    {
                        case "10" : countDownTextView.setText("Download");
                            break;
                        case "8" : countDownTextView.setText("Starting...");
                            break;
                        case "5" : countDownTextView.setText("Downloading");
                            break;
                        case "4" : countDownTextView.setText("Downloading.");
                            break;
                        case "3" : countDownTextView.setText("Downloading..");
                            break;
                        case "2" : countDownTextView.setText("Downloading...");
                            break;
                        case "1" : countDownTextView.setText("Downloading....");
                            break;
                    }

                }

                public void onFinish() {
                    countDownTextView.setText("Download Finished!");
                }
            };
           timer.start();
    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown_activity);
        ButterKnife.bind(this);




    }




}
