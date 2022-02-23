package com.mateusz.jasiak.knowmore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
//TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
//import com.facebook.gamingservices.FriendFinderDialog;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mateusz.jasiak.knowmore.databinding.ActivityMainBinding;
import com.mateusz.jasiak.knowmore.databinding.ActivityStartBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    //Google login
    private Button signOut;
    ActivityMainBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    //TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
    //private static final String TAG = "Test";
    //----------------------------------------------------------------------------------------------
    //Retrofit
    //private TextView nameWithAPI; //TODO: Usunąć.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOut = findViewById(R.id.sign_out_button);

        //------------------------------------------------------------------------------------------
        //Google login
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //Retrofit łączenie.
        //getPlayers("1");

        //------------------------------------------------------------------------------------------
        //Przechodzenie do QuestionsActivity
        /*Button questionsActivity = findViewById(R.id.questionsActivity);

        questionsActivity.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);

            MainActivity.this.startActivity(intent);
        });*/
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public void signOut(View view) {
        switch (view.getId()) {
            // ...
            case R.id.sign_out_button:
                signOut();
                Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show();

                //Przechodzenie do StartActivity
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            // ...
        }
    }

    //TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
    /*public void Test(View view) {
        FriendFinderDialog dialog = new FriendFinderDialog(this);
        // if we want to get notified when the dialog is closed
        // we can register a Callback
        dialog.registerCallback(
                this.callbackManager,
                new FacebookCallback<FriendFinderDialog.Result>() {
                    @Override
                    public void onSuccess(FriendFinderDialog.Result friendFinderResult) {
                        Log.e(MainActivity.TAG, "Player Finder Dialog closed");
                    }

                    @Override
                    public void onCancel() {}

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("GamingServicesFBCallback", exception.toString());
                    }
                });
        // open the dialog
        dialog.show();
    }*/
}
