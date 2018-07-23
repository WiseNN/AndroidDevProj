package digitalfavors.wisen.android.mobiledev2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class SearchUsers extends Fragment {

    FrameLayout chatSearchLayout;
    RelativeLayout searchChatUserRelLayout;
    EditText searchUserEditTextField;
    Button searchChatUserBtn;
    String privateChatKey = "privateChat";
    Button signOutBtn;
    private GestureDetector mDetector;
    private GestureDetectorCompat mDetectorForKeyboard;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        SimpleGestureListener simpleGestureListener = new SimpleGestureListener();
        simpleGestureListener.setListener(new SimpleGestureListener.Listener() {
            @Override
            public void onScrollHorizontal(float dx) {
                Log.i("motion","horizontal = " +dx);
            }

            @Override
            public void onScrollVertical(float dy) {
                Log.i("motion","vertical = " +dy);


                //drag down
                if(dy < -50)
                {
                    slideScreenDown();
                }

                //drag up
                if(dy > 50)
                {
                    slideScreenUp();

                }
            }
        });


        mDetector = new GestureDetector(getActivity(), simpleGestureListener);
    }

    private void slideScreenUp()
    {
        long animDuration = 100;
        signOutBtn.animate().setDuration(animDuration).yBy(-signOutBtn.getHeight());
        searchChatUserRelLayout.animate().setDuration(animDuration).yBy(-signOutBtn.getHeight());
    }
    private void slideScreenDown()
    {
        long animDuration = 170;
        signOutBtn.animate().setDuration(animDuration).yBy(signOutBtn.getHeight());
        searchChatUserRelLayout.animate().setDuration(animDuration).yBy(signOutBtn.getHeight());
    }









    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //get search chat screen layout
        chatSearchLayout = (FrameLayout) inflater.inflate(R.layout.search_chat_user,container,false);

        //get searchContainerLayout View without signOut button by iD
        searchChatUserRelLayout = chatSearchLayout.findViewById(R.id.search_chat_username_relLayout);

        //get search edit text field
        searchUserEditTextField = chatSearchLayout.findViewById(R.id.search_username_edit_text_box);

        searchUserEditTextField.setOnEditorActionListener(new PasswordTextListener(getActivity()));

        //get search user button
        searchChatUserBtn = chatSearchLayout.findViewById(R.id.btn_search_username);

        //get signOut Button
        signOutBtn = chatSearchLayout.findViewById(R.id.btn_signOut);

        //show chat layout
        return chatSearchLayout;
    }

    long starttime = 0;
    //this  posts a message to the main thread from our timertask
    //and updates the textfield
    final Handler h = new Handler(new Handler.Callback() {


        @Override
        public boolean handleMessage(Message msg) {

            getActivity().onBackPressed();

            return false;
        }
    });
    //runs without timer be reposting self
    Handler handler = new Handler();




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        //search for recipeintUser when search button is clicked
        searchChatUserBtn.setOnClickListener((view) -> searchRecipeintChatUser());

        //sign out of firebase, slide screenUp and go back to login screen
        // after a 1/2 of a second
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                slideScreenUp();

                //create handler to give time delay & callback function
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().onBackPressed();
                    }
                }, 500);



            }
        });

        //when user drags screen, signOut button will appear
        searchChatUserRelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });



        //listen for view to pre-draw & layout dimensions, get height & move button offScreen
        signOutBtn.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                if (signOutBtn.getViewTreeObserver().isAlive())
                    signOutBtn.getViewTreeObserver().removeOnPreDrawListener(this);


                //translate signOut button out of view
                signOutBtn.setTranslationY(-signOutBtn.getHeight());
                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void searchRecipeintChatUser()
    {
        String recipeintUsername = searchUserEditTextField.getText().toString();

        //if textField is empty, ignore submission
        if(recipeintUsername.equals(""))
        {return;}


        FirebaseDatabase.getInstance().getReference(privateChatKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Object doesRecipeintExist = dataSnapshot.child("_"+recipeintUsername).getValue();

                        //report to UI that user does not exist
                        if(doesRecipeintExist == null)
                        {
                            //Create toast and tell UI that user does not exist
                            String response = "Sorry, the username"+recipeintUsername+" does not exist";
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                        }else{
                            //cast value to int to see if user has authorization to send messages
                            // 0 - means no (cannot receive messages)
                            // 1 - means yes (can receive messages)
                            long isRecipeintAvail = (Long) doesRecipeintExist;

                            if(isRecipeintAvail == 1) {
                                //load messenger view, pass recipeintUsername
                                loadMessengerView(recipeintUsername);
                            }
                            else if(isRecipeintAvail == 2)
                            {
                                //change to TextView prompt on screen
                                String response = "Sorry, this user cannot chat at the moment";
                                Toast.makeText(getContext(), response,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }


    //called to load a chat between the user & recipeint
    private void loadMessengerView(String recipeintUsername)
    {

        //create new intent
        Intent intent = new Intent(getContext(), MyIntentService.class);
        //set load messenger action (convo)
        intent.setAction(MyIntentService.LOAD_CONVO_ACTION);
        //add recipeint username to intent
        intent.putExtra("recipeintUsername", recipeintUsername);
        //start the intent service
        getActivity().startService(intent);

    }

    private void setupKeyboardGesture()
    {
        mDetectorForKeyboard = new GestureDetectorCompat(getContext(), new MyGestureListener());
        chatSearchLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);

                return getActivity().onTouchEvent(event);
            }
        });
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

