package digitalfavors.wisen.android.mobiledev2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    Button demoBtn;
    Button workBtn;
    Button topBtn1;
    ImageButton topBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set content view is used to inflate the layout
        setContentView(R.layout.activity_main);




        demoBtn =  findViewById(R.id.btn_demo);
        workBtn =  findViewById(R.id.btn_work);
        topBtn1 = findViewById(R.id.btn_button1);
        topBtn2 = findViewById(R.id.btn_button2);

        //create on click listener for demo button
        topBtn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.btn_left_text, Toast.LENGTH_LONG).show();

            }
        });

        //create on click listener for work button
        topBtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.btn_right_text, Toast.LENGTH_LONG).show();
            }
        });


        demoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Boolean demoFragIsPresented = (Boolean) demoBtn.getTag();

                //if demo fragment is not visible, show & change isPresented frags
                //for both buttons
                if(demoFragIsPresented == null || !demoFragIsPresented) {
                    demoBtn.setTag(true);
                    workBtn.setTag(false);
                    FragmentManager fragManager = getFragmentManager();
                    FragmentTransaction fragTransaction = fragManager.beginTransaction();
                    fragTransaction.replace(R.id.placeholder_frame_layout, new DemoFragment());
                    fragTransaction.commit();
                }




            }
        });

        workBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Boolean workFragIsPresented = (Boolean) workBtn.getTag();

                //if work fragment is not visible, show & change isPresented frags
                //for both buttons
                if(workFragIsPresented == null || !workFragIsPresented) {

                    workBtn.setTag(true);
                    demoBtn.setTag(false);
                    FragmentManager fragManager = getFragmentManager();
                    FragmentTransaction fragTransaction = fragManager.beginTransaction();
                    fragTransaction.replace(R.id.placeholder_frame_layout, new WorkFragment());
                    fragTransaction.commit();
                }



            }
        });


    }
}

//find views
//set on click listeners for button
