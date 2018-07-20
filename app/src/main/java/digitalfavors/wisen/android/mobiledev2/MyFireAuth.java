package digitalfavors.wisen.android.mobiledev2;

import com.google.firebase.auth.FirebaseAuth;

public class MyFireAuth {

    public static void login(String username, String password)
    {

        //create email addresses on the fly when creating accounts
        // only require users to sign in with usernames
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password);

    }
}
