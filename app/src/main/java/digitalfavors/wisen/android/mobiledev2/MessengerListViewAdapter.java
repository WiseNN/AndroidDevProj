package digitalfavors.wisen.android.mobiledev2;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MessengerListViewAdapter extends BaseAdapter {

Context mContext;
private ArrayList<DataSnapshot> msgsList;
long msgsCount;
long count; //inHouse counter

private String senderUsername;
private String recipeintUsername;
private String messagesKey = "messages";
ChildEventListener newMsgEventListener;



MessengerListViewAdapter(String sender, String recipeint, ArrayList<DataSnapshot> msgsList,
                         long msgsCount, Context context)
{
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

    public void setMsgsList(ArrayList<DataSnapshot> msgsList)
    {
        this.msgsList = msgsList;
    }

    @Override
    public int getCount()
    {
        return this.msgsList.size();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public Object getItem(int position) {
        return msgsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {


        RelativeLayout relView = null;


        Log.d("Getview", "getView");


            //count current number of messages
            count++;

            //if so, get next message
            DataSnapshot nextMsg = msgsList.get(position);

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


            }
            else if(sender.equals(senderUsername))
            {
                Log.d("Getview", "right");
                relView = (RelativeLayout) LayoutInflater.from
                        (mContext.getApplicationContext())
                        .inflate(R.layout.messages_screen_item_right, parent,false );
                TextView tvBubble = relView.findViewById(R.id.tv_right_bubble);
                tvBubble.setText(messageText);

            }

            return relView;

    }



    public void addMessage(DataSnapshot message)
    {
        //add message
        msgsList.add(message);


    }

}
