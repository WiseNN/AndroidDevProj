package digitalfavors.wisen.android.mobiledev2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedHashMap;

public class DemoFragment extends android.support.v4.app.Fragment
{
    ListView myListView;
    LinkedHashMap<String,String> dataToIntentClassMap;
    String NOTIF_CHANNEL_ID = "3284";


    int DEMO_FRAG_RESOURCE_ID = 7836441;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        String[] classList =  {
                "digitalfavors.wisen.android.mobiledev2.NewActivity",
                "digitalfavors.wisen.android.mobiledev2.MyViewPager",
                "digitalfavors.wisen.android.mobiledev2.MyViewPager",
                "","","",
                "digitalfavors.wisen.android.mobiledev2.MessangerView",
                "digitalfavors.wisen.android.mobiledev2.AdvancedViewPagerActivity",
                "digitalfavors.wisen.android.mobiledev2.RadioButtonActivity",
                "digitalfavors.wisen.android.mobiledev2.CheckBoxActivity",
                "digitalfavors.wisen.android.mobiledev2.DialogActivity",
                "digitalfavors.wisen.android.mobiledev2.TimerActivity",
                "digitalfavors.wisen.android.mobiledev2.AnimationActivity",
                "digitalfavors.wisen.android.mobiledev2.DragActivity",
                "digitalfavors.wisen.android.mobiledev2.ViewSlider",
                "digitalfavors.wisen.android.mobiledev2.LoginActivity"
        };
        String[] dataList = {
                "A","ViewPager","C","Small Notification", "Large Notification", "Pic Notification",
                "Messenger","AdvancedViewPager","Radio Button Activity","Check Box Activity",
                "Dialogs","Timer/Handler Activity","Spring Animation Activity",
                "Drag Activity","ViewSlider","login Activity"

        };

        int size = dataList.length;
         dataToIntentClassMap = new LinkedHashMap<>();

        for(int i=0; i<size;i++)
        {


            //get letter from charCode
            String letter = Character.toString((char)(97+i));

            //add letter to list
            dataToIntentClassMap.put(dataList[i], classList[i]);
        }

        Log.i("List Info ", dataToIntentClassMap.toString());

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        //if container exists, inflate fragment container view, and return
        if(container != null)
        {
            View frameLayoutView = inflater.inflate(R.layout.fragment_demo, container, false);
            myListView = frameLayoutView.findViewById(R.id.list_view);


//            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, stringList);
            CustomListAdapter cc = new CustomListAdapter(this, getActivity().getBaseContext(),DEMO_FRAG_RESOURCE_ID,dataToIntentClassMap, myListView);

//            CustomListAdapter listAdapter = new CustomListAdapter();
            myListView.setAdapter(cc);



            return frameLayoutView;
        }
        //else, return generic superview
        else{

            return  super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    //process all button events without intents
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    int NOTIF_ID = 0;
    void processActionEvent(String cellTitle)
    {

        switch(cellTitle)
        {
            case "Small Notification" : {
                Toast.makeText(getContext().getApplicationContext(), R.string.hello_text, Toast.LENGTH_SHORT).show();

               NOTIF_ID = createSmallNotification(3234);
               break;

            }
            case "Large Notification" : {
                Toast.makeText(getContext().getApplicationContext(), R.string.hello_text, Toast.LENGTH_SHORT).show();

                NOTIF_ID = createLargeNotification(35434);
                break;

            }
            case "Pic Notification" : {
                Toast.makeText(getContext().getApplicationContext(), R.string.hello_text, Toast.LENGTH_SHORT).show();

                NOTIF_ID = createPicNotification(363749);
                break;

            }



        }
    }



    int createSmallNotification(int withID)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext().getApplicationContext(),NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("WiseApp")
                .setContentText("This is your man, from Wise Solutions. DJ HeyWiise! #VibeCityENT")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(withID, mBuilder.build());
        return withID;
    }

    int createLargeNotification(int withID)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext().getApplicationContext(),NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("WiseApp")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Line 1\nLine 2\nLine 3\n" +
                        "Line 4\n Line 5"))
                .setContentText("This is your man, from Wise Solutions. DJ HeyWiise! #VibeCityENT")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(withID, mBuilder.build());
        return withID;
    }

    int createPicNotification(int withID)
    {
        Bitmap bm = BitmapFactory .decodeResource(getResources(),R.drawable.city);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext().getApplicationContext(),NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("WiseApp")
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm))
                .setContentText("This is your man, from Wise Solutions. DJ HeyWiise! #VibeCityENT")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(withID, mBuilder.build());
        return withID;
    }






}
