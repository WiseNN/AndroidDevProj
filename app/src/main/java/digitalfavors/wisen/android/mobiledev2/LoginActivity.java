package digitalfavors.wisen.android.mobiledev2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends Activity
{
    TextView createAccountTextView;
    EditText usernameTextField;
    EditText passwordTextField;
    Toast errorReporterToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.login_activity);


        //create textView and attach create account click listener
        createAccountTextView = findViewById(R.id.tv_create_account);
        createAccountTextView.setOnClickListener((view) -> loadCreateAccountDialog());

        //create username edit textField and create onTextChange handler
        usernameTextField = findViewById(R.id.edit_text_username);
        usernameTextField.addTextChangedListener(new UsernameTextListener());

        //create password edit textField and create onTextChange handler
        passwordTextField = findViewById(R.id.edit_text_username);
        passwordTextField.addTextChangedListener(new PasswordTextListener());

        //set the on edit keyboard listener (username & password field), listen for enter key
        usernameTextField.setOnEditorActionListener(new UsernameTextListener());
        passwordTextField.setOnEditorActionListener(new PasswordTextListener());


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



    }



    public void loadCreateAccountDialog()
    {
        CreateAccountDialog createActDialog = new CreateAccountDialog();

        createActDialog.show(getFragmentManager(), "CreateAccountDialog");

    }
    public boolean usernameEditorHandler(TextView textView,int actionId,KeyEvent event)
    {

        Log.d("in", event.toString());
        if(event!= null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
        {
            errorReporterToast = Toast.makeText(LoginActivity.this, "called Me!", Toast.LENGTH_SHORT);
            errorReporterToast.show();
        }
        return true;
    }


    public boolean passwordEditorHandler(TextView textView,int actionId,KeyEvent event)
    {
        return true;
    }
}

