package digitalfavors.wisen.android.mobiledev2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchUsers extends Fragment {

    RelativeLayout chatSearchLayout;
    EditText searchUserEditTextField;
    Button searchChatUserBtn;
    String privateChatKey = "privateChat";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //get search chat screen layout
        chatSearchLayout = (RelativeLayout) inflater.inflate(R.layout.search_chat_user,container,false);

        //get search edit text field
        searchUserEditTextField = chatSearchLayout.findViewById(R.id.search_username_edit_text_box);

        //get search user button
        searchChatUserBtn = chatSearchLayout.findViewById(R.id.btn_search_username);

        //show chat layout
        return chatSearchLayout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchChatUserBtn.setOnClickListener((view) -> searchRecipeintChatUser());

    }

    private void searchRecipeintChatUser()
    {
        String recipeintUsername = searchUserEditTextField.getText().toString();

        //if textField is empty, ignore submission
        if(recipeintUsername.equals(""))
        {return;}


        FirebaseDatabase.getInstance().getReference(privateChatKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Object doesRecipeintExist = dataSnapshot.child("_"+recipeintUsername).getValue();

                        //report to UI that user does not exist
                        if(doesRecipeintExist == null)
                        {
                            //Create toast and tell UI that user does not exist
                            String response = "Sorry, the username"+recipeintUsername+" does not exist";
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                        }else{
                            //cast value to int to see if user has authorization to send messages
                            // 0 - means no (cannot receive messages)
                            // 1 - means yes (can receive messages)
                            long isRecipeintAvail = (Long) doesRecipeintExist;

                            if(isRecipeintAvail == 1) {
                                //load messenger view, pass recipeintUsername
                                loadMessengerView(recipeintUsername);
                            }
                            else if(isRecipeintAvail == 2)
                            {
                                //change to TextView prompt on screen
                                String response = "Sorry, this user cannot chat at the moment";
                                Toast.makeText(getContext(), response,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }


    //called to load a chat between the user & recipeint
    private void loadMessengerView(String recipeintUsername)
    {

        //create new intent
        Intent intent = new Intent(getContext(), MyIntentService.class);
        //set load messenger action (convo)
        intent.setAction(MyIntentService.LOAD_CONVO_ACTION);
        //add recipeint username to intent
        intent.putExtra("recipeintUsername", recipeintUsername);
        //start the intent service
        getActivity().startService(intent);

    }
}

