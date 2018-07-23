package digitalfavors.wisen.android.mobiledev2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity
{
    RelativeLayout loginLinearLayout;
    TextView createAccountTextView;
    EditText usernameTextField;
    EditText passwordTextField;
    Toast errorReporterToast;
    Button loginButton;
    private GestureDetectorCompat mDetector;

    //create BroadcastReceiver, getting message from login auth state
    private BroadcastReceiver mLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("msgReceiver", "Received Message from LoginActivity");

            //get username and password from bundle
            Bundle userCredentials = intent.getExtras();

            //set account username and password
            String username = (String)userCredentials.get("username");
            String password = (String)userCredentials.get("password");
            usernameTextField.setText(username);
            passwordTextField.setText(password);

            //attempt to login with new account credentials
            attemptAccountLogin();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorReporterToast = Toast.makeText(LoginActivity.this,"" , Toast.LENGTH_SHORT);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.login_activity);

        //create login view, and set the content view
        loginLinearLayout = findViewById(R.id.quick_chat_login_layout);


        //create textView and attach create account click listener
        createAccountTextView = findViewById(R.id.tv_create_account);
        createAccountTextView.setOnClickListener((view) -> loadCreateAccountDialog());

        //create username edit textField and create onTextChange handler
        usernameTextField = findViewById(R.id.edit_text_login_username);
        usernameTextField.addTextChangedListener(new UsernameTextListener(LoginActivity.this));

        //create password edit textField and create onTextChange handler
        passwordTextField = findViewById(R.id.edit_text_login_password);
        passwordTextField.addTextChangedListener(new PasswordTextListener(LoginActivity.this));

        //set the on edit keyboard listener (username & password field), listen for enter key
        usernameTextField.setOnEditorActionListener(new UsernameTextListener(LoginActivity.this));
        passwordTextField.setOnEditorActionListener(new PasswordTextListener(LoginActivity.this));

        //create login button, and submit username & password textFields
        loginButton = findViewById(R.id.btn_account_login);
        loginButton.setOnClickListener((view) -> attemptAccountLogin());

        setupKeyboardGesture();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register and associate intentFilter with mLogin broadcast receiver
        IntentFilter iff = new IntentFilter(MyIntentService.LOGIN_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLoginReceiver, iff);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLoginReceiver);
    }

    public void attemptAccountLogin()
    {
        final String username = usernameTextField.getText().toString();
        final String password = passwordTextField.getText().toString();
        final String usernameAsEmail = username+"@quickchat.com";

        //if username textField or password textField is clear, ignore event
        if(username.equals("") || password.equals(""))
        {
            return;
        }

        //login to firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usernameAsEmail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if task is successful, pass current user inside of intent
                        // & send to the MessengerView
                        if(task.isSuccessful())
                        {
                            Log.d("firebase-auth", "signIn Successful!");
                            //create an Intent to start the service
                            Intent intent = new Intent(LoginActivity.this, MyIntentService.class);
                            //load the messenger (action meaning)
                            intent.setAction(MyIntentService.LOAD_MESSENGER_INTERFACE_ACTION);
                            //attach username to the intent
                            intent.putExtra("username", username);
                            startService(intent);

                            //if login not successful get message and show with Toast
                        }else{

                            errorReporterToast.cancel();

                            errorReporterToast.setDuration(Toast.LENGTH_SHORT);

                            if(task.getException() != null)
                            {

                                errorReporterToast = Toast.makeText(getApplicationContext(),task.getException().getMessage() ,Toast.LENGTH_SHORT );
                            }else{
                                if(task.getResult() != null)
                                {
                                    errorReporterToast = Toast.makeText(getApplicationContext(),task.getResult().toString() ,Toast.LENGTH_SHORT );
                                }
                            }

                            Log.d("firebase-auth", "signIn Failed!");
                            errorReporterToast.show();
                        }
                    }
                });

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

    private void setupKeyboardGesture()
    {
        mDetector = new GestureDetectorCompat(this, new LoginActivity.MyGestureListener());

        loginLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);

                return onTouchEvent(event);
            }
        });
    }



    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            // Check if no view has focus:

            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return true;
        }


    }
}

