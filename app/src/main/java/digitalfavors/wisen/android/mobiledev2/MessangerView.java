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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;



public class MessangerView extends Activity
{

    ArrayList<DataSnapshot> messagesList;
    ListView mListView;
    EditText msgTextField;
    RelativeLayout messengerView;
    MessengerListViewAdapter adapter;
    final String senderUsername = "WiseNN";
    final String recipeintUsername = "tellMeWhen2Go";
    final String privateChatKey = "privateChat";
    final String userPostsKey = "user-posts";
    final String messagesKey = "messages";
    long initLoadMessageCount = 0;
    long currentMsgCount = 0;
    boolean isInitialLoadFinished = false;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        messagesList = new ArrayList<>();
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
        setContentView(messengerView);


        //get firebase reference
        final DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();

        //create new message child event listener
        final ChildEventListener newChildMessageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //increment currentMsgCount
                currentMsgCount++;
                //if the current amount of messages in the arrayList is less than the
                //max number of expected messages, add message to list,
                //else, add last message to list and set the adapter
                // and flip isInitialLoadFinished boolean
                if(currentMsgCount < initLoadMessageCount)
                {
                    //add message to list of messages
                    messagesList.add(dataSnapshot);


                    //this should be called once, when all messages have been added on initial load
                    //do whatever you need to do on initial load, after all data has loaded, here
                }else if(!(currentMsgCount < initLoadMessageCount) && !isInitialLoadFinished) {

                    //add message to list of messages
                    messagesList.add(dataSnapshot);

                    //make initial load finished
                    isInitialLoadFinished = true;

                    adapter = new MessengerListViewAdapter(senderUsername, recipeintUsername, messagesList,
                            initLoadMessageCount, MessangerView.this);

                    //set the listView adapter
                    mListView.setAdapter(adapter);

                    //set selection to last item in list to force view to start
                    // from bottom of screen
                    mListView.setSelection(messagesList.size());





                }//this should be called when a new message gets edded to the messagesList
                else if(isInitialLoadFinished){

                    messagesList.add(dataSnapshot);
                    adapter.notifyDataSetChanged();
                    mListView.smoothScrollToPosition(messagesList.size()-1);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };


        //create initial firebase load listener to get initial messages count
      ValueEventListener initMsgValueEventListener = new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              initLoadMessageCount = dataSnapshot.getChildrenCount();

              DBRef.child("privateChat").child(senderUsername).child(recipeintUsername).child(messagesKey).addChildEventListener(newChildMessageListener);

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {}
      };

      //create successful message addition listener
        final OnSuccessListener successAddMessageListener = new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                msgTextField.setText("", TextView.BufferType.NORMAL);
                Log.d("onSuccess", "Wrote "+senderUsername+"'s message to the" +
                        "database");
//                            mListView.invalidate();
            }
        };

        //create failure message addition listener
        final OnFailureListener failedAddMessageListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("onFailure", "FAILED to write "+senderUsername+"'s message to the" +
                        "database");
            }
        };





        //add one event listener to list messages on initial load
        //get down to messages child level, and retrieve list of messages
        DBRef.child("privateChat").child(senderUsername).child(recipeintUsername).child(messagesKey).addListenerForSingleValueEvent(initMsgValueEventListener);






        //set message textField event listener to send message (update firebase)
        msgTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Boolean handled = false;

                if(actionId == EditorInfo.IME_ACTION_SEND)
                {

                    //create message map
                    HashMap<String, String> messageObj = createMessage(senderUsername, v.getText().toString());

                    //mesage uid & timestamp
                    String privateChatMsgkey = DBRef.child(privateChatKey).child(senderUsername).child(recipeintUsername).child(messagesKey).push().getKey();
                    String privateChatMsgPath = privateChatKey+"/"+senderUsername+"/"+recipeintUsername+"/"+messagesKey+"/"+privateChatMsgkey;
                    String userPostMsgKey = DBRef.child(userPostsKey).child(senderUsername).child(messageObj.get("date")).push().getKey();
                    String userPostMsgPath =  userPostsKey+"/"+senderUsername+"/"+messageObj.get("date")+"/"+userPostMsgKey;


                    //create childUpdates map
                    HashMap<String, Object> childUpdates = new HashMap<String, Object>();

                    //put privateChat childUpdate in map
                    childUpdates.put(privateChatMsgPath, messageObj);

                    //clone & remove date & sender from messageObj
                    HashMap<String, Object> userPostMap = (HashMap<String, Object>) messageObj.clone();
                    userPostMap.remove("sender");
                    userPostMap.remove("date");

                    //now put remaining object as user-post messageObj
                    childUpdates.put(userPostMsgPath, userPostMap);

                    //instantiate add message success & failure listeners
                    DBRef.updateChildren(childUpdates).addOnSuccessListener(successAddMessageListener).addOnFailureListener(failedAddMessageListener);

                    handled = true;
                }
                return handled;
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();





    }




    HashMap<String, String> createMessage(String sender, String message)
    {
        HashMap<String, String> messageObj = new HashMap<>();



        //get the current date
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());


        int month = calendar.get(Calendar.MONTH)+1; //offset by +1, month is incorrect
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String date = year+"-"+month+"-"+day;

        //get current time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());

        messageObj.put("date", date);
        messageObj.put("message", message);
        messageObj.put("sender", sender);
        messageObj.put("time", time);


        return messageObj;
    }



    @Override
    protected void onPause() {
        super.onPause();

        //remove the new message event listener
        FirebaseDatabase.getInstance().getReference().child("privateChat").child(senderUsername)
                .child(recipeintUsername).child(messagesKey).removeEventListener(adapter.newMsgEventListener);
    }

}
