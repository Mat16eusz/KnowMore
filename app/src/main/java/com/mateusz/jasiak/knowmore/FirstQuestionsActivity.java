package com.mateusz.jasiak.knowmore;

import static com.mateusz.jasiak.knowmore.APIClient.getClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstQuestionsActivity extends AppCompatActivity {

    private ArrayList<String> positions = new ArrayList<>(); //TODO: Pobierać dla konkretnej pary z bazy danych
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
        setContentView(R.layout.activity_first_questions);

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

        positions.add(myIdSocialMedia);
        positions.add(friendIdSocialMedia);
        positions.add(getQuestion());
    }

    public void answerButton1(View view) {
        showQuestions(1);
    }

    public void answerButton2(View view) {
        showQuestions(2);
    }

    public void answerButton3(View view) {
        showQuestions(3);
    }

    public void answerButton4(View view) {
        showQuestions(4);
    }

    private void showQuestions(int markedAnswer) {
        if (question != 2) {
            myMarkedAnswer.add(markedAnswer);
            question++;
            positions.add(getQuestion());
        } else {
            myMarkedAnswer.add(markedAnswer);
            addFirstCurrentQuestions();
            Intent intent = new Intent(FirstQuestionsActivity.this, MainActivity.class);
            FirstQuestionsActivity.this.startActivity(intent);
        }
    }

    private String getQuestion() {
        Random random = new Random();
        Integer position;
        position = random.nextInt(idQuestions.size());
        if (question == 1) {
            while (Integer.parseInt(positions.get(2)) == position) {
                position = random.nextInt(idQuestions.size());
            }
        }
        if (question == 2) {
            while (Integer.parseInt(positions.get(2)) == position || Integer.parseInt(positions.get(3)) == position) {
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

        return position.toString();
    }

    private void addFirstCurrentQuestions() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        ArrayList<String> myAnswerOneEN = new ArrayList<>();
        myAnswerOneEN.add(answerOneEN.get(Integer.parseInt(positions.get(2))));
        myAnswerOneEN.add(answerTwoEN.get(Integer.parseInt(positions.get(2))));
        myAnswerOneEN.add(answerThreeEN.get(Integer.parseInt(positions.get(2))));
        myAnswerOneEN.add(answerFourEN.get(Integer.parseInt(positions.get(2))));
        ArrayList<String> myAnswerOnePL = new ArrayList<>();
        myAnswerOnePL.add(answerOnePL.get(Integer.parseInt(positions.get(2))));
        myAnswerOnePL.add(answerTwoPL.get(Integer.parseInt(positions.get(2))));
        myAnswerOnePL.add(answerThreePL.get(Integer.parseInt(positions.get(2))));
        myAnswerOnePL.add(answerFourPL.get(Integer.parseInt(positions.get(2))));
        ArrayList<String> myAnswerTwoEN = new ArrayList<>();
        myAnswerTwoEN.add(answerOneEN.get(Integer.parseInt(positions.get(3))));
        myAnswerTwoEN.add(answerTwoEN.get(Integer.parseInt(positions.get(3))));
        myAnswerTwoEN.add(answerThreeEN.get(Integer.parseInt(positions.get(3))));
        myAnswerTwoEN.add(answerFourEN.get(Integer.parseInt(positions.get(3))));
        ArrayList<String> myAnswerTwoPL = new ArrayList<>();
        myAnswerTwoPL.add(answerOnePL.get(Integer.parseInt(positions.get(3))));
        myAnswerTwoPL.add(answerTwoPL.get(Integer.parseInt(positions.get(3))));
        myAnswerTwoPL.add(answerThreePL.get(Integer.parseInt(positions.get(3))));
        myAnswerTwoPL.add(answerFourPL.get(Integer.parseInt(positions.get(3))));
        ArrayList<String> myAnswerThreeEN = new ArrayList<>();
        myAnswerThreeEN.add(answerOneEN.get(Integer.parseInt(positions.get(4))));
        myAnswerThreeEN.add(answerTwoEN.get(Integer.parseInt(positions.get(4))));
        myAnswerThreeEN.add(answerThreeEN.get(Integer.parseInt(positions.get(4))));
        myAnswerThreeEN.add(answerFourEN.get(Integer.parseInt(positions.get(4))));
        ArrayList<String> myAnswerThreePL = new ArrayList<>();
        myAnswerThreePL.add(answerOnePL.get(Integer.parseInt(positions.get(4))));
        myAnswerThreePL.add(answerTwoPL.get(Integer.parseInt(positions.get(4))));
        myAnswerThreePL.add(answerThreePL.get(Integer.parseInt(positions.get(4))));
        myAnswerThreePL.add(answerFourPL.get(Integer.parseInt(positions.get(4))));

        CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(myIdSocialMedia, friendIdSocialMedia, myIdSocialMedia, friendIdSocialMedia, false, positions, Integer.parseInt(positions.get(2)), questionsEN.get(Integer.parseInt(positions.get(2))), myAnswerOneEN, questionsPL.get(Integer.parseInt(positions.get(2))), myAnswerOnePL, myMarkedAnswer.get(0), Integer.parseInt(positions.get(3)), questionsEN.get(Integer.parseInt(positions.get(3))), myAnswerTwoEN, questionsPL.get(Integer.parseInt(positions.get(3))), myAnswerTwoPL, myMarkedAnswer.get(1), Integer.parseInt(positions.get(4)), questionsEN.get(Integer.parseInt(positions.get(4))), myAnswerThreeEN, questionsPL.get(Integer.parseInt(positions.get(4))), myAnswerThreePL, myMarkedAnswer.get(2));
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
