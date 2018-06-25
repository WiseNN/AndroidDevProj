package digitalfavors.wisen.android.mobiledev2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends AppCompatActivity {

    int myCheckedId;
    @BindView(R.id.dialog_radio_group)
    RadioGroup dialogRadioGroup;

    @OnClick(R.id.submit_button)
    public void submitButtonHandler(View view)
    {
        switch (myCheckedId)
        {
            case R.id.radio_button_1 :
            {
                final CustomDialog.ICustomDialogListener listener
                        = new CustomDialog.ICustomDialogListener() {
                    @Override
                    public void onClickListner(String msg) {


                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                };
                CustomDialog dialog = new CustomDialog(DialogActivity.this  ,listener);
                dialog.show();
            }

                break;

                default:
                    Toast.makeText(this, "No dialog Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);

        dialogRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myCheckedId = checkedId;

                switch (checkedId)
                {
                    case R.id.radio_button_1  :

                       myCheckedId = checkedId;

                        break;
                }
            }
        });
    }
}
