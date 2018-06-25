package digitalfavors.wisen.android.mobiledev2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomDialog extends Dialog {

    private ICustomDialogListener listener;

    public CustomDialog(@NonNull Context context, ICustomDialogListener listener) {
        super(context);

        this.listener = listener;

    }

    public interface ICustomDialogListener{

        public void onClickListner(String msg);
    }


    @OnClick(R.id.ok_button)
    public void okButtonHandler()
    {
        listener.onClickListner("OK");
        dismiss();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        ButterKnife.bind(this);
    }
}
