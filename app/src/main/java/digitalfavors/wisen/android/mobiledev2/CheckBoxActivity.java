package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckBoxActivity extends AppCompatActivity {

    @BindView(R.id.checkbox_1)
    CheckBox checkBox1;

    @BindView(R.id.checkbox_2)
    CheckBox checkBox2;

    @BindView(R.id.checkbox_3)
    CheckBox checkBox3;

    @OnClick(R.id.checkbox_submit_btn)
    public void checkboxHandler(View view)
    {
//        Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();
        String printStr = "";
        for(Map.Entry<Integer, Boolean> entry : checkList.entrySet())
        {
            if(entry.getValue())
            {
                switch (entry.getKey())
                {
                    case R.id.checkbox_1 :
                        printStr += "CheckBox1\n\n";
                       break;

                    case R.id.checkbox_2 :
                        printStr += "CheckBox2\n\n";
                        break;

                    case R.id.checkbox_3 :
                        printStr += "CheckBox3\n\n";
                        break;
                        default:
                            printStr = "No Check Box is selected";
                }
            }
        }

        Toast.makeText(this, printStr, Toast.LENGTH_SHORT).show();
    }

    private HashMap<Integer, Boolean> checkList = new HashMap<>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkbox_layout);
        ButterKnife.bind(this);

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkList.put(buttonView.getId(), isChecked);
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkList.put(buttonView.getId(), isChecked);

            }
        });

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkList.put(buttonView.getId(), isChecked);

            }
        });
    }
}
