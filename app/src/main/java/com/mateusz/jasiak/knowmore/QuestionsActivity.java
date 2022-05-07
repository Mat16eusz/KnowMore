package com.mateusz.jasiak.knowmore;

import static com.mateusz.jasiak.knowmore.APIClient.getClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity {

    private ArrayList<Integer> positions = new ArrayList<>(); //TODO: Pobierać dla konkretnej pary z bazy danych
    private ArrayList<Integer> myMarkedAnswer = new ArrayList<>();
    private int question = 0;

    private String myIdSocialMedia;
    private String friendIdSocialMedia;

    private TextView questionView;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;

    private ArrayList<Integer> idQuestions = new ArrayList<>();
    private ArrayList<String> questionsEN = new ArrayList<>();
    private ArrayList<String> answerOneEN = new ArrayList<>();
    private ArrayList<String> answerTwoEN = new ArrayList<>();
    private ArrayList<String> answerThreeEN = new ArrayList<>();
    private ArrayList<String> answerFourEN = new ArrayList<>();
    private ArrayList<String> questionsPL = new ArrayList<>();
    private ArrayList<String> answerOnePL = new ArrayList<>();
    private ArrayList<String> answerTwoPL = new ArrayList<>();
    private ArrayList<String> answerThreePL = new ArrayList<>();
    private ArrayList<String> answerFourPL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Intent intent = getIntent();
        myIdSocialMedia = intent.getStringExtra("KEY_MY_ID_SOCIAL_MEDIA");
        friendIdSocialMedia = intent.getStringExtra("KEY_FRIEND_ID_SOCIAL_MEDIA");
        idQuestions = intent.getIntegerArrayListExtra("KEY_FRIEND_ID_SOCIAL_ID_QUESTIONS");
        questionsEN = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_QUESTIONS_EN");
        answerOneEN = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_ONE_EN");
        answerTwoEN = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_TWO_EN");
        answerThreeEN = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_THREE_EN");
        answerFourEN = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_FOUR_EN");
        questionsPL = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_QUESTIONS_PL");
        answerOnePL = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_ONE_PL");
        answerTwoPL = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_TWO_PL");
        answerThreePL = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_THREE_PL");
        answerFourPL = intent.getStringArrayListExtra("KEY_FRIEND_ID_SOCIAL_ANSWER_FOUR_PL");

        questionView = findViewById(R.id.questionView);
        answerButton1 = findViewById(R.id.answerButton1);
        answerButton2 = findViewById(R.id.answerButton2);
        answerButton3 = findViewById(R.id.answerButton3);
        answerButton4 = findViewById(R.id.answerButton4);

        positions.add(getQuestion());
    }

    public void answerButton1(View view) {
        if (question != 2) {
            myMarkedAnswer.add(1);
            question++;
            positions.add(getQuestion());
        } else {
            myMarkedAnswer.add(1);
            getCurrentQuestions();
            Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
            QuestionsActivity.this.startActivity(intent);
        }
    }

    public void answerButton2(View view) {
        if (question != 2) {
            myMarkedAnswer.add(2);
            question++;
            positions.add(getQuestion());
        } else {
            myMarkedAnswer.add(2);
            getCurrentQuestions();
            Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
            QuestionsActivity.this.startActivity(intent);
        }
    }

    public void answerButton3(View view) {
        if (question != 2) {
            myMarkedAnswer.add(3);
            question++;
            positions.add(getQuestion());
        } else {
            myMarkedAnswer.add(3);
            getCurrentQuestions();
            Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
            QuestionsActivity.this.startActivity(intent);
        }
    }

    public void answerButton4(View view) {
        if (question != 2) {
            myMarkedAnswer.add(4);
            question++;
            positions.add(getQuestion());
        } else {
            myMarkedAnswer.add(4);
            getCurrentQuestions();
            Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
            QuestionsActivity.this.startActivity(intent);
        }
    }

    private void getCurrentQuestions() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getCurrentQuestions();
        call.enqueue(new Callback<List<CurrentQuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<CurrentQuestionsAPI>> call, Response<List<CurrentQuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<CurrentQuestionsAPI> currentQuestionsAPI = response.body();
                    int temp = 0;

                    for (CurrentQuestionsAPI currentQuestionsAPIs : currentQuestionsAPI) {
                        if ((currentQuestionsAPIs.getMyIdSocialMedia().equals(myIdSocialMedia) && currentQuestionsAPIs.getFriendIdSocialMedia().equals(friendIdSocialMedia)) || (currentQuestionsAPIs.getMyIdSocialMedia().equals(friendIdSocialMedia) && currentQuestionsAPIs.getFriendIdSocialMedia().equals(myIdSocialMedia))) {
                            temp++;
                        }
                    }
                    if (temp != 0) {
                        // Update
                        Log.d("Test", "Test update");
                    } else {
                        // Post
                        Log.d("Test", "Test post");

                        addFirstCurrentQuestions();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentQuestionsAPI>> call, Throwable t) {

            }
        });
    }

    private int getQuestion() {
        Random random = new Random();
        int position = random.nextInt(idQuestions.size());
        if (question == 1) {
            while (positions.get(0) == position) {
                position = random.nextInt(idQuestions.size());
            }
        }
        if (question == 2) {
            while (positions.get(0) == position || positions.get(1) == position) {
                position = random.nextInt(idQuestions.size());
            }
        }

        if (Locale.getDefault().getLanguage().equals("pl")) {
            questionView.setText(questionsPL.get(position));
            answerButton1.setText(answerOnePL.get(position));
            answerButton2.setText(answerTwoPL.get(position));
            answerButton3.setText(answerThreePL.get(position));
            answerButton4.setText(answerFourPL.get(position));
        } else {
            questionView.setText(questionsEN.get(position));
            answerButton1.setText(answerOneEN.get(position));
            answerButton2.setText(answerTwoEN.get(position));
            answerButton3.setText(answerThreeEN.get(position));
            answerButton4.setText(answerFourEN.get(position));
        }

        return position;
    }

    private void addFirstCurrentQuestions() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        ArrayList<String> myAnswerOneEN = new ArrayList<>();
        myAnswerOneEN.add(answerOneEN.get(positions.get(0)));
        myAnswerOneEN.add(answerTwoEN.get(positions.get(0)));
        myAnswerOneEN.add(answerThreeEN.get(positions.get(0)));
        myAnswerOneEN.add(answerFourEN.get(positions.get(0)));
        ArrayList<String> myAnswerOnePL = new ArrayList<>();
        myAnswerOnePL.add(answerOnePL.get(positions.get(0)));
        myAnswerOnePL.add(answerTwoPL.get(positions.get(0)));
        myAnswerOnePL.add(answerThreePL.get(positions.get(0)));
        myAnswerOnePL.add(answerFourPL.get(positions.get(0)));
        ArrayList<String> myAnswerTwoEN = new ArrayList<>();
        myAnswerTwoEN.add(answerOneEN.get(positions.get(1)));
        myAnswerTwoEN.add(answerTwoEN.get(positions.get(1)));
        myAnswerTwoEN.add(answerThreeEN.get(positions.get(1)));
        myAnswerTwoEN.add(answerFourEN.get(positions.get(1)));
        ArrayList<String> myAnswerTwoPL = new ArrayList<>();
        myAnswerTwoPL.add(answerOnePL.get(positions.get(1)));
        myAnswerTwoPL.add(answerTwoPL.get(positions.get(1)));
        myAnswerTwoPL.add(answerThreePL.get(positions.get(1)));
        myAnswerTwoPL.add(answerFourPL.get(positions.get(1)));
        ArrayList<String> myAnswerThreeEN = new ArrayList<>();
        myAnswerThreeEN.add(answerOneEN.get(positions.get(2)));
        myAnswerThreeEN.add(answerTwoEN.get(positions.get(2)));
        myAnswerThreeEN.add(answerThreeEN.get(positions.get(2)));
        myAnswerThreeEN.add(answerFourEN.get(positions.get(2)));
        ArrayList<String> myAnswerThreePL = new ArrayList<>();
        myAnswerThreePL.add(answerOnePL.get(positions.get(2)));
        myAnswerThreePL.add(answerTwoPL.get(positions.get(2)));
        myAnswerThreePL.add(answerThreePL.get(positions.get(2)));
        myAnswerThreePL.add(answerFourPL.get(positions.get(2)));

        CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(myIdSocialMedia, friendIdSocialMedia, false, true, positions, positions.get(0), questionsEN.get(positions.get(0)), myAnswerOneEN, questionsPL.get(positions.get(0)), myAnswerOnePL, myMarkedAnswer.get(0), positions.get(1), questionsEN.get(positions.get(1)), myAnswerTwoEN, questionsPL.get(positions.get(1)), myAnswerTwoPL, myMarkedAnswer.get(1), positions.get(2), questionsEN.get(positions.get(2)), myAnswerThreeEN, questionsPL.get(positions.get(2)), myAnswerThreePL, myMarkedAnswer.get(2), /*friend*/ null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null); //TODO: Nulle zamienić.
        Call<CurrentQuestionsAPI> call = jsonKnowMoreAPI.addCurrentQuestions(currentQuestionsAPI);
        call.enqueue(new Callback<CurrentQuestionsAPI>() {
            @Override
            public void onResponse(Call<CurrentQuestionsAPI> call, Response<CurrentQuestionsAPI> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                CurrentQuestionsAPI currentQuestionsAPIResponse = response.body();
            }

            @Override
            public void onFailure(Call<CurrentQuestionsAPI> call, Throwable t) {

            }
        });
    }
}
