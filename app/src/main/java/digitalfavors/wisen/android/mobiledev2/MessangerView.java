package digitalfavors.wisen.android.mobiledev2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;



public class MessangerView extends Fragment
{

    TextView messageThreadTitle;
    ArrayList<DataSnapshot> messagesList;
    ListView mListView;
    EditText msgTextField;
    RelativeLayout messengerView;
    MessengerListViewAdapter adapter;
    String senderUsername;
    String recipeintUsername;
    final String privateChatKey = "privateChat";
    final String userPostsKey = "user-posts";
    final String messagesKey = "messages";
    long initLoadMessageCount = 0;
    long currentMsgCount = 0;
    boolean isInitialLoadFinished = false;
    private GestureDetectorCompat mDetector;








    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        //adjust screen when keyboard is present
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        messagesList = new ArrayList<>();
        messengerView = (RelativeLayout) LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.msg_screen, null);
        mListView = messengerView.findViewById(R.id.lv_for_messages);
        msgTextField = messengerView.findViewById(R.id.message_text_field);
        messageThreadTitle = messengerView.findViewById(R.id.tv_message_thread_title);

        //gesture detector in parentView, to check for keyboard retreat
        setupKeyboardGesture();

        //set username and recipeintUsername (if not present disable message textField)
        checkUserArguments();




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
                            initLoadMessageCount, getContext());

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

                //show database fail error on screen @ application level
                Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

                    //create private message key for new message
                    String privateChatMsgkey = DBRef.child(privateChatKey).child(senderUsername).child(recipeintUsername).child(messagesKey).push().getKey();
                    //append private message key, to newly created private message path for current user
                    String userPrivateChatMsgPath = privateChatKey+"/"+senderUsername+"/"+recipeintUsername+"/"+messagesKey+"/"+privateChatMsgkey;
                    //append private message key, to newly created private message path for recipeint user
                    String recipeintPrivateChatMsgPath = privateChatKey+"/"+recipeintUsername+"/"+senderUsername+"/"+messagesKey+"/"+privateChatMsgkey;

                    //create new user-post key for current message
                    String userPostMsgKey = DBRef.child(userPostsKey).child(senderUsername).child(messageObj.get("date")).push().getKey();
                    // append user-post key to user-post, to create user-post path
                    String userPostMsgPath =  userPostsKey+"/"+senderUsername+"/"+messageObj.get("date")+"/"+userPostMsgKey;


                    //create childUpdates map
                    HashMap<String, Object> childUpdates = new HashMap<String, Object>();

                    //put privateChat childUpdates in map
                    childUpdates.put(userPrivateChatMsgPath, messageObj);
                    childUpdates.put(recipeintPrivateChatMsgPath, messageObj);

                    //clone & remove date & sender from messageObj
                    HashMap<String, Object> userPostMap = (HashMap<String, Object>) messageObj.clone();
                    userPostMap.remove("sender");
                    userPostMap.remove("date");

                    //now put remaining object as user-post messageObj
                    childUpdates.put(userPostMsgPath, userPostMap);

                    //instantiate add message success & failure listeners
                    DBRef.updateChildren(childUpdates).addOnSuccessListener(successAddMessageListener).addOnFailureListener(failedAddMessageListener);

                    handled = true;

                    //hide keyboard
                    View view =  getActivity().getCurrentFocus();
                    if (view != null)
                    {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return handled;
            }
        });




        //create gesture recognizer for the mListView, on tap keyboard will retreat if inView



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return messengerView;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        //remove the new message event listener
//        FirebaseDatabase.getInstance().getReference().child("privateChat").child(senderUsername)
//                .child(recipeintUsername).child(messagesKey).removeEventListener(adapter.newMsgEventListener);


    }

    private void setupKeyboardGesture()
    {
        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);

                return getActivity().onTouchEvent(event);
            }
        });
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

    private void checkUserArguments()
    {
        //get username from args Bundle
        senderUsername = (String)getArguments().get("username");

        //check if recipeintUsername param is avail
        Object result = getArguments().get("recipeintUsername");

        //if result is null, disable the msgTextField
        if(result == null)
        {
            recipeintUsername = "NO_RECIPEINT_USERNAME";
            msgTextField.setEnabled(false);
            //set empty string as header
            messageThreadTitle.setText("");

        }//else assign result to recipeintUsername
        else{
            recipeintUsername = (String)result;
            //set title for chat thread (as recipeint's username)
            messageThreadTitle.setText(recipeintUsername);
        }

    }





    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            // Check if no view has focus:

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return true;
        }


    }
}
