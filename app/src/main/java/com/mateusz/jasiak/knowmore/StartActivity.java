package com.mateusz.jasiak.knowmore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private String personId; //Edit, było w funkcji zainicjowane lokalnie.
    //----------------------------------------------------------------------------------------------
    //Firebase
    private String token;

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
        //Firebase
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w("Token FCM error", "Fetching FCM registration token failed", task.getException());

                        return;
                    }

                    token = task.getResult();
                    Log.v("Token FCM", token, task.getException());
                }
            });
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
                personId = acct.getId();
                if (!(acct.getPhotoUrl() == null)) {
                    personPhoto = Objects.requireNonNull(acct.getPhotoUrl()).getEncodedPath();
                }

                //Wysyłanie na API
                if (!postPlayerAfterTheFirstLogin("uuid", personId, personGivenName, personFamilyName, personName, personPhoto, token)) {

                }

                //Update tokenu
                //TODO: Optymalizacja -> WAŻNE! musi być przed wywołaniem saveData("", "");
                //      -> if () jeżeli token z pamięci jest inny niż aktualny (pierwsze logowanie chyba zabezpieczenie przed nullem).
                updateToken(personId, token);

                //Przechodzenie do MainActivity
                saveData("true", token);
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(intent);
            }
        } catch (ApiException e) {
            Log.w("GOOGLE ERROR", e.getMessage());
        }
    }

    Boolean postPlayerAfterTheFirstLogin(String id, String idSocialMedia, String firstName, String surname, String name, String personPhoto, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JsonKnowMoreAPI.BASE_URL)
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
                        postPlayer(id, idSocialMedia, firstName, surname, name, personPhoto, token);
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

    void postPlayer(String id, String idSocialMedia, String firstName, String surname, String name, String personPhoto, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JsonKnowMoreAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonKnowMoreAPI jsonKnowMoreAPI = retrofit.create(JsonKnowMoreAPI.class);

        PlayersDataAPI playersDataAPI = new PlayersDataAPI(id, idSocialMedia, firstName, surname, name, personPhoto, token);
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

    private void saveData(String logged, String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGGED_KEY", logged);
        editor.putString("TOKEN_KEY", token);
        editor.putString("ID_SOCIAL_MEDIA_KEY", personId);
        editor.apply();
    }

    private String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String logged = sharedPreferences.getString("LOGGED_KEY", "false");

        return logged;
    }

    public void updateToken(String idSocialMedia, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JsonKnowMoreAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonKnowMoreAPI jsonKnowMoreAPI = retrofit.create(JsonKnowMoreAPI.class);

        PlayersDataAPI playersDataAPI = new PlayersDataAPI(idSocialMedia, token);
        Call<PlayersDataAPI> call = jsonKnowMoreAPI.putPlayer(idSocialMedia, playersDataAPI);
        call.enqueue(new Callback<PlayersDataAPI>() {
            @Override
            public void onResponse(Call<PlayersDataAPI> call, Response<PlayersDataAPI> response) {

            }

            @Override
            public void onFailure(Call<PlayersDataAPI> call, Throwable t) {

            }
        });
    }
}
