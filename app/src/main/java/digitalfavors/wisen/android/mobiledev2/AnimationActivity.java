package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimationActivity extends AppCompatActivity
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
        animateBallOne(view);
        animateBallTwo(view);
    }

    @OnClick(R.id.img_view_bouncy_ball)
    public void animationHandler2(View view)
    {
        animateBallOne(view);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_layout);
        ButterKnife.bind(this);

    }


    public void animateBallOne(View view)
    {
        startButton.setEnabled(false);
        // create an animation for your view and set the property you want to animate
        SpringAnimation animation = new SpringAnimation(bouncyBallImageView, SpringAnimation.X);

        // create a spring with desired parameters
        SpringForce spring = new SpringForce();
        spring.setFinalPosition(newXPos1); // can also be passed directly in the constructor
        spring.setStiffness(SpringForce.STIFFNESS_LOW); // optional, default is STIFFNESS_MEDIUM
        spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);// optional, default is DAMPING_RATIO_MEDIUM_BOUNCY

        // set your animation's spring
        animation.setSpring(spring);

        animation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                 if(bouncyBallImageView.getX() == 600)
                 {
                     newXPos1 = 10;
                 }else{
                    newXPos1 = 600;
                 }

                startButton.setEnabled(true);
            }
        });

        // animate!
        animation.start();

    }

    public void animateBallTwo(final View view)
    {
        startButton.setEnabled(false);
        // create an animation for your view and set the property you want to animate
        SpringAnimation animation = new SpringAnimation(bouncyBallImageView2, SpringAnimation.X);

        // create a spring with desired parameters
        SpringForce spring = new SpringForce();
        spring.setFinalPosition(newXPos2); // can also be passed directly in the constructor
        spring.setStiffness(SpringForce.STIFFNESS_LOW); // optional, default is STIFFNESS_MEDIUM
        spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);// optional, default is DAMPING_RATIO_MEDIUM_BOUNCY

        // set your animation's spring
        animation.setSpring(spring);

        animation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                if(bouncyBallImageView2.getX() == 100)
                {
                    newXPos2 = ((View)view.getParent()).getWidth() - bouncyBallImageView.getWidth();
                }else{
                    newXPos2 = 100;
                }
                startButton.setEnabled(true);
            }
        });

        // animate!
        animation.start();
    }



}
