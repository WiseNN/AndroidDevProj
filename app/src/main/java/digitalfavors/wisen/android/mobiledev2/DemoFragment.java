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
import java.util.LinkedHashMap;

public class DemoFragment extends android.support.v4.app.Fragment
{
    ListView myListView;
    LinkedHashMap<String,String> dataToIntentClassMap;


    int DEMO_FRAG_RESOURCE_ID = 7836441;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String[] classList =  {
                "digitalfavors.wisen.android.mobiledev2.NewActivity",
                "digitalfavors.wisen.android.mobiledev2.MyViewPager",
                "digitalfavors.wisen.android.mobiledev2.MyViewPager"
        };
        String[] dataList = {
                "A","B","C"
        };

        int size = 3;
         dataToIntentClassMap = new LinkedHashMap<>();

        for(int i=0; i<size;i++)
        {

            //get letter from charCode
            String letter = Character.toString((char)(97+i));

            //add letter to list
            dataToIntentClassMap.put(dataList[i], classList[i]);
        }

        Log.i("List Info ", dataToIntentClassMap.toString());

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        //if container exists, inflate fragment container view, and return
        if(container != null)
        {
            View frameLayoutView = inflater.inflate(R.layout.fragment_demo, container, false);
            myListView = frameLayoutView.findViewById(R.id.list_view);


//            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, stringList);
            CustomListAdapter cc = new CustomListAdapter(getActivity().getBaseContext(),DEMO_FRAG_RESOURCE_ID,dataToIntentClassMap, myListView);

//            CustomListAdapter listAdapter = new CustomListAdapter();
            myListView.setAdapter(cc);



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
