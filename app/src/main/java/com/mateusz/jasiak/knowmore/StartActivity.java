package com.mateusz.jasiak.knowmore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mateusz.jasiak.knowmore.databinding.ActivityStartBinding;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    //Login
    private Boolean checkFirstLogin = true;
    //----------------------------------------------------------------------------------------------
    //Google login
    ActivityStartBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    //----------------------------------------------------------------------------------------------
    //Facebook login
    /*private CallbackManager callbackManager;
    private LoginButton loginButton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //------------------------------------------------------------------------------------------
        //Google login
        binding = ActivityStartBinding.inflate(getLayoutInflater());
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

        if (loadData().equals("true")) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            StartActivity.this.startActivity(intent);
        }

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
                String personPhoto = "";
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                if (!(acct.getPhotoUrl() == null)) {
                    personPhoto = Objects.requireNonNull(acct.getPhotoUrl()).getEncodedPath();
                }


                //Wysyłanie na API
                if (!postPlayerAfterTheFirstLogin("uuid", personId, personGivenName, personFamilyName, personName, personPhoto)) {

                }

                //Przechodzenie do MainActivity
                saveData("true");
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(intent);
            }
        } catch (ApiException e) {
            Log.w("GOOGLE ERROR", e.getMessage());
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

    //----------------------------------------------------------------------------------------------
    //Retrofit łączenie. Docelowo zamienić adres z localhost na domenę. Wyodrębnić na funkcję itp.
    /*void getPlayers(String id, String idSocialMedia, String firstName, String surname, String name) {

        nameWithAPI = findViewById(R.id.nameWithAPI); //TODO: Usunąć.

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
                        nameWithAPI.setText(playersDataAPIs.getName() + '\n'); //TODO: Usunąć.
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlayersDataAPI>> call, Throwable t) {

            }
        });
    }*/

    Boolean postPlayerAfterTheFirstLogin(String id, String idSocialMedia, String firstName, String surname, String name, String personPhoto) {
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
                    }
                    if (temp == 0) {
                        postPlayer(id, idSocialMedia, firstName, surname, name, personPhoto);
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

    void postPlayer(String id, String idSocialMedia, String firstName, String surname, String name, String personPhoto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonKnowMoreAPI jsonKnowMoreAPI = retrofit.create(JsonKnowMoreAPI.class);

        PlayersDataAPI playersDataAPI = new PlayersDataAPI(id, idSocialMedia, firstName, surname, name, personPhoto);
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

    private void saveData(String logged) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGGED_KEY", logged);
        editor.apply();
    }

    private String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String logged = sharedPreferences.getString("LOGGED_KEY", "false");

        return logged;
    }
}