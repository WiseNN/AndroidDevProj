package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DragActivity extends AppCompatActivity
{



    @BindView(R.id.img_view_bouncy_ball)
    ImageView bouncyBallImageView;

    @BindView(R.id.img_view_bouncy_ball2)
    ImageView bouncyBallImageView2;


    @BindView(R.id.btn_start_animation)
    Button startButton;


    float newXPos1;
    float newXPos2;



    @OnClick(R.id.btn_start_animation)
    public void animationHandler(View view)
    {

    }

    @OnClick(R.id.img_view_bouncy_ball)
    public void animationHandler2(View view)
    {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_layout);
        ButterKnife.bind(this);


        bouncyBallImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getRawX();

                float y = event.getRawY();

                bouncyBallImageView.setTranslationX(x);
                bouncyBallImageView.setTranslationY(y);

                return true;
            }
        });


    }

}
