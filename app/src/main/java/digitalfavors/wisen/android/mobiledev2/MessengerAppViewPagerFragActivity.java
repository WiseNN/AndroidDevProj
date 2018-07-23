package digitalfavors.wisen.android.mobiledev2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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

            //show message to user in variable time frame
            errorReporter = Toast.makeText(getApplicationContext(),"Please sign-out before leaving. Thanks!",Toast.LENGTH_SHORT);
            Handler handler = new Handler();

            //if current item is searchUsersView, allow toast message
            if(mViewPager.getCurrentItem() == 0)
            {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        errorReporter.show();
                    }
                }, 700);
            }else{

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        errorReporter.show();
                    }
                }, 1500);
            }



            FrameLayout searchViewParentLayout = ((FrameLayout) mViewPager.getRootView());
            //get searchUserView from root slot in ViewPager
            RelativeLayout searchUserViewRelLayout = searchViewParentLayout
                    .findViewById(R.id.search_chat_username_relLayout);

            //get signOut button from root slot in viewPager
            Button signOutBtn = searchViewParentLayout
                    .findViewById(R.id.btn_signOut);




            long viewPropAnimDuration = 400;

            //if position is 1, then get messengerView controller
            if(mViewPager.getCurrentItem() == 1)
            {

                //create messengerView
                RelativeLayout messengerView = (RelativeLayout)mViewPager.getChildAt(1);

                //animate messengerView, with signOut bounce on completion, then return
                // to messenger screen
                messengerView.animate().setDuration(viewPropAnimDuration/2).xBy(messengerView.getWidth());


                //set current item to searchView
                mViewPager.setCurrentItem(0);

                //bring messengerView back to original xPosition (although offScreen)
                messengerView.setX(0);

                //immediately move searchScreen offscreen to the left (prepare for animation)
                searchViewParentLayout.setTranslationX(-searchViewParentLayout.getWidth());

                //create Handler, reset screen to original messengerView
                Handler handler1 = new Handler();


                //animate searchScreen onScreen from left-to-right, on completion show signOut hint
                //  animation. Then animate original messengerView back onto screen
                searchViewParentLayout.animate().setDuration(viewPropAnimDuration/2).x(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {



                        //show signOut animation hint
                        animateSignOutHint(viewPropAnimDuration,signOutBtn ,searchUserViewRelLayout);

                                //with delay, animate searchView off screen, at same-time, animate messengerView
                                //  back onScreen

                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //animate searchView off screen
//                                        searchUserViewRelLayout.animate().setDuration(viewPropAnimDuration/2)
//                                                .x(-searchUserViewRelLayout.getWidth());

                                        //set current item as messengerView
                                        mViewPager.setCurrentItem(1);

                                        //reset searchUsersView back to default position (although offScreen)
//                                        searchUserViewRelLayout.setTranslationX(searchUserViewRelLayout.getWidth());

//                                        //immediate move messengerView off screen to the right
                                        messengerView.setTranslationX(messengerView.getWidth());

//                                        //now animate messengerView onto screen from right-to-left
                                        messengerView.animate().setDuration(viewPropAnimDuration/2)
                                                .xBy(-messengerView.getWidth());

                                        //now set permenant xPosition for messengerView
                                        messengerView.setX(0);





                                        }



                                }, viewPropAnimDuration*2);
                    }
                });

            }else{

                //animate showing user how to signOut
                animateSignOutHint(viewPropAnimDuration,signOutBtn, searchUserViewRelLayout);
            }

















        }
    }

    //animate the hint to show a user how to sign out
    private void animateSignOutHint(long viewPropAnimDuration,Button signOutBtn, RelativeLayout searchUserViewRelLayout)
    {
        float slideDownValue = (signOutBtn.getHeight()/2);

        long startAnimDelay = 1000;

        //slide the slignOut button down by half of its height
        signOutBtn.animate().setDuration(viewPropAnimDuration).yBy(slideDownValue).withEndAction(new Runnable() {
            @Override
            public void run() {

                // bounce animate up to original position
                final SpringAnimation springAnimForBtnUp = new SpringAnimation(signOutBtn, DynamicAnimation.TRANSLATION_Y, 0);
                springAnimForBtnUp.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
                springAnimForBtnUp.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
                springAnimForBtnUp.getSpring().setFinalPosition(-signOutBtn.getHeight());
                springAnimForBtnUp.start();

            }
        });

        //slide messenegerView down by half of the signOut button's height
        searchUserViewRelLayout.animate().setDuration(viewPropAnimDuration).yBy(slideDownValue).withEndAction(
                new Runnable() {
                    @Override
                    public void run() {

                        //bounce animate view back to original position
                        final SpringAnimation springAnimForView = new SpringAnimation(searchUserViewRelLayout, DynamicAnimation.TRANSLATION_Y, 0);
                        springAnimForView.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                        springAnimForView.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
                        springAnimForView.getSpring().setFinalPosition(0);
                        springAnimForView.start();

                    }
                });
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