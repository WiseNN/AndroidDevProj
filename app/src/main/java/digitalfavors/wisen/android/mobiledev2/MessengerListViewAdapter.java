package digitalfavors.wisen.android.mobiledev2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MessengerListViewAdapter extends BaseAdapter {
ArrayList<String> myList;
Context mContext;


MessengerListViewAdapter(ArrayList<String> list, Context context)
{
    myList = list;
    mContext = context;

}

    @Override
    public int getCount()
    {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RelativeLayout relView;

        Log.d("Getview", "getView");
        if(position % 2 == 0)
        {
            Log.d("Getview", "left");
            relView = (RelativeLayout) LayoutInflater.from
                    (mContext.getApplicationContext())
                    .inflate(R.layout.messages_screen_item_left, parent,false );

            TextView tvBubble = relView.findViewById(R.id.tv_left_bubble);
            tvBubble.setText(myList.get(position));
            return relView;

        }else{
            Log.d("Getview", "right");
            relView = (RelativeLayout) LayoutInflater.from
                    (mContext.getApplicationContext())
                    .inflate(R.layout.messages_screen_item_right, parent,false );
            TextView tvBubble = relView.findViewById(R.id.tv_right_bubble);
            tvBubble.setText(myList.get(position));
            return relView;
        }

    }
}
