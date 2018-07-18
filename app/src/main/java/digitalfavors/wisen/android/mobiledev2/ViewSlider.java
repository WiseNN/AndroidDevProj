package digitalfavors.wisen.android.mobiledev2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ViewSlider extends AppCompatActivity{
    WorkFragment workFragment;
    View mainView;
//    Float num = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);

        mainView = findViewById(R.id.new_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //create work fragment instance
        workFragment = new WorkFragment();



        fragmentTransaction.add(R.id.new_activity, workFragment);


//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();


    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();


        workFragment.getView().setTranslationX(-1200);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.EDGE_LEFT)
                {
                    workFragment.getView().animate().x(800);
                }
//                workFragment.onTouch(workFragment.getView(), event);
                return true;
            }
        });

//                workFragment.getView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float x = event.getRawX()-(v.getWidth()/2);
//
//                Log.d("val", x+"");
//
////                float y = event.getRawY()- (v.getHeight()/2);
//
//
//                v.setTranslationX(x);
////                v.setTranslationY(y);
//
//                return true;
//            }
//        });








        Log.d("width", workFragment.getView().getMeasuredWidth()+"");





    }


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        Log.d("track", event.getX()+" "+event.getY());
//        workFragment.onTouch(workFragment.getView(), event);
////
//        return true;
//    }
}
