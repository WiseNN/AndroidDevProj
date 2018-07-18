package digitalfavors.wisen.android.mobiledev2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessangerView extends Activity {

    ArrayList<String> messagesList;
    ListView mListView;
    EditText msgTextField;
    RelativeLayout messengerView;
    MessengerListViewAdapter adapter;
    final String senderUsername = "WiseNN";
    final String recipeintUsername = "tellMeWhen2Go";
    final String messagesKey = "messages";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        messagesList = new ArrayList<String>();
//
//        messagesList.add("Hey Whats Up!");
//        messagesList.add("Nothing much, just writing this app ");
//        messagesList.add("Are you coming over later? \nI was gonn see if we could go " +
//                "to the spot you love ;) \n\nYou don't have to , just let me know!");
//        messagesList.add("Ok...give me like 1 hour, if you don't mind.");
//
//
        messengerView = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.msg_screen, null);
        mListView = messengerView.findViewById(R.id.lv_for_messages);
        msgTextField = messengerView.findViewById(R.id.message_text_field);


       ValueEventListener getMsgsListener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //convert to JSON for reading
                JSONObject obj = new JSONObject((Map<String, String>)dataSnapshot.getValue());

                //get an Iterable list of messages as DataSnapshot
                Iterable<DataSnapshot> msgList = dataSnapshot.getChildren();

                // instant. adapter, and send iterable list to the adapter
                adapter = new MessengerListViewAdapter(senderUsername,recipeintUsername,messagesList,msgList,dataSnapshot.getChildrenCount(),getApplicationContext());

                //set adapter on listView
                mListView.setAdapter(adapter);

                //print the json object of messages
                try{
                    Log.d("FireJSON", obj.toString(3));
                    Log.d("FireList", msgList.toString());
                }
                catch (Exception e)
                {
                    Log.d("jsonErr", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        //get firebase reference
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference DBMsgsRef = DBRef.child("privateChat").child(senderUsername).child(recipeintUsername).child(messagesKey).getRef();

        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //get down to messages child level, and retrieve list of messages
        DBMsgsRef.addValueEventListener(getMsgsListener);

//                DBMsgsRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        //get an Iterable list of messages as DataSnapshot
//                        Iterable<DataSnapshot> msgList = dataSnapshot.getChildren();
//
//                        //set Iterable<DataSnapshot> with updated list of messages
//                        adapter.setMsgsList(msgList);
//
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



        //set message textField event listener to send message (update firebase)
//        msgTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Boolean handled = false;
//
//                if(actionId == EditorInfo.IME_ACTION_SEND)
//                {
//                    String key = DBMsgsRef.push().getKey();
//
//                    handled = true;
//                }
//                return handled;
//            }
//        });

        setContentView(messengerView);
    }


















    Map<String, String> createMessage(String sender, String meessage)
    {
        Map<String, String> messageObj = new HashMap<>();

        //get the current date
        Date date = new Date();

            date.getTime();


//        LocalDate today = LocalDate.now();
          Date d = new Date();

//        int month = today.getMonthValue();
//        int day = today.getDayOfMonth();
//        int year = today.getYear();
//        final String creationDate = year+"-"+month+"-"+day;
//        LocalDateTime time = new LocalDateTime.now();
//        Log.d("time", time.toString());







        return null;
    }

}
