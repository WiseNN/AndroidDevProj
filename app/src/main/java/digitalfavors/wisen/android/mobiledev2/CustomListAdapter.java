package digitalfavors.wisen.android.mobiledev2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class CustomListAdapter extends BaseAdapter
{
    int mResource;
    Context mContext;
    private TextView mCurrentViewCell;

    List<String> dataList;
    ListView myListView;
    private List<String> intentClassNameList;



    public CustomListAdapter(@NonNull Context context, int resource, @NonNull LinkedHashMap<String,String> valueToIntentClassMap,
                             ListView myListView)
    {

//        super(context, resource, textViewResourceId, objects);

        this.mResource = resource;
        this.myListView = myListView;
        this.mContext = context;
        this.mCurrentViewCell = null;
        final int mapSize = valueToIntentClassMap.size();

        //cast Values generic collection to string list
        dataList = new ArrayList<>(valueToIntentClassMap.keySet());

        //cast Keys to intent class name list
        intentClassNameList = new ArrayList<>(valueToIntentClassMap.values());

    }



    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }





    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent)
    {
        //create click listener per item view, similar to cellForRowAtIndexPath

        //if null, get text view from xml
        if(convertView == null)
        {


            mCurrentViewCell = (TextView) LayoutInflater.from(mContext).inflate(R.layout.cell_text_view,null);

//            //if current cell does not have listeners, add click listener
//            if(!mCurrentViewCell.hasOnClickListeners())
//            {
                mCurrentViewCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View viewCell)
                    {

                        //create dynamic intent
                        Intent intent = getIntentForRowAtIndexPosition(position);


                        //start new activity from main activity
                        mContext.startActivity(intent);
                    }
                });
//            }

        }

        //set the current view cell
        mCurrentViewCell.setText(dataList.get(position));

        convertView = mCurrentViewCell;

        return mCurrentViewCell;
    }


    //get corresponding class for index
    private Intent getIntentForRowAtIndexPosition(int position)
    {
      Intent intent = new Intent();
      intent.putExtra("pagePosition", position);
      final String pkgRef = "digitalfavors.wisen.android.mobiledev2";

      return intent.setClassName(pkgRef, intentClassNameList.get(position));
    }


}
