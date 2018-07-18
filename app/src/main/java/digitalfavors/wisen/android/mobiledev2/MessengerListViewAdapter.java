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

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class MessengerListViewAdapter extends BaseAdapter {
private ArrayList<String> myList;
Context mContext;
private Iterable<DataSnapshot> msgsList;
long msgsCount;
private String senderUsername;
private String recipeintUsername;


MessengerListViewAdapter(String sender, String recipeint,ArrayList<String> list, Iterable<DataSnapshot> msgsList, long msgsCount, Context context)
{
    myList = list;
    mContext = context;
    this.msgsList = msgsList;
    this.msgsCount = msgsCount;
    this.senderUsername = sender;
    this.recipeintUsername = recipeint;

}

    public Iterable<DataSnapshot> getMsgsList()
    {
        return msgsList;
    }

    public void setMsgsList(Iterable<DataSnapshot> msgsList)
    {
        this.msgsList = msgsList;
    }

    @Override
    public int getCount()
    {
        return Math.toIntExact(msgsCount);
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


        //check if there is another message
        if(msgsList.iterator().hasNext())
        {
            //if so, get next message
            DataSnapshot nextMsg = msgsList.iterator().next();

            //get username of sender
            String sender = (String)nextMsg.child("sender").getValue();

            //get message text
            String messageText = (String)nextMsg.child("message").getValue();

            Log.d("msgObj", "sender -> "+sender+" :: messageText -> "+messageText);
            //if sender name is recipeint's...load left-side, otherwise load right-side
            if(sender.equals(recipeintUsername))
            {
                Log.d("Getview", "left");
                //load left messaging bubble screen
                relView = (RelativeLayout) LayoutInflater.from
                        (mContext.getApplicationContext())
                        .inflate(R.layout.messages_screen_item_left, parent,false );

                //load left messaging textView
                TextView tvBubble = relView.findViewById(R.id.tv_left_bubble);
                tvBubble.setText(messageText);
                return relView;

            }
            else if(sender.equals(senderUsername))
            {
                Log.d("Getview", "right");
                relView = (RelativeLayout) LayoutInflater.from
                        (mContext.getApplicationContext())
                        .inflate(R.layout.messages_screen_item_right, parent,false );
                TextView tvBubble = relView.findViewById(R.id.tv_right_bubble);
                tvBubble.setText(messageText);
                return relView;
            }
            else{return convertView;}

        }else{
            return convertView;
        }




    }
}
