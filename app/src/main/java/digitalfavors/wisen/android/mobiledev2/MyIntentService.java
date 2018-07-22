package digitalfavors.wisen.android.mobiledev2;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyIntentService extends IntentService
{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public static final String LOAD_MESSENGER_INTERFACE_ACTION = "loadMessengerAction";
    public static final String LOAD_CONVO_ACTION = "loadConversation";

    public static final String LOGIN_ACTION = "loginAction";


    public MyIntentService()
    {
        super(MyIntentService.class.getSimpleName());

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO Auto-generated method stub

        String ACTION = null;

        switch(intent.getAction())
        {
            case LOGIN_ACTION:

                Log.d("intent-handler","onHandleIntent called");
                Log.d("intent-handler","sending broadcast for action: "+ACTION);

                //pass login intent straight to LoginActivity
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    break;
            case LOAD_MESSENGER_INTERFACE_ACTION:
                //get current chat user's username from intent and pass to Msngr ViewPager (FragActivity)
                String username = intent.getStringExtra("username");
                loadMessengerAppFragmentActivity(username);
                    break;
            case LOAD_CONVO_ACTION:
                //pass login intent straight to MessengerAppViewPager FragActivity
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    break;

        }


    }

    private void loadMessengerAppFragmentActivity(String forUsername)
    {

        //create bundle and put username in it
        Bundle userCreds = new Bundle();
        userCreds.putString("username", forUsername);

        //create the intent to load our Messenger
        Intent intent = new Intent(getApplicationContext(), MessengerAppViewPagerFragActivity.class);

        //put bundle inside of intent
        intent.putExtras(userCreds);

        //start the MessengerView (FragmentActivity)
        getApplicationContext().startActivity(intent);
    }
}
