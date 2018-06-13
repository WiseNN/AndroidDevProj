package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PicFragment extends Fragment {

    ImageView imgView1;
    ImageView imgView2;
    TextView headerTextView;
    ImageView.ScaleType[] scaleTypes;
    int pagePosition;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scaleTypes = ImageView.ScaleType.values();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //if container exists, inflate fragment container view, and return
        if(container != null)
        {


            LinearLayout linearLayoutView = (LinearLayout) inflater.inflate(R.layout.pic_fragment, container, false);
            imgView1 = linearLayoutView.findViewById(R.id.img_view_1);
            imgView1.setScaleType(scaleTypes[pagePosition]);
            imgView2 = linearLayoutView.findViewById(R.id.img_view_2);
            imgView2.setScaleType(scaleTypes[pagePosition]);
            headerTextView = linearLayoutView.findViewById(R.id.header_text_view);
            headerTextView.setText(scaleTypes[pagePosition].toString());


            return linearLayoutView;
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

}
