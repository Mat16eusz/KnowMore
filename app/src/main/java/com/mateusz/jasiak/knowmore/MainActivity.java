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
    private RecyclerView recyclerView;
    private PlayerFriendAdapterRecyclerView adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    //----------------------------------------------------------------------------------------------
    //Firebase
    private String myToken; //TODO: Sprawdzić czy lepiej z bazy danych pobierać po ID.
    private ArrayList<String> friendToken = new ArrayList<>();
    private ArrayList<String> idSocialMedia = new ArrayList<>();
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
    //----------------------------------------------------------------------------------------------
    //Questions
    public ArrayList<String> whoseTurn = new ArrayList<>();
    public ArrayList<String> whoseTurnFriendIdSocialMedia = new ArrayList<>();
    public ArrayList<String> initializationGame = new ArrayList<>();
    public ArrayList<Boolean> gameProper = new ArrayList<>();
    public ArrayList<String> myReplies = new ArrayList<>();
    public ArrayList<String> myFriendReplies = new ArrayList<>();
    public ArrayList<String> friendReplies = new ArrayList<>();
    public ArrayList<ArrayList<String>> selectedQuestions = new ArrayList<>();

    private ArrayList<String> myMarkedAnswer = new ArrayList<>();
    private ArrayList<String> myFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<String> friendMarkedAnswer = new ArrayList<>();

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
        getQuestions(); //TODO: Zrobić odświeżanie.
        getWhoIsTurn(); //TODO: Zrobić odświeżanie.
        getCurrentQuestion(); //TODO: Zrobić odświeżanie.
        getMyAndFriendCurrentQuestion(); //TODO: Zrobić odświeżanie.
        getMarkedAnswer(); //TODO: Zrobić odświeżanie.

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
    public void getPlayers() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<PlayersDataAPI>> call = jsonKnowMoreAPI.getPlayersData();
        call.enqueue(new Callback<List<PlayersDataAPI>>() {
            @Override
            public void onResponse(Call<List<PlayersDataAPI>> call, Response<List<PlayersDataAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<PlayersDataAPI> playersDataAPIs = response.body();

                    for (PlayersDataAPI playersDataAPI : playersDataAPIs) {
                        players.add(playersDataAPI.getName() + "#" + playersDataAPI.getIdSocialMedia() + "#" + playersDataAPI.getPersonPhoto() + "#" + playersDataAPI.getToken());

                        friendToken.add(playersDataAPI.getToken());
                        idSocialMedia.add(playersDataAPI.getIdSocialMedia());

                        if (myIdSocialMedia.equals(playersDataAPI.getIdSocialMedia())) {
                            myName = playersDataAPI.getName();
                            myAvatar = playersDataAPI.getPersonPhoto();
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
                String localFriendToken = "";
                boolean orFirstGame = true;
                boolean orYourTurn = false;

                for (int i = 0; i < idSocialMedia.size(); i++) {
                    if (idSocialMedia.get(i).equals(friendIdSocialMedia)) {
                        localFriendToken = friendToken.get(i);
                        break;
                    }
                }

                for (int i = 0; i < whoseTurn.size(); i++) {
                    if (whoseTurn.get(i).equals(friendIdSocialMedia) && whoseTurnFriendIdSocialMedia.get(i).equals(myIdSocialMedia) && initializationGame.get(i).equals(myIdSocialMedia) && gameProper.get(i)) {
                        //TODO: Sprawdzić tego ifa. Czy ma być friendId czy myId.
                        orFirstGame = false;
                        orYourTurn = true;

                        Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                        intent.putExtra("KEY_FRIEND_TOKEN", localFriendToken);

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

                        intent.putExtra("KEY_MY_REPLIES", myReplies);
                        intent.putExtra("KEY_FRIEND_REPLIES", friendReplies);
                        intent.putExtra("KEY_SELECTED_QUESTIONS", selectedQuestions);

                        intent.putExtra("KEY_MY_MARKED_ANSWER", myMarkedAnswer);
                        intent.putExtra("KEY_MY_FRIEND_MARKED_ANSWER", myFriendMarkedAnswer);
                        intent.putExtra("KEY_FRIEND_MARKED_ANSWER", friendMarkedAnswer);
                        MainActivity.this.startActivity(intent); //TODO: Sprawdzić czy nie trzeba wywołać finish(); - wyciek danych?
                        //TODO: Sprawdzanie czy drugi użytkownik nie rozpączął gry (pierwsza gra).
                    }
                    if (whoseTurn.get(i).equals(friendIdSocialMedia) && whoseTurnFriendIdSocialMedia.get(i).equals(myIdSocialMedia) && !initializationGame.get(i).equals(myIdSocialMedia) && gameProper.get(i)) {
                        //TODO: Sprawdzić tego ifa. Czy ma być friendId czy myId.
                        orFirstGame = false;
                        orYourTurn = true;

                        Intent intent = new Intent(MainActivity.this, QuestionsFriendActivity.class);
                        intent.putExtra("KEY_FRIEND_TOKEN", localFriendToken);

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

                        intent.putExtra("KEY_MY_REPLIES", myReplies);
                        intent.putExtra("KEY_FRIEND_REPLIES", friendReplies);
                        intent.putExtra("KEY_SELECTED_QUESTIONS", selectedQuestions);

                        intent.putExtra("KEY_MY_MARKED_ANSWER", myMarkedAnswer);
                        intent.putExtra("KEY_MY_FRIEND_MARKED_ANSWER", myFriendMarkedAnswer);
                        intent.putExtra("KEY_FRIEND_MARKED_ANSWER", friendMarkedAnswer);
                        MainActivity.this.startActivity(intent); //TODO: Sprawdzić czy nie trzeba wywołać finish(); - wyciek danych?
                        //TODO: Sprawdzanie czy drugi użytkownik nie rozpączął gry (pierwsza gra).
                    }
                    if (whoseTurn.get(i).equals(myIdSocialMedia) && whoseTurnFriendIdSocialMedia.get(i).equals(myIdSocialMedia) && !gameProper.get(i)) {
                        orFirstGame = false;
                        orYourTurn = true;

                        Intent intent = new Intent(MainActivity.this, FirstQuestionsFriendActivity.class);
                        intent.putExtra("KEY_FRIEND_TOKEN", localFriendToken);

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

                        intent.putExtra("KEY_MY_FRIEND_REPLIES", myFriendReplies);
                        MainActivity.this.startActivity(intent); //TODO: Sprawdzić czy nie trzeba wywołać finish(); - wyciek danych?
                        //TODO: Sprawdzanie czy drugi użytkownik nie rozpączął gry (pierwsza gra).
                    }
                    if (whoseTurnFriendIdSocialMedia.get(i).equals(friendIdSocialMedia)) {
                        orFirstGame = false;
                    }
                }
                if (orFirstGame) {
                    Intent intent = new Intent(MainActivity.this, FirstQuestionsActivity.class);
                    intent.putExtra("KEY_FRIEND_TOKEN", localFriendToken);

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
                    MainActivity.this.startActivity(intent); //TODO: Sprawdzić czy nie trzeba wywołać finish(); - wyciek danych?
                    //TODO: Sprawdzanie czy drugi użytkownik nie rozpączął gry (pierwsza gra).
                }
                if (!orFirstGame && !orYourTurn) {
                    Toast.makeText(MainActivity.this, R.string.not_your_turn, Toast.LENGTH_LONG).show();
                }
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
                    } else {
                        Toast.makeText(MainActivity.this, name + " " + getText(R.string.player_added), Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Add player to list friends", e.getMessage());
            Toast.makeText(MainActivity.this, R.string.wrong_name, Toast.LENGTH_LONG).show();
        }
    }

    private void insertItem(String idSocialMedia, String name, String avatar) {
        playerFriendRecyclerViewArrayList.add(new PlayerFriendRecyclerView(idSocialMedia, name, avatar));
        adapterRecyclerView.notifyItemInserted(playerFriendRecyclerViewArrayList.size());

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
                    List<PlayerInvitationAPI> playerInvitationAPIs = response.body();

                    for (PlayerInvitationAPI playerInvitationAPI : playerInvitationAPIs) {
                        if (myIdSocialMedia.equals(playerInvitationAPI.getMyIdSocialMedia())) {
                            insertItem(playerInvitationAPI.getIdSocialMedia(), playerInvitationAPI.getName(), playerInvitationAPI.getPersonPhoto());
                            deletePlayerInvite(playerInvitationAPI.getId());
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

    private void deletePlayerInvite(String id) {
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
                    List<QuestionsAPI> questionsAPIs = response.body();

                    for (QuestionsAPI questionsAPI : questionsAPIs) {
                        idQuestions.add(questionsAPI.getIdQuestions());
                        questionsEN.add(questionsAPI.getQuestionsEN());
                        answerOneEN.add(questionsAPI.getAnswerOneEN());
                        answerTwoEN.add(questionsAPI.getAnswerTwoEN());
                        answerThreeEN.add(questionsAPI.getAnswerThreeEN());
                        answerFourEN.add(questionsAPI.getAnswerFourEN());
                        questionsPL.add(questionsAPI.getQuestionsPL());
                        answerOnePL.add(questionsAPI.getAnswerOnePL());
                        answerTwoPL.add(questionsAPI.getAnswerTwoPL());
                        answerThreePL.add(questionsAPI.getAnswerThreePL());
                        answerFourPL.add(questionsAPI.getAnswerFourPL());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<QuestionsAPI>> call, Throwable t) {

            }
        });
    }

    private void getWhoIsTurn() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getWhoIsTurn();
        call.enqueue(new Callback<List<CurrentQuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<CurrentQuestionsAPI>> call, Response<List<CurrentQuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<CurrentQuestionsAPI> currentQuestionsAPIs = response.body();

                    for (CurrentQuestionsAPI currentQuestionsAPI : currentQuestionsAPIs) {
                        whoseTurn.add(currentQuestionsAPI.getFriendIdSocialMedia());
                        whoseTurnFriendIdSocialMedia.add(currentQuestionsAPI.getWhoseTurn());
                        initializationGame.add(currentQuestionsAPI.getInitializationGame());
                        gameProper.add(currentQuestionsAPI.getGameProper());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentQuestionsAPI>> call, Throwable t) {

            }
        });
    }

    private void getCurrentQuestion() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getCurrentQuestion();
        call.enqueue(new Callback<List<CurrentQuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<CurrentQuestionsAPI>> call, Response<List<CurrentQuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<CurrentQuestionsAPI> currentQuestionsAPIs = response.body();

                    for (CurrentQuestionsAPI currentQuestionsAPI : currentQuestionsAPIs) {
                        myFriendReplies.add(currentQuestionsAPI.getId());
                        myFriendReplies.add(currentQuestionsAPI.getMyIdSocialMedia());
                        myFriendReplies.add(currentQuestionsAPI.getFriendIdSocialMedia());
                        myFriendReplies.add(currentQuestionsAPI.getMyIdQuestionOne().toString());
                        myFriendReplies.add(currentQuestionsAPI.getMyMarkedAnswerOne().toString());
                        myFriendReplies.add(currentQuestionsAPI.getMyIdQuestionTwo().toString());
                        myFriendReplies.add(currentQuestionsAPI.getMyMarkedAnswerTwo().toString());
                        myFriendReplies.add(currentQuestionsAPI.getMyIdQuestionThree().toString());
                        myFriendReplies.add(currentQuestionsAPI.getMyMarkedAnswerThree().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentQuestionsAPI>> call, Throwable t) {

            }
        });
    }

    private void getMyAndFriendCurrentQuestion() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getCurrentQuestion();
        call.enqueue(new Callback<List<CurrentQuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<CurrentQuestionsAPI>> call, Response<List<CurrentQuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<CurrentQuestionsAPI> currentQuestionsAPIs = response.body();

                    for (CurrentQuestionsAPI currentQuestionsAPI : currentQuestionsAPIs) {
                        if (currentQuestionsAPI.getMyIdQuestionOne() != null) {
                            myReplies.add(currentQuestionsAPI.getId());
                            myReplies.add(currentQuestionsAPI.getMyIdSocialMedia());
                            myReplies.add(currentQuestionsAPI.getFriendIdSocialMedia());
                            myReplies.add(currentQuestionsAPI.getMyIdQuestionOne().toString());
                            myReplies.add(currentQuestionsAPI.getMyMarkedAnswerOne().toString());
                            myReplies.add(currentQuestionsAPI.getMyIdQuestionTwo().toString());
                            myReplies.add(currentQuestionsAPI.getMyMarkedAnswerTwo().toString());
                            myReplies.add(currentQuestionsAPI.getMyIdQuestionThree().toString());
                            myReplies.add(currentQuestionsAPI.getMyMarkedAnswerThree().toString());
                        }
                        if (currentQuestionsAPI.getFriendIdQuestionOne() != null) {
                            friendReplies.add(currentQuestionsAPI.getId());
                            friendReplies.add(currentQuestionsAPI.getMyIdSocialMedia());
                            friendReplies.add(currentQuestionsAPI.getFriendIdSocialMedia());
                            friendReplies.add(currentQuestionsAPI.getFriendIdQuestionOne().toString());
                            friendReplies.add(currentQuestionsAPI.getFriendMarkedAnswerOne().toString());
                            friendReplies.add(currentQuestionsAPI.getFriendIdQuestionTwo().toString());
                            friendReplies.add(currentQuestionsAPI.getFriendMarkedAnswerTwo().toString());
                            friendReplies.add(currentQuestionsAPI.getFriendIdQuestionThree().toString());
                            friendReplies.add(currentQuestionsAPI.getFriendMarkedAnswerThree().toString());
                        }

                        selectedQuestions.add(currentQuestionsAPI.getSelectedQuestions());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentQuestionsAPI>> call, Throwable t) {

            }
        });
    }

    private void getMarkedAnswer() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getCurrentQuestion();
        call.enqueue(new Callback<List<CurrentQuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<CurrentQuestionsAPI>> call, Response<List<CurrentQuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<CurrentQuestionsAPI> currentQuestionsAPIs = response.body();

                    for (CurrentQuestionsAPI currentQuestionsAPI : currentQuestionsAPIs) {
                        if (currentQuestionsAPI.getMyMarkedAnswerOne() != null) {
                            myMarkedAnswer.add(currentQuestionsAPI.getMyIdSocialMedia());
                            myMarkedAnswer.add(currentQuestionsAPI.getFriendIdSocialMedia());
                            myMarkedAnswer.add(currentQuestionsAPI.getMyMarkedAnswerOne().toString());
                            myMarkedAnswer.add(currentQuestionsAPI.getMyMarkedAnswerTwo().toString());
                            myMarkedAnswer.add(currentQuestionsAPI.getMyMarkedAnswerThree().toString());
                        }
                        if (currentQuestionsAPI.getMyFriendMarkedAnswerOne() != null) {
                            myFriendMarkedAnswer.add(currentQuestionsAPI.getMyIdSocialMedia());
                            myFriendMarkedAnswer.add(currentQuestionsAPI.getFriendIdSocialMedia());
                            myFriendMarkedAnswer.add(currentQuestionsAPI.getMyFriendMarkedAnswerOne().toString());
                            myFriendMarkedAnswer.add(currentQuestionsAPI.getMyFriendMarkedAnswerTwo().toString());
                            myFriendMarkedAnswer.add(currentQuestionsAPI.getMyFriendMarkedAnswerThree().toString());
                        }
                        if (currentQuestionsAPI.getFriendMarkedAnswerOne() != null) {
                            friendMarkedAnswer.add(currentQuestionsAPI.getMyIdSocialMedia());
                            friendMarkedAnswer.add(currentQuestionsAPI.getFriendIdSocialMedia());
                            friendMarkedAnswer.add(currentQuestionsAPI.getFriendMarkedAnswerOne().toString());
                            friendMarkedAnswer.add(currentQuestionsAPI.getFriendMarkedAnswerTwo().toString());
                            friendMarkedAnswer.add(currentQuestionsAPI.getFriendMarkedAnswerThree().toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentQuestionsAPI>> call, Throwable t) {

            }
        });
    }
}
