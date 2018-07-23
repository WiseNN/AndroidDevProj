package digitalfavors.wisen.android.mobiledev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateAccountDialog extends DialogFragment {


    TextView loginTextView;
    EditText usernameEditTextView;
    EditText passwordEditTextView;
    Button createAccountBtn;
    Dialog createAccountDialog;
    Toast errorReporter;
    ProgressBar mProgressBar;
    RadioGroup signUpRadioGroup;
    int checkedRadioBtnId;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //inflate dialogLayout
        FrameLayout dialogLayout = (FrameLayout) inflater.inflate(R.layout.create_account,container , false);

        //get UI elements
        loginTextView = dialogLayout.findViewById(R.id.tv_login);
        usernameEditTextView = dialogLayout.findViewById(R.id.edit_text_createAccount_username);
        passwordEditTextView = dialogLayout.findViewById(R.id.edit_text_createAccount_password);
        createAccountBtn = dialogLayout.findViewById(R.id.btn_signIn);
        mProgressBar = dialogLayout.findViewById(R.id.pBar);
        signUpRadioGroup = dialogLayout.findViewById(R.id.signUp_radio_group);


        //dismiss dialog on click
        loginTextView.setOnClickListener((view) -> this.dismiss());
        //sign new user up on btn click
        createAccountBtn.setOnClickListener((view) -> createAccountHandler());
        //track ID of if user should be automatically logged in or not
        signUpRadioGroup.setOnCheckedChangeListener((radioGroup,checkedId) -> checkedRadioBtnId = checkedId);
        //set radio button to signIn for default value
        checkedRadioBtnId = R.id.signIn_radio_btn;

        return dialogLayout;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        createAccountDialog = super.onCreateDialog(savedInstanceState);
        createAccountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return createAccountDialog;
    }

    private void createAccountHandler()
    {

        mProgressBar.setVisibility(View.VISIBLE);
        final String username = usernameEditTextView.getText().toString();
        final String password = passwordEditTextView.getText().toString();

        //if either field is empty, ignore submission
        if(username.equals("") || password.equals(""))
        {
            return;
        }

        String usernameAsEmail = username+"@quickChat.com";

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usernameAsEmail,password )
                .addOnCompleteListener((task) -> {

                    //hide progress bar
                    mProgressBar.setVisibility(View.GONE);
                    //retreat keyboard
                    View view = getView();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    //then check if task is successful
                    if(task.isSuccessful())
                    {

                        FirebaseDatabase.getInstance().getReference()
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                HashMap<String, Object> map = new HashMap<>();

                                //create key for users
                                String key = dataSnapshot.getRef().push().getKey();

                                map.put("privateChat/_"+username, 1);
                                map.put("users/"+key,username);
                                dataSnapshot.getRef().updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        //Log fireAuth response
                                        Log.d("firebase-auth", "createAccount Successful!");

                                        //clear editTextField on success
                                        usernameEditTextView.setText("");
                                        passwordEditTextView.setText("");


                                        //show account created notification
                                        if(errorReporter != null)
                                        {
                                            errorReporter.cancel();
                                        }
                                        errorReporter = Toast.makeText(getActivity(),username+", your account has been created!",Toast.LENGTH_SHORT);
                                        errorReporter.show();

                                        //dismiss dialog
                                        dismiss();

                                        //if auto login is check, log new user in
                                        if(R.id.signIn_radio_btn == checkedRadioBtnId)
                                        {
                                            Intent loginIntent= new Intent(getActivity(), MyIntentService.class);

                                            //set action for intent
                                            loginIntent.setAction(MyIntentService.LOGIN_ACTION);

                                            //create bundle to store username and password
                                            Bundle userCredintals = new Bundle();
                                            userCredintals.putString("username", username);
                                            userCredintals.putString("password", password);

                                            //put the bundle inside of intent
                                            loginIntent.putExtras(userCredintals);

                                            loginIntent.putExtra("check", "isSent!");

                                            //broadcast to receiver
                                            LoginActivity loginActivity = (LoginActivity) getActivity();
                                            loginActivity.startService(loginIntent);

                                        }


                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }//else print the reason for account not created or exception
                    else{

                        //Log fireAuth response
                        Log.d("firebase-auth", "createAccount Failed!");
                        String fireResult;

                        //check if exception exists
                        if(task.getException() != null)
                        {
                            //if yes, print exception in a toast, and retreat the keyboard
                            fireResult  = task.getException().getMessage();
                        }else{
                            fireResult = task.getResult().toString();
                        }
                        errorReporter = Toast.makeText(getActivity(), fireResult, Toast.LENGTH_LONG);
                        errorReporter.show();
                    }

                });

    }



    @Override
    public void onStart() {
        super.onStart();

//
//        this.getView().setTranslationY(900);
//
//
//        //Create spring animation, slide up bounce
//        SpringAnimation springAnimation = new SpringAnimation(this.getView(), DynamicAnimation.TRANSLATION_Y);
//        // create a spring with desired parameters
//        SpringForce spring = new SpringForce();
//        spring.setFinalPosition(0); // can also be passed directly in the constructor
//        spring.setStiffness(SpringForce.STIFFNESS_LOW); // optional, default is STIFFNESS_MEDIUM
//        spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);// optional, default is DAMPING_RATIO_MEDIUM_BOUNCY
//
//        // set your animation's spring
//        springAnimation.setSpring(spring);
//        springAnimation.start();




    }


}
