package digitalfavors.wisen.android.mobiledev2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class DemoFragment extends Fragment
{
    ListView myListView;
    String[] stringList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int arySize = 20;
         stringList = new String[arySize];

        for(int i=0; i<arySize;i++)
        {

            //get letter from charCode
            String letter = Character.toString((char)(97+i));
            //add letter to list
            stringList[i] = letter;
        }

        Log.i("List Info: ", Arrays.toString(stringList));

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        //if container exists, inflate fragment container view, and return
        if(container != null)
        {
            View frameLayoutView = inflater.inflate(R.layout.fragment_demo, container, false);
            myListView = frameLayoutView.findViewById(R.id.list_view);

            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, stringList);

            myListView.setAdapter(aa);


            return frameLayoutView;
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
