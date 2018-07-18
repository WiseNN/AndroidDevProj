package digitalfavors.wisen.android.mobiledev2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class WorkFragment extends android.support.v4.app.Fragment implements View.OnTouchListener
{



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //if container exists, inflate fragment container view, and return
        if(container != null)
        {
            return inflater.inflate(R.layout.fragment_work, container, false);
        }
        //else, return generic superview
        else{
            return  super.onCreateView(inflater, container, savedInstanceState);
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {


        float x = (event.getRawX());

//        float y = event.getRawY();

        v.setTranslationX(-x);
//        v.setTranslationY(y);
        Log.d("track", x+" ");

        return true;

    }
}
