package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RadioButtonActivity extends AppCompatActivity {



    @BindView(R.id.my_radio_group)
    RadioGroup radioGroup;
//    @BindView(R.id.radio_submit_btn)
//    Button radioSubmitBtn;

    //submit button click listener for radio group submit button
    @OnClick(R.id.radio_submit_btn)
    public void submit(View view)
    {

        switch (myCheckedId)
        {
            case R.id.radio_button_1 :
                Toast.makeText(this, "show btn 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_button_2 :
                Toast.makeText(this, "show btn 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_button_3 :
                Toast.makeText(this, "show btn 3", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "chose nothing", Toast.LENGTH_SHORT).show();

        }


    }

    private int myCheckedId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_group_layout);
        ButterKnife.bind(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               myCheckedId = checkedId;
            }
        });
    }
}
