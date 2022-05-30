package com.mateusz.jasiak.knowmore;

import static com.mateusz.jasiak.knowmore.APIClient.getClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstQuestionsFriendActivity extends AppCompatActivity {

    private ArrayList<String> positions = new ArrayList<>(); //TODO: Pobierać dla konkretnej pary z bazy danych
    private ArrayList<Integer> myMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> myFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> friendMarkedAnswer = new ArrayList<>();
    private int question = 0;
    private int friendQuestion = 0;
    private boolean endOfAnswer = false;
    private boolean checkTheAnswers = true;
    private int markedAnswer = 0;

    private String myIdSocialMedia;
    private String friendIdSocialMedia;

    private TextView whoseAsking;
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
    private ArrayList<String> friendReplies = new ArrayList<>();
    private ArrayList<String> localFriendReplies = new ArrayList<>();
    private String idCurrentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_questions_friend);

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

        friendReplies = intent.getStringArrayListExtra("KEY_MY_FRIEND_REPLIES");

        whoseAsking = findViewById(R.id.whoseAsking);
        questionView = findViewById(R.id.questionView);
        answerButton1 = findViewById(R.id.answerButton1);
        answerButton2 = findViewById(R.id.answerButton2);
        answerButton3 = findViewById(R.id.answerButton3);
        answerButton4 = findViewById(R.id.answerButton4);

        positions.add(myIdSocialMedia);
        positions.add(friendIdSocialMedia);

        getMyMarkedAnswer();

        for (int i = 0; i < friendReplies.size(); i++) {
            if ((friendReplies.get(i + 1).equals(myIdSocialMedia) && friendReplies.get(i + 2).equals(friendIdSocialMedia)) || (friendReplies.get(i + 1).equals(friendIdSocialMedia) && friendReplies.get(i + 2).equals(myIdSocialMedia))) {
                idCurrentQuestion = friendReplies.get(i);
                localFriendReplies.add(friendReplies.get(i + 3));
                localFriendReplies.add(friendReplies.get(i + 4));
                localFriendReplies.add(friendReplies.get(i + 5));
                localFriendReplies.add(friendReplies.get(i + 6));
                localFriendReplies.add(friendReplies.get(i + 7));
                localFriendReplies.add(friendReplies.get(i + 8));

                positions.add(friendReplies.get(i + 3));
                positions.add(friendReplies.get(i + 5));
                positions.add(friendReplies.get(i + 7));
                break;
            }
        }

        showQuestion(0);
    }

    public void answerButton1(View view) {
        showQuestions(1);
        if (checkTheAnswers) {
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
            answerButton1.setClickable(false);
            answerButton2.setClickable(false);
            answerButton3.setClickable(false);
            answerButton4.setClickable(false);
        }
    }

    public void answerButton2(View view) {
        showQuestions(2);
        if (checkTheAnswers) {
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
            answerButton1.setClickable(false);
            answerButton2.setClickable(false);
            answerButton3.setClickable(false);
            answerButton4.setClickable(false);
        }
    }

    public void answerButton3(View view) {
        showQuestions(3);
        if (checkTheAnswers) {
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
            answerButton1.setClickable(false);
            answerButton2.setClickable(false);
            answerButton3.setClickable(false);
            answerButton4.setClickable(false);
        }
    }

    public void answerButton4(View view) {
        showQuestions(4);
        if (checkTheAnswers) {
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
            answerButton1.setClickable(false);
            answerButton2.setClickable(false);
            answerButton3.setClickable(false);
            answerButton4.setClickable(false);
        }
    }

    private void showQuestions(int markedAnswer) {
        int delayMillis = 2000;

        if (question != 2) {
            myFriendMarkedAnswer.add(markedAnswer);
            question++;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    showQuestion(question + question);
                    answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    answerButton1.setClickable(true);
                    answerButton2.setClickable(true);
                    answerButton3.setClickable(true);
                    answerButton4.setClickable(true);
                }
            }, delayMillis);
        } else {
            myFriendMarkedAnswer.add(markedAnswer);

            if (!endOfAnswer) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        endOfAnswer = true;
                        checkTheAnswers = false;
                        answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton1.setClickable(true);
                        answerButton2.setClickable(true);
                        answerButton3.setClickable(true);
                        answerButton4.setClickable(true);

                        whoseAsking.setText(R.string.select_answer);
                        showQuestion(friendQuestion);
                        friendQuestion++;
                    }
                }, delayMillis);
            } else {
                if (friendQuestion != 3) {
                    showQuestion(friendQuestion + friendQuestion);
                    friendMarkedAnswer.add(markedAnswer);
                    friendQuestion++;
                } else {
                    friendMarkedAnswer.add(markedAnswer);
                    updateCurrentQuestion();
                    Intent intent = new Intent(FirstQuestionsFriendActivity.this, MainActivity.class);
                    FirstQuestionsFriendActivity.this.startActivity(intent);
                }
            }
        }
    }

    private void showSelectedAnswers(int questions) {
        if (myMarkedAnswer.get(questions).equals(myFriendMarkedAnswer.get(questions)) && myMarkedAnswer.get(questions) == 1) {
            answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else if (myMarkedAnswer.get(questions).equals(myFriendMarkedAnswer.get(questions)) && myMarkedAnswer.get(questions) == 2) {
            answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else if (myMarkedAnswer.get(questions).equals(myFriendMarkedAnswer.get(questions)) && myMarkedAnswer.get(questions) == 3) {
            answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else if (myMarkedAnswer.get(questions).equals(myFriendMarkedAnswer.get(questions)) && myMarkedAnswer.get(questions) == 4) {
            answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else {
            if (myMarkedAnswer.get(questions) == 1 && myFriendMarkedAnswer.get(questions) == 2) {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 1 && myFriendMarkedAnswer.get(questions) == 3) {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 1 && myFriendMarkedAnswer.get(questions) == 4) {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 2 && myFriendMarkedAnswer.get(questions) == 1) {
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 2 && myFriendMarkedAnswer.get(questions) == 3) {
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 2 && myFriendMarkedAnswer.get(questions) == 4) {
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 3 && myFriendMarkedAnswer.get(questions) == 1) {
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 3 && myFriendMarkedAnswer.get(questions) == 2) {
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 3 && myFriendMarkedAnswer.get(questions) == 4) {
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 4 && myFriendMarkedAnswer.get(questions) == 1) {
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 4 && myFriendMarkedAnswer.get(questions) == 2) {
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            } else if (myMarkedAnswer.get(questions) == 4 && myFriendMarkedAnswer.get(questions) == 3) {
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
            }
        }
    }

    private void showQuestion(int currentQuestion) {
        if (Locale.getDefault().getLanguage().equals("pl")) {
            questionView.setText(questionsPL.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton1.setText(answerOnePL.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton2.setText(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton3.setText(answerThreePL.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton4.setText(answerFourPL.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
        } else {
            questionView.setText(questionsEN.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton1.setText(answerOneEN.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton2.setText(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton3.setText(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
            answerButton4.setText(answerFourEN.get(Integer.parseInt(localFriendReplies.get(currentQuestion))));
        }
    }

    private void updateCurrentQuestion() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        ArrayList<String> friendAnswerOneEN = new ArrayList<>();
        friendAnswerOneEN.add(answerOneEN.get(Integer.parseInt(localFriendReplies.get(0))));
        friendAnswerOneEN.add(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(0))));
        friendAnswerOneEN.add(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(0))));
        friendAnswerOneEN.add(answerFourEN.get(Integer.parseInt(localFriendReplies.get(0))));
        ArrayList<String> friendAnswerOnePL = new ArrayList<>();
        friendAnswerOnePL.add(answerOnePL.get(Integer.parseInt(localFriendReplies.get(0))));
        friendAnswerOnePL.add(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(0))));
        friendAnswerOnePL.add(answerThreePL.get(Integer.parseInt(localFriendReplies.get(0))));
        friendAnswerOnePL.add(answerFourPL.get(Integer.parseInt(localFriendReplies.get(0))));
        ArrayList<String> friendAnswerTwoEN = new ArrayList<>();
        friendAnswerTwoEN.add(answerOneEN.get(Integer.parseInt(localFriendReplies.get(2))));
        friendAnswerTwoEN.add(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(2))));
        friendAnswerTwoEN.add(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(2))));
        friendAnswerTwoEN.add(answerFourEN.get(Integer.parseInt(localFriendReplies.get(2))));
        ArrayList<String> friendAnswerTwoPL = new ArrayList<>();
        friendAnswerTwoPL.add(answerOnePL.get(Integer.parseInt(localFriendReplies.get(2))));
        friendAnswerTwoPL.add(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(2))));
        friendAnswerTwoPL.add(answerThreePL.get(Integer.parseInt(localFriendReplies.get(2))));
        friendAnswerTwoPL.add(answerFourPL.get(Integer.parseInt(localFriendReplies.get(2))));
        ArrayList<String> friendAnswerThreeEN = new ArrayList<>();
        friendAnswerThreeEN.add(answerOneEN.get(Integer.parseInt(localFriendReplies.get(4))));
        friendAnswerThreeEN.add(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(4))));
        friendAnswerThreeEN.add(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(4))));
        friendAnswerThreeEN.add(answerFourEN.get(Integer.parseInt(localFriendReplies.get(4))));
        ArrayList<String> friendAnswerThreePL = new ArrayList<>();
        friendAnswerThreePL.add(answerOnePL.get(Integer.parseInt(localFriendReplies.get(4))));
        friendAnswerThreePL.add(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(4))));
        friendAnswerThreePL.add(answerThreePL.get(Integer.parseInt(localFriendReplies.get(4))));
        friendAnswerThreePL.add(answerFourPL.get(Integer.parseInt(localFriendReplies.get(4))));

        CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(friendIdSocialMedia, true, positions, myFriendMarkedAnswer.get(0), myFriendMarkedAnswer.get(1), myFriendMarkedAnswer.get(2), /*friend*/ idQuestions.get(Integer.parseInt(localFriendReplies.get(0))), questionsEN.get(Integer.parseInt(localFriendReplies.get(0))), friendAnswerOneEN, questionsPL.get(Integer.parseInt(localFriendReplies.get(0))), friendAnswerOnePL, friendMarkedAnswer.get(0), idQuestions.get(Integer.parseInt(localFriendReplies.get(2))), questionsEN.get(Integer.parseInt(localFriendReplies.get(2))), friendAnswerTwoEN, questionsPL.get(Integer.parseInt(localFriendReplies.get(2))), friendAnswerTwoPL, friendMarkedAnswer.get(1), idQuestions.get(Integer.parseInt(localFriendReplies.get(4))), questionsEN.get(Integer.parseInt(localFriendReplies.get(4))), friendAnswerThreeEN, questionsPL.get(Integer.parseInt(localFriendReplies.get(4))), friendAnswerThreePL, friendMarkedAnswer.get(2));
        Call<CurrentQuestionsAPI> call = jsonKnowMoreAPI.updateCurrentQuestion(idCurrentQuestion, currentQuestionsAPI);
        call.enqueue(new Callback<CurrentQuestionsAPI>() {
            @Override
            public void onResponse(Call<CurrentQuestionsAPI> call, Response<CurrentQuestionsAPI> response) {

            }

            @Override
            public void onFailure(Call<CurrentQuestionsAPI> call, Throwable t) {

            }
        });
    }

    private void getMyMarkedAnswer() {
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
                        if ((currentQuestionsAPI.getId().equals(idCurrentQuestion))) {
                            myMarkedAnswer.add(currentQuestionsAPI.getMyMarkedAnswerOne());
                            myMarkedAnswer.add(currentQuestionsAPI.getMyMarkedAnswerTwo());
                            myMarkedAnswer.add(currentQuestionsAPI.getMyMarkedAnswerThree());
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
