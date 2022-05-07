package com.mateusz.jasiak.knowmore;

import static com.mateusz.jasiak.knowmore.APIClient.getClient;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //TODO: System usuwania osób z recyclerView: usuwanie z recyclerView z potwierdzeniem czy na pewno chcemy usunąć -> wysłanie do osoby powiadomienia, że dany urzytkownik Cię usunął.
    //TODO: System zapraszania przebudować na zasadzie activity z zaproszeniami i tam akcetacja lub odrzucenie. Gdy akceptacja dodanie do recyclerView.
    //----------------------------------------------------------------------------------------------
    //Google login
    ActivityMainBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    //----------------------------------------------------------------------------------------------
    //Rozwijana lista z graczami
    //TODO: Dodać moduł usuwania użytkonika z listy po potwierdzeniu (eliminacja missclicku).
    //TODO: Odśiweżanie listy (pullToRefresh i może coś automatycznego).
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> players = new ArrayList<>();

    ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViewArrayList;
    ArrayList<String> idSocialMediaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayerFriendAdapterRecyclerView adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    //----------------------------------------------------------------------------------------------
    //Firebase
    private String myToken; //TODO: Sprawdzić czy lepiej z bazy danych pobierać po ID.
    private String myIdSocialMedia;
    private String myName;
    private String myAvatar;
    //----------------------------------------------------------------------------------------------
    //Questions
    public ArrayList<Integer> idQuestions = new ArrayList<>();
    public ArrayList<String> questionsEN = new ArrayList<>();
    public ArrayList<String> answerOneEN = new ArrayList<>();
    public ArrayList<String> answerTwoEN = new ArrayList<>();
    public ArrayList<String> answerThreeEN = new ArrayList<>();
    public ArrayList<String> answerFourEN = new ArrayList<>();
    public ArrayList<String> questionsPL = new ArrayList<>();
    public ArrayList<String> answerOnePL = new ArrayList<>();
    public ArrayList<String> answerTwoPL = new ArrayList<>();
    public ArrayList<String> answerThreePL = new ArrayList<>();
    public ArrayList<String> answerFourPL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData(); //IDSocialMedia + TOKEN + RecyclerView
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
        //Otrzymanie zaproszeń
        getInvitations(); //TODO: Zrobić odświeżanie i zabezpieczenie przed sytuacją, że ktoś usuwa dane aplikacji i wyślę się jeszcze raz.
        getQuestions();

        //TODO: Po dodaniu ustawić listę na pusty string.
        //TODO: Od znaku # ukryć tekst w prawo.
        autoCompleteTextView = findViewById(R.id.editTextTextPersonName);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, players);

        autoCompleteTextView.setAdapter(arrayAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        buildRecyclerView();
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
    public void getPlayers() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

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

        //Przechodzenie do QuestionsActivity
        adapterRecyclerView.setOnItemClickListener(new PlayerFriendAdapterRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String friendIdSocialMedia = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.idSocialMediaRecyclerView)).getText().toString();

                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                intent.putExtra("KEY_MY_ID_SOCIAL_MEDIA", myIdSocialMedia);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_MEDIA", friendIdSocialMedia);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ID_QUESTIONS", idQuestions);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_QUESTIONS_EN", questionsEN);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_ONE_EN", answerOneEN);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_TWO_EN", answerTwoEN);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_THREE_EN", answerThreeEN);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_FOUR_EN", answerFourEN);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_QUESTIONS_PL", questionsPL);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_ONE_PL", answerOnePL);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_TWO_PL", answerTwoPL);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_THREE_PL", answerThreePL);
                intent.putExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_FOUR_PL", answerFourPL);
                MainActivity.this.startActivity(intent);
                //TODO: Sprawdzanie czy drugi użytkownik nie rozpączął gry (pierwsza gra).
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
                    sendNotificationToPlayer(friendToken, idSocialMedia, myIdSocialMedia, myName, myAvatar);
                } else {
                    for (i = 0; i < playerFriendRecyclerViewArrayList.size(); i++) {
                        if (((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.idSocialMediaRecyclerView)).getText().toString().equals(idSocialMedia)) {
                            counterIdSocialMedia++;
                        }
                    }

                    if (counterIdSocialMedia == 0) {
                        insertItem(idSocialMedia, name, avatar);
                        sendNotificationToPlayer(friendToken, idSocialMedia, myIdSocialMedia, myName, myAvatar);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Add player to list friends", e.getMessage());
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

    //----------------------------------------------------------------------------------------------
    //Wysyłanie powiadomień z zaproszeniami i obsługa ich
    private void sendNotificationToPlayer(String friendToken, String friendIdSocialMedia, String myIdSocialMedia, String myName, String myAvatar) {
        //TODO: TOKEN na który chcę wysyłać dane zamienić na zmienną i to najprawdopodobniej będzie z bazy danych.
        if (!(myToken.equals(friendToken))) {
            FCMNotificationSend FCMNotificationSend = new FCMNotificationSend(friendToken,
                    getResources().getString(R.string.invite_notification) + " " + myName, getApplicationContext(), MainActivity.this);
            FCMNotificationSend.SendNotifications();

            postInvitation("uuid", friendIdSocialMedia, myIdSocialMedia, myName, myAvatar);
        }
    }

    public void getInvitations() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<PlayerInvitationAPI>> call = jsonKnowMoreAPI.getPlayerInvitation();
        call.enqueue(new Callback<List<PlayerInvitationAPI>>() {
            @Override
            public void onResponse(Call<List<PlayerInvitationAPI>> call, Response<List<PlayerInvitationAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<PlayerInvitationAPI> playerInvitationAPI = response.body();

                    for (PlayerInvitationAPI playerInvitationAPIs : playerInvitationAPI) {
                        if (myIdSocialMedia.equals(playerInvitationAPIs.getMyIdSocialMedia())) {
                            insertItem(playerInvitationAPIs.getIdSocialMedia(), playerInvitationAPIs.getName(), playerInvitationAPIs.getPersonPhoto());
                            deletePost(playerInvitationAPIs.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlayerInvitationAPI>> call, Throwable t) {

            }
        });
    }

    private void postInvitation(String id, String myIdSocialMedia, String idSocialMedia, String name, String personPhoto) {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        PlayerInvitationAPI playerInvitationAPI = new PlayerInvitationAPI(id, myIdSocialMedia, idSocialMedia, name, personPhoto);
        Call<PlayerInvitationAPI> call = jsonKnowMoreAPI.addPlayerInvite(playerInvitationAPI);
        call.enqueue(new Callback<PlayerInvitationAPI>() {
            @Override
            public void onResponse(Call<PlayerInvitationAPI> call, Response<PlayerInvitationAPI> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                PlayerInvitationAPI playerInvitationAPIResponse = response.body();
            }

            @Override
            public void onFailure(Call<PlayerInvitationAPI> call, Throwable t) {

            }
        });
    }

    private void deletePost(String id) {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<Void> call = jsonKnowMoreAPI.deletePlayerInvite(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void getQuestions() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<QuestionsAPI>> call = jsonKnowMoreAPI.getQuestions();
        call.enqueue(new Callback<List<QuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<QuestionsAPI>> call, Response<List<QuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<QuestionsAPI> questionsAPI = response.body();

                    for (QuestionsAPI questionsAPIs : questionsAPI) {
                        idQuestions.add(questionsAPIs.getIdQuestions());
                        questionsEN.add(questionsAPIs.getQuestionsEN());
                        answerOneEN.add(questionsAPIs.getAnswerOneEN());
                        answerTwoEN.add(questionsAPIs.getAnswerTwoEN());
                        answerThreeEN.add(questionsAPIs.getAnswerThreeEN());
                        answerFourEN.add(questionsAPIs.getAnswerFourEN());
                        questionsPL.add(questionsAPIs.getQuestionsPL());
                        answerOnePL.add(questionsAPIs.getAnswerOnePL());
                        answerTwoPL.add(questionsAPIs.getAnswerTwoPL());
                        answerThreePL.add(questionsAPIs.getAnswerThreePL());
                        answerFourPL.add(questionsAPIs.getAnswerFourPL());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<QuestionsAPI>> call, Throwable t) {

            }
        });
    }
}
