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

    //TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
    //private static final String TAG = "Test";
    //----------------------------------------------------------------------------------------------
    private Button signOut;
    //----------------------------------------------------------------------------------------------
    //Google login
    ActivityMainBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    //----------------------------------------------------------------------------------------------
    //Retrofit
    //private TextView nameWithAPI; //TODO: Usunąć.
    //----------------------------------------------------------------------------------------------
    //Facebook login
    /*private CallbackManager callbackManager;
    private LoginButton loginButton;*/

    private Boolean checkFirstLogin = true; //TODO: Przenieś na górę kodu albo jakoś inaczej to przerobić.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //------------------------------------------------------------------------------------------
        //Google login
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        binding.signInButton.setSize(SignInButton.SIZE_WIDE);

        binding.signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult.launch(signInIntent);
        });

        //------------------------------------------------------------------------------------------
        //Facebook login
        /*loginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setPermissions(Arrays.asList("user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Demo", "Login Successful!");
            }

            @Override
            public void onCancel() {
                Log.d("Demo", "Login canceled.");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("Demo", "Login error.");
            }
        });*/

        //Retrofit łączenie.
        //getPlayers("1");

        //------------------------------------------------------------------------------------------
        //Przechodzenie do QuestionsActivity
        /*Button twoActivity = findViewById(R.id.twoActivity);

        twoActivity.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);

            MainActivity.this.startActivity(intent);
        });*/
    }

    //----------------------------------------------------------------------------------------------
    //Google login
    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                }
            }
    );

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                signOut = findViewById(R.id.sign_out_button);
                binding.signInButton.setVisibility(View.INVISIBLE);
                signOut.setVisibility(View.VISIBLE);


                Toast.makeText(this, R.string.logged_in, Toast.LENGTH_SHORT).show();

                //Wysyłanie na API
                if (!postPlayerAfterTheFirstLogin("uuid", personId, personGivenName, personFamilyName, personName)) {

                }
            }
        } catch (ApiException e) {
            Log.w("GOOGLE ERROR", e.getMessage());
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.signInButton.setVisibility(View.VISIBLE);
                        signOut.setVisibility(View.INVISIBLE);
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
                break;
            // ...
        }
    }

    //----------------------------------------------------------------------------------------------
    //Facebook login
    //TODO: jakoś inaczej te zmienne.
    /*String firstName;
    String lastName;
    String id;
    String name;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                try {
                    Log.d("Demo", jsonObject.toString());
                    name = jsonObject.getString("name");
                    firstName = jsonObject.getString("first_name"); // Nic
                    lastName = jsonObject.getString("last_name"); // Nic
                    id = jsonObject.getString("id"); // Nic
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Wysyłanie na API
                //TODO: wywala bo nie ma id.
                if (!postPlayerAfterTheFirstLogin("uuid", id, firstName, lastName, name)) {

                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "gender, name, id, first_name, last_name");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }*/

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

    //----------------------------------------------------------------------------------------------
    //Retrofit łączenie. Docelowo zamienić adres z localhost na domenę. Wyodrębnić na funkcję itp.
    Boolean postPlayerAfterTheFirstLogin(String id, String idSocialMedia, String firstName, String surname, String name) {

        //nameWithAPI = findViewById(R.id.nameWithAPI); //TODO: Usunąć.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonKnowMoreAPI jsonKnowMoreAPI = retrofit.create(JsonKnowMoreAPI.class);

        Call<List<PlayersDataAPI>> call = jsonKnowMoreAPI.getPlayersData();
        call.enqueue(new Callback<List<PlayersDataAPI>>() {
            @Override
            public void onResponse(Call<List<PlayersDataAPI>> call, Response<List<PlayersDataAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<PlayersDataAPI> playersDataAPI = response.body();
                    int temp = 0;

                    for (PlayersDataAPI playersDataAPIs : playersDataAPI) {
                        if (playersDataAPIs.getIdSocialMedia().equals(idSocialMedia)) {
                            temp++;
                        }
                        //nameWithAPI.setText(playersDataAPIs.getFirstName() + '\n'); //TODO: Usunąć.
                    }
                    if (temp == 0) {
                        postPlayer(id, idSocialMedia, firstName, surname, name);
                        checkFirstLogin = false;
                    } else {
                        checkFirstLogin = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlayersDataAPI>> call, Throwable t) {

            }
        });

        return checkFirstLogin;
    }

    /*void getPlayers(String id, String idSocialMedia, String name, String surname, String name) {

        nameWithAPI = findViewById(R.id.nameWithAPI);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonKnowMoreAPI jsonKnowMoreAPI = retrofit.create(JsonKnowMoreAPI.class);

        Call<List<PlayersDataAPI>> call = jsonKnowMoreAPI.getPlayersData();
        call.enqueue(new Callback<List<PlayersDataAPI>>() {
            @Override
            public void onResponse(Call<List<PlayersDataAPI>> call, Response<List<PlayersDataAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<PlayersDataAPI> playersDataAPI = response.body();

                    for (PlayersDataAPI playersDataAPIs : playersDataAPI) {
                        nameWithAPI.setText(playersDataAPIs.getName() + '\n');
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlayersDataAPI>> call, Throwable t) {

            }
        });
    }*/

    void postPlayer(String id, String idSocialMedia, String firstName, String surname, String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonKnowMoreAPI jsonKnowMoreAPI = retrofit.create(JsonKnowMoreAPI.class);

        PlayersDataAPI playersDataAPI = new PlayersDataAPI(id, idSocialMedia, firstName, surname, name);
        Call<PlayersDataAPI> call = jsonKnowMoreAPI.addPlayer(playersDataAPI);
        call.enqueue(new Callback<PlayersDataAPI>() {
            @Override
            public void onResponse(Call<PlayersDataAPI> call, Response<PlayersDataAPI> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                PlayersDataAPI playersDataAPIResponse = response.body();
            }

            @Override
            public void onFailure(Call<PlayersDataAPI> call, Throwable t) {

            }
        });
    }
}
