package com.mateusz.jasiak.knowmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateusz.jasiak.knowmore.databinding.ActivityMainBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    //Google login
    ActivityMainBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    //----------------------------------------------------------------------------------------------
    //Rozwijana lista z graczami
    //TODO: Dodać moduł usuwania użytkonika z listy po potwierdzeniu (eliminacja missclicku).
    //TODO: Odśiweżanie listy (pullToRefresh i może coś automatycznego).
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> players = new ArrayList<String>();

    ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViewArrayList;
    ArrayList<String> idSocialMediaList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private PlayerFriendAdapterRecyclerView adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    //----------------------------------------------------------------------------------------------
    //Firebase
    private String myToken; //TODO: Sprawdzić czy lepiej z bazy danych pobierać po ID.
    private String myIdSocialMedia;
    private String myName;
    private String myAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        //------------------------------------------------------------------------------------------
        //Google login
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Toast.makeText(this, R.string.logged_in, Toast.LENGTH_SHORT).show();

        //------------------------------------------------------------------------------------------
        //Rozwijana lista z graczami
        getPlayers(); //TODO: Zrobić odświeżanie.

        //TODO: Po dodaniu ustawić listę na pusty string.
        //TODO: Od znaku # ukryć tekst w prawo.
        autoCompleteTextView = findViewById(R.id.editTextTextPersonName);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, players);

        autoCompleteTextView.setAdapter(arrayAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        buildRecyclerView();

        loadDataNotification();
        if (!loadDataNotification().equals("false")) {
            addPlayerToListFriendsFromNotification(loadDataNotification());

            //TODO: Przekminić czy ma to sens vvv
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("RECEIVED_KEY", "false");
            editor.apply();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
    }

    public void signOut(View view) {
        switch (view.getId()) {
            case R.id.sign_out_button:
                signOut();
                Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show();

                //Przechodzenie do StartActivity
                saveData("false");
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }
    }

    private void saveData(String logged) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGGED_KEY", logged);
        editor.apply();
    }

    //----------------------------------------------------------------------------------------------
    //Retrofit łączenie. Docelowo zamienić adres z localhost na domenę. Wyodrębnić na funkcję itp.
    void getPlayers() {
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

                    for (PlayersDataAPI playersDataAPIs : playersDataAPI) {
                        players.add(playersDataAPIs.getName() + "#" + playersDataAPIs.getIdSocialMedia() + "#" + playersDataAPIs.getPersonPhoto() + "#" + playersDataAPIs.getToken());

                        if (myIdSocialMedia.equals(playersDataAPIs.getIdSocialMedia())) {
                            myName = playersDataAPIs.getName();
                            myAvatar = playersDataAPIs.getPersonPhoto();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlayersDataAPI>> call, Throwable t) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //Rozwijana lista z graczami
    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.playerFriendRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManagerRecyclerView = new LinearLayoutManager(this);
        adapterRecyclerView = new PlayerFriendAdapterRecyclerView(playerFriendRecyclerViewArrayList);

        recyclerView.setLayoutManager(layoutManagerRecyclerView);
        recyclerView.setAdapter(adapterRecyclerView);

        adapterRecyclerView.setOnItemClickListener(new PlayerFriendAdapterRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    //TODO: Zabezpieczyć aby użytkownik nie mógł dodać samego siebie.
    public void addPlayerToListFriends(View view) {
        try {
            String text;
            String idSocialMedia = "";
            String name = "";
            String avatar = "";
            String friendToken = "";
            int i = 0;
            int j, k, textLength;
            int counterIdSocialMedia = 0;

            if (!autoCompleteTextView.getText().toString().equals("")) {
                text = autoCompleteTextView.getText().toString();
                while (!(text.charAt(i) == '#')) { //TODO: Zabezpieczyć jak nie będzie zanku "#"
                    i++;
                }
                name = text.substring(0, i);
                j = i + 1;
                while (!(text.charAt(j) == '#')) { //TODO: Zabezpieczyć jak nie będzie zanku "#"
                    j++;
                }
                idSocialMedia = text.substring(i + 1, j);
                k = j + 1;
                while (!(text.charAt(k) == '#')) { //TODO: Zabezpieczyć jak nie będzie zanku "#"
                    k++;
                }
                avatar = text.substring(j + 1, k);

                textLength = text.length();
                friendToken = text.substring(k + 1, textLength);

                if (playerFriendRecyclerViewArrayList.size() == 0) {
                    insertItem(idSocialMedia, name, avatar);
                    sendNotificationToPlayer(friendToken, myIdSocialMedia, myName, myAvatar);
                } else {
                    for (i = 0; i < playerFriendRecyclerViewArrayList.size(); i++) {
                        if (((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.idSocialMediaRecyclerView)).getText().toString().equals(idSocialMedia)) {
                            counterIdSocialMedia++;
                        }
                    }

                    if (counterIdSocialMedia == 0) {
                        insertItem(idSocialMedia, name, avatar);
                        sendNotificationToPlayer(friendToken, myIdSocialMedia, myName, myAvatar);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Add player to list friends", e.getMessage());
            Toast.makeText(this, R.string.wrong_name, Toast.LENGTH_LONG).show();
        }
    }

    public void addPlayerToListFriendsFromNotification(String body) {
        try {
            String idSocialMedia = "";
            String name = "";
            String avatar = "";
            int i = 0;
            int j, textLength;
            int counterIdSocialMedia = 0;

            while (!(body.charAt(i) == '#')) { //TODO: Zabezpieczyć jak nie będzie zanku "#"
                i++;
            }
            name = body.substring(0, i);
            j = i + 1;
            while (!(body.charAt(j) == '#')) { //TODO: Zabezpieczyć jak nie będzie zanku "#"
                j++;
            }
            idSocialMedia = body.substring(i + 1, j);
            textLength = body.length();
            avatar = body.substring(j + 1, textLength);

            if (playerFriendRecyclerViewArrayList.size() == 0) {
                insertItem(idSocialMedia, name, avatar);
            } else {
                for (i = 0; i < idSocialMediaList.size(); i++) {
                    if (idSocialMediaList.get(i).equals(idSocialMedia)) {
                        counterIdSocialMedia++;
                    }
                    /*if (((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.idSocialMediaRecyclerView)).getText().toString().equals(idSocialMedia)) {
                        counterIdSocialMedia++;
                    }*/
                }

                if (counterIdSocialMedia == 0) {
                    insertItem(idSocialMedia, name, avatar);
                }
            }
        } catch (Exception e) {
            Log.e("Add player to list friends from notification", e.getMessage());
            Toast.makeText(this, R.string.wrong_name, Toast.LENGTH_LONG).show();
        }
    }

    private void insertItem(String idSocialMedia, String name, String avatar) {
        playerFriendRecyclerViewArrayList.add(new PlayerFriendRecyclerView(idSocialMedia, name, avatar));
        adapterRecyclerView.notifyItemInserted(playerFriendRecyclerViewArrayList.size());
        idSocialMediaList.add(idSocialMedia);

        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(playerFriendRecyclerViewArrayList);

        editor.putString("player_friend", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("player_friend", null);
        myToken = sharedPreferences.getString("TOKEN_KEY", null);
        myIdSocialMedia = sharedPreferences.getString("ID_SOCIAL_MEDIA_KEY", null);
        Type type = new TypeToken<ArrayList<PlayerFriendRecyclerView>>() {}.getType();
        playerFriendRecyclerViewArrayList = gson.fromJson(json, type);

        if (playerFriendRecyclerViewArrayList == null) {
            playerFriendRecyclerViewArrayList = new ArrayList<>();
        }
    }

    private void sendNotificationToPlayer(String friendToken, String myIdSocialMedia, String myName, String myAvatar) {
        //TODO: TOKEN na który chcę wysyłać dane zamienić na zmienną i to najprawdopodobniej będzie z bazy danych.
        if (!(myToken.equals(friendToken))) {
            String body = myName + "#" + myIdSocialMedia + "#" + myAvatar;

            FCMNotificationSend FCMNotificationSend = new FCMNotificationSend(friendToken,
            getResources().getString(R.string.invite_notification) + " " + myName, body, getApplicationContext(), MainActivity.this);
            FCMNotificationSend.SendNotifications();
        }
    }

    private String loadDataNotification() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String body = sharedPreferences.getString("RECEIVED_KEY", "false");

        return body;
    }
}
