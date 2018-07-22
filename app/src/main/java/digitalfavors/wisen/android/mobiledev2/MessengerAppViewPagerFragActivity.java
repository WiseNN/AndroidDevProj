package digitalfavors.wisen.android.mobiledev2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MessengerAppViewPagerFragActivity extends FragmentActivity
{
    //number of pager in viewPger
    int numOfPages = 0;
    Toast errorReporter;

    //viewPager reference
    ViewPager mViewPager;

    //pager adapter (holder)
    MessengerAppViewPagerAdapter mPagerAdapter;

    //create BroadcastReceiver, reload the MessengerView Fragment when new search has been made
    private BroadcastReceiver mPagerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //log received broadcast
            Log.d("mPagerReceiver", "Received Message from Messenger");

            //get the fragment list from the manager
            ArrayList<Fragment> fragList = mPagerAdapter.getFragmentArrayList();

            //get username from old messengerView Fragment
            String username = ((MessangerView)fragList.get(fragList.size()-1)).senderUsername;

            //get recipeint username from intent
            String recipeintUsername = intent.getStringExtra("recipeintUsername");

            //create Bundle to pass username and recipeintUsername to new Fragment
            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("recipeintUsername", recipeintUsername);

            //remove the last fragment from list
            fragList.remove(fragList.size()-1);

            //re-create the same fragment
            MessangerView messangerView = new MessangerView();

            //pass the new args to the new Fragment
            messangerView.setArguments(args);

            //now add this new messengerView Fragment to the list
            fragList.add(messangerView);

            //now re-add this fragment to the list
//            mPagerAdapter.setFragmentArrayList((ArrayList<Fragment>) fragList);

            //reload the data
//            mPagerAdapter.notifyDataSetChanged();

            //create and set adapter on viewPager
            mPagerAdapter = new MessengerAppViewPagerAdapter(getSupportFragmentManager(), fragList);
            mViewPager.setAdapter(mPagerAdapter);


            //force mViewPager to show messengerView
            mViewPager.setCurrentItem(fragList.size()-1);



        }
    };

    @Override
    public void onBackPressed() {

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            super.onBackPressed();
        }else{

            if(errorReporter != null)
            {
                errorReporter.cancel();
            }
            errorReporter = Toast.makeText(getApplicationContext(),"Please sign-out before leaving. Thanks!",Toast.LENGTH_SHORT);
            errorReporter.show();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.messnger_app_view_pager);

        ArrayList<Fragment> fragmentList = new ArrayList<>();

        //create fragments
        SearchUsers searchUsersFrag = new SearchUsers();
        MessangerView messangerViewFrag = new MessangerView();

        //attach arguments from intent bundle
        searchUsersFrag.setArguments(getIntent().getExtras());
        messangerViewFrag.setArguments(getIntent().getExtras());

        //add fragments to list
        fragmentList.add(searchUsersFrag);
        fragmentList.add(messangerViewFrag);

        //get viewPager
        mViewPager = findViewById(R.id.messenge_app_view_pager);

        //create and set adapter on viewPager
        mPagerAdapter = new MessengerAppViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mPagerAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //register and associate intentFilter with mMessenger broadcast receiver
        IntentFilter iff = new IntentFilter(MyIntentService.LOAD_CONVO_ACTION);
        LocalBroadcastManager.getInstance(MessengerAppViewPagerFragActivity.this).registerReceiver(mPagerReceiver, iff);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister broadcast receiver
        LocalBroadcastManager.getInstance(MessengerAppViewPagerFragActivity.this).unregisterReceiver(mPagerReceiver);
    }
}