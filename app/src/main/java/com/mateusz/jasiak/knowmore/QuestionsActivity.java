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
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity {

    private ArrayList<String> positions = new ArrayList<>(); //TODO: Pobierać dla konkretnej pary z bazy danych
    private ArrayList<String> myMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> intentMyMarkedAnswer = new ArrayList<>();
    private ArrayList<String> myFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> intentMyFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> localMyFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<String> friendMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> intentFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> localFriendMarkedAnswer = new ArrayList<>();
    private int question = 0;
    private int friendQuestion = 0;
    private boolean endOfAnswer = false;
    private boolean startFriendAnswer = false;
    private boolean checkTheAnswers = true;
    private int markedAnswer = 0;

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
    private ArrayList<String> myReplies = new ArrayList<>();
    private ArrayList<String> localMyReplies = new ArrayList<>();
    private ArrayList<String> myFriendReplies = new ArrayList<>();
    private ArrayList<String> friendReplies = new ArrayList<>();
    private ArrayList<String> localFriendReplies = new ArrayList<>();

    /*ArrayList<String> myAnswerOneEN = new ArrayList<>();
    ArrayList<String> myAnswerTwoEN = new ArrayList<>();
    ArrayList<String> myAnswerThreeEN = new ArrayList<>();
    ArrayList<String> myAnswerOnePL = new ArrayList<>();
    ArrayList<String> myAnswerTwoPL = new ArrayList<>();
    ArrayList<String> myAnswerThreePL = new ArrayList<>();*/

    private String idCurrentQuestion;
    //private ArrayList<ArrayList<String>> selectedQuestions = new ArrayList<>();
    private ArrayList<String> selectedQuestions = new ArrayList<>();
    private int positionI;

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

        myReplies = intent.getStringArrayListExtra("KEY_MY_REPLIES");
        myFriendReplies = intent.getStringArrayListExtra("KEY_MY_FRIEND_REPLIES");
        friendReplies = intent.getStringArrayListExtra("KEY_FRIEND_REPLIES");
        //selectedQuestions = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("KEY_SELECTED_QUESTIONS");

        myMarkedAnswer = intent.getStringArrayListExtra("KEY_MY_MARKED_ANSWER");
        myFriendMarkedAnswer = intent.getStringArrayListExtra("KEY_MY_FRIEND_MARKED_ANSWER");
        friendMarkedAnswer = intent.getStringArrayListExtra("KEY_FRIEND_MARKED_ANSWER");

        questionView = findViewById(R.id.questionView);
        answerButton1 = findViewById(R.id.answerButton1);
        answerButton2 = findViewById(R.id.answerButton2);
        answerButton3 = findViewById(R.id.answerButton3);
        answerButton4 = findViewById(R.id.answerButton4);

        answerButton1.setClickable(false);
        answerButton2.setClickable(false);
        answerButton3.setClickable(false);
        answerButton4.setClickable(false);

        getSelectedQuestions();

        /*positions.add(myIdSocialMedia);
        positions.add(friendIdSocialMedia);*/

        for (int i = 0; i < myReplies.size(); i++) {
            if ((myReplies.get(i + 1).equals(myIdSocialMedia) && myReplies.get(i + 2).equals(friendIdSocialMedia)) || (myReplies.get(i + 1).equals(friendIdSocialMedia) && myReplies.get(i + 2).equals(myIdSocialMedia))) {
                idCurrentQuestion = myReplies.get(i);
                localMyReplies.add(myReplies.get(i + 3));
                localMyReplies.add(myReplies.get(i + 4));
                localMyReplies.add(myReplies.get(i + 5));
                localMyReplies.add(myReplies.get(i + 6));
                localMyReplies.add(myReplies.get(i + 7));
                localMyReplies.add(myReplies.get(i + 8));
                break;
            }
        }
        for (int i = 0; i < friendReplies.size(); i++) {
            if ((friendReplies.get(i + 1).equals(myIdSocialMedia) && friendReplies.get(i + 2).equals(friendIdSocialMedia)) || (friendReplies.get(i + 1).equals(friendIdSocialMedia) && friendReplies.get(i + 2).equals(myIdSocialMedia))) {
                idCurrentQuestion = friendReplies.get(i);
                localFriendReplies.add(friendReplies.get(i + 3));
                localFriendReplies.add(friendReplies.get(i + 4));
                localFriendReplies.add(friendReplies.get(i + 5));
                localFriendReplies.add(friendReplies.get(i + 6));
                localFriendReplies.add(friendReplies.get(i + 7));
                localFriendReplies.add(friendReplies.get(i + 8));
                break;
            }
        }

        for (int i = 0; i < myMarkedAnswer.size(); i++) {
            if ((myMarkedAnswer.get(i).equals(myIdSocialMedia) && myMarkedAnswer.get(i + 1).equals(friendIdSocialMedia)) || (myMarkedAnswer.get(i).equals(friendIdSocialMedia) && myMarkedAnswer.get(i + 1).equals(myIdSocialMedia))) {
                intentMyMarkedAnswer.add(Integer.parseInt(myMarkedAnswer.get(i + 2)));
                intentMyMarkedAnswer.add(Integer.parseInt(myMarkedAnswer.get(i + 3)));
                intentMyMarkedAnswer.add(Integer.parseInt(myMarkedAnswer.get(i + 4)));
                break;
            }
        }
        for (int i = 0; i < myFriendMarkedAnswer.size(); i++) {
            if ((myFriendMarkedAnswer.get(i).equals(myIdSocialMedia) && myFriendMarkedAnswer.get(i + 1).equals(friendIdSocialMedia)) || (myFriendMarkedAnswer.get(i).equals(friendIdSocialMedia) && myFriendMarkedAnswer.get(i + 1).equals(myIdSocialMedia))) {
                intentMyFriendMarkedAnswer.add(Integer.parseInt(myFriendMarkedAnswer.get(i + 2)));
                intentMyFriendMarkedAnswer.add(Integer.parseInt(myFriendMarkedAnswer.get(i + 3)));
                intentMyFriendMarkedAnswer.add(Integer.parseInt(myFriendMarkedAnswer.get(i + 4)));
                break;
            }
        }
        for (int i = 0; i < friendMarkedAnswer.size(); i++) {
            if ((friendMarkedAnswer.get(i).equals(myIdSocialMedia) && friendMarkedAnswer.get(i + 1).equals(friendIdSocialMedia)) || (friendMarkedAnswer.get(i).equals(friendIdSocialMedia) && friendMarkedAnswer.get(i + 1).equals(myIdSocialMedia))) {
                intentFriendMarkedAnswer.add(Integer.parseInt(friendMarkedAnswer.get(i + 2)));
                intentFriendMarkedAnswer.add(Integer.parseInt(friendMarkedAnswer.get(i + 3)));
                intentFriendMarkedAnswer.add(Integer.parseInt(friendMarkedAnswer.get(i + 4)));
                break;
            }
        }

        showQuestion(0, localMyReplies);
        showSelectedAnswers(0, intentMyMarkedAnswer, intentMyFriendMarkedAnswer);

        int delayMillis = 2000;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                showQuestion(2, localMyReplies);
                showSelectedAnswers(1, intentMyMarkedAnswer, intentMyFriendMarkedAnswer);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        showQuestion(4, localMyReplies);
                        showSelectedAnswers(2, intentMyMarkedAnswer, intentMyFriendMarkedAnswer);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                endOfAnswer = true;
                                answerButton1.setClickable(true);
                                answerButton2.setClickable(true);
                                answerButton3.setClickable(true);
                                answerButton4.setClickable(true);
                                showQuestion(0, localFriendReplies);
                            }
                        }, delayMillis);
                    }
                }, delayMillis);
            }
        }, delayMillis);

        /*myAnswerOneEN.add(answerOneEN.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerOneEN.add(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerOneEN.add(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerOneEN.add(answerFourEN.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerTwoEN.add(answerOneEN.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerTwoEN.add(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerTwoEN.add(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerTwoEN.add(answerFourEN.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerThreeEN.add(answerOneEN.get(Integer.parseInt(localFriendReplies.get(4))));
        myAnswerThreeEN.add(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(4))));
        myAnswerThreeEN.add(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(4))));
        myAnswerThreeEN.add(answerFourEN.get(Integer.parseInt(localFriendReplies.get(4))));

        myAnswerOnePL.add(answerOnePL.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerOnePL.add(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerOnePL.add(answerThreePL.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerOnePL.add(answerFourPL.get(Integer.parseInt(localFriendReplies.get(0))));
        myAnswerTwoPL.add(answerOnePL.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerTwoPL.add(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerTwoPL.add(answerThreePL.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerTwoPL.add(answerFourPL.get(Integer.parseInt(localFriendReplies.get(2))));
        myAnswerThreePL.add(answerOnePL.get(Integer.parseInt(localFriendReplies.get(4))));
        myAnswerThreePL.add(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(4))));
        myAnswerThreePL.add(answerThreePL.get(Integer.parseInt(localFriendReplies.get(4))));
        myAnswerThreePL.add(answerFourPL.get(Integer.parseInt(localFriendReplies.get(4))));*/
    }

    public void answerButton1(View view) {
        if (endOfAnswer) {
            showQuestions(1);
            if (checkTheAnswers) {
                showSelectedAnswers(markedAnswer, localMyFriendMarkedAnswer, intentFriendMarkedAnswer);
                markedAnswer++;
                answerButton1.setClickable(false);
                answerButton2.setClickable(false);
                answerButton3.setClickable(false);
                answerButton4.setClickable(false);
            }
        }
        if (startFriendAnswer) {
            showQuestions(1);
        }
    }

    public void answerButton2(View view) {
        if (endOfAnswer) {
            showQuestions(2);
            if (checkTheAnswers) {
                showSelectedAnswers(markedAnswer, localMyFriendMarkedAnswer, intentFriendMarkedAnswer);
                markedAnswer++;
                answerButton1.setClickable(false);
                answerButton2.setClickable(false);
                answerButton3.setClickable(false);
                answerButton4.setClickable(false);
            }
        }
        if (startFriendAnswer) {
            showQuestions(2);
        }
    }

    public void answerButton3(View view) {
        if (endOfAnswer) {
            showQuestions(3);
            if (checkTheAnswers) {
                showSelectedAnswers(markedAnswer, localMyFriendMarkedAnswer, intentFriendMarkedAnswer);
                markedAnswer++;
                answerButton1.setClickable(false);
                answerButton2.setClickable(false);
                answerButton3.setClickable(false);
                answerButton4.setClickable(false);
            }
        }
        if (startFriendAnswer) {
            showQuestions(3);
        }
    }

    public void answerButton4(View view) {
        if (endOfAnswer) {
            showQuestions(4);
            if (checkTheAnswers) {
                showSelectedAnswers(markedAnswer, localMyFriendMarkedAnswer, intentFriendMarkedAnswer);
                markedAnswer++;
                answerButton1.setClickable(false);
                answerButton2.setClickable(false);
                answerButton3.setClickable(false);
                answerButton4.setClickable(false);
            }
        }
        if (startFriendAnswer) {
            showQuestions(4);
        }
    }

    private void showQuestions(int markedAnswer) {
        int delayMillis = 2000;

        if (question != 2) {
            localMyFriendMarkedAnswer.add(markedAnswer);
            question++;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    showQuestion(question + question, localFriendReplies);
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
            localMyFriendMarkedAnswer.add(markedAnswer);

            if (endOfAnswer) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        endOfAnswer = false;
                        checkTheAnswers = false;
                        startFriendAnswer = true;
                        answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton1.setClickable(true);
                        answerButton2.setClickable(true);
                        answerButton3.setClickable(true);
                        answerButton4.setClickable(true);

                        getQuestion();
                        friendQuestion++;
                    }
                }, delayMillis);
            } else {
                if (friendQuestion != 3) {
                    localFriendMarkedAnswer.add(markedAnswer);
                    friendQuestion++;
                    getQuestion();
                } else {
                    localFriendMarkedAnswer.add(markedAnswer);
                    //updateCurrentQuestion();
                    Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                    QuestionsActivity.this.startActivity(intent);
                }
            }
        }
    }

    private void showSelectedAnswers(int questions, ArrayList<Integer> markedAnswerPlayerOne, ArrayList<Integer> markedAnswerPlayerTwo) {
        if (markedAnswerPlayerOne.get(questions).equals(markedAnswerPlayerTwo.get(questions)) && markedAnswerPlayerOne.get(questions) == 1) {
            answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else if (markedAnswerPlayerOne.get(questions).equals(markedAnswerPlayerTwo.get(questions)) && markedAnswerPlayerOne.get(questions) == 2) {
            answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else if (markedAnswerPlayerOne.get(questions).equals(markedAnswerPlayerTwo.get(questions)) && markedAnswerPlayerOne.get(questions) == 3) {
            answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else if (markedAnswerPlayerOne.get(questions).equals(markedAnswerPlayerTwo.get(questions)) && markedAnswerPlayerOne.get(questions) == 4) {
            answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, R.drawable.avatar, 0);
        } else {
            if (markedAnswerPlayerOne.get(questions) == 1 && markedAnswerPlayerTwo.get(questions) == 2) {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 1 && markedAnswerPlayerTwo.get(questions) == 3) {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 1 && markedAnswerPlayerTwo.get(questions) == 4) {
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 2 && markedAnswerPlayerTwo.get(questions) == 1) {
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 2 && markedAnswerPlayerTwo.get(questions) == 3) {
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 2 && markedAnswerPlayerTwo.get(questions) == 4) {
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 3 && markedAnswerPlayerTwo.get(questions) == 1) {
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 3 && markedAnswerPlayerTwo.get(questions) == 2) {
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 3 && markedAnswerPlayerTwo.get(questions) == 4) {
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 4 && markedAnswerPlayerTwo.get(questions) == 1) {
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 4 && markedAnswerPlayerTwo.get(questions) == 2) {
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            } else if (markedAnswerPlayerOne.get(questions) == 4 && markedAnswerPlayerTwo.get(questions) == 3) {
                answerButton4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0);
                answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar, 0);
            }
        }
    }

    private void getQuestion() {
        Random random = new Random();
        Integer position;
        for (int i = 0; i < idQuestions.size(); i++) { //TODO: Przerobić na Iterator.
            for (int j = 0; j < idQuestions.size(); j++) {
                for (int k = 0; k < selectedQuestions.size(); k++) {
                    if (idQuestions.get(j).toString().equals(selectedQuestions.get(k))) {
                        idQuestions.remove(j);
                        questionsPL.remove(j);
                        answerOnePL.remove(j);
                        answerTwoPL.remove(j);
                        answerThreePL.remove(j);
                        answerFourPL.remove(j);
                        questionsEN.remove(j);
                        answerOneEN.remove(j);
                        answerTwoEN.remove(j);
                        answerThreeEN.remove(j);
                        answerFourEN.remove(j);
                    }
                }
            }
        }

        position = random.nextInt(idQuestions.size());
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
        positions.add(position.toString());
        selectedQuestions.add(position.toString());

        idQuestions.remove(position);
        questionsPL.remove(position);
        answerOnePL.remove(position);
        answerTwoPL.remove(position);
        answerThreePL.remove(position);
        answerFourPL.remove(position);
        questionsEN.remove(position);
        answerOneEN.remove(position);
        answerTwoEN.remove(position);
        answerThreeEN.remove(position);
        answerFourEN.remove(position);
    }

    private void showQuestion(int currentQuestion, ArrayList<String> replies) {
        if (Locale.getDefault().getLanguage().equals("pl")) {
            questionView.setText(questionsPL.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton1.setText(answerOnePL.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton2.setText(answerTwoPL.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton3.setText(answerThreePL.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton4.setText(answerFourPL.get(Integer.parseInt(replies.get(currentQuestion))));
        } else {
            questionView.setText(questionsEN.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton1.setText(answerOneEN.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton2.setText(answerTwoEN.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton3.setText(answerThreeEN.get(Integer.parseInt(replies.get(currentQuestion))));
            answerButton4.setText(answerFourEN.get(Integer.parseInt(replies.get(currentQuestion))));
        }
    }

    private void getSelectedQuestions() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getCurrentQuestion();
        call.enqueue(new Callback<List<CurrentQuestionsAPI>>() {
            @Override
            public void onResponse(Call<List<CurrentQuestionsAPI>> call, Response<List<CurrentQuestionsAPI>> response) {
                if (response.code() > 399) {
                    finish();
                } else {
                    List<CurrentQuestionsAPI> currentQuestionsAPI = response.body();

                    for (CurrentQuestionsAPI currentQuestionsAPIs : currentQuestionsAPI) {
                        if (currentQuestionsAPIs.getId().equals(idCurrentQuestion)) {
                            for (int i = 0; i < currentQuestionsAPIs.getSelectedQuestions().size(); i++) {
                                selectedQuestions.add(currentQuestionsAPIs.getSelectedQuestions().get(i));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentQuestionsAPI>> call, Throwable t) {

            }
        });
    }

    /*private void updateCurrentQuestion() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        ArrayList<String> friendAnswerOneEN = new ArrayList<>();
        friendAnswerOneEN.add(answerOneEN.get(Integer.parseInt(positions.get(0))));
        friendAnswerOneEN.add(answerTwoEN.get(Integer.parseInt(positions.get(0))));
        friendAnswerOneEN.add(answerThreeEN.get(Integer.parseInt(positions.get(0))));
        friendAnswerOneEN.add(answerFourEN.get(Integer.parseInt(positions.get(0))));
        ArrayList<String> friendAnswerOnePL = new ArrayList<>();
        friendAnswerOnePL.add(answerOnePL.get(Integer.parseInt(positions.get(0))));
        friendAnswerOnePL.add(answerTwoPL.get(Integer.parseInt(positions.get(0))));
        friendAnswerOnePL.add(answerThreePL.get(Integer.parseInt(positions.get(0))));
        friendAnswerOnePL.add(answerFourPL.get(Integer.parseInt(positions.get(0))));
        ArrayList<String> friendAnswerTwoEN = new ArrayList<>();
        friendAnswerTwoEN.add(answerOneEN.get(Integer.parseInt(positions.get(1))));
        friendAnswerTwoEN.add(answerTwoEN.get(Integer.parseInt(positions.get(1))));
        friendAnswerTwoEN.add(answerThreeEN.get(Integer.parseInt(positions.get(1))));
        friendAnswerTwoEN.add(answerFourEN.get(Integer.parseInt(positions.get(1))));
        ArrayList<String> friendAnswerTwoPL = new ArrayList<>();
        friendAnswerTwoPL.add(answerOnePL.get(Integer.parseInt(positions.get(1))));
        friendAnswerTwoPL.add(answerTwoPL.get(Integer.parseInt(positions.get(1))));
        friendAnswerTwoPL.add(answerThreePL.get(Integer.parseInt(positions.get(1))));
        friendAnswerTwoPL.add(answerFourPL.get(Integer.parseInt(positions.get(1))));
        ArrayList<String> friendAnswerThreeEN = new ArrayList<>();
        friendAnswerThreeEN.add(answerOneEN.get(Integer.parseInt(positions.get(2))));
        friendAnswerThreeEN.add(answerTwoEN.get(Integer.parseInt(positions.get(2))));
        friendAnswerThreeEN.add(answerThreeEN.get(Integer.parseInt(positions.get(2))));
        friendAnswerThreeEN.add(answerFourEN.get(Integer.parseInt(positions.get(2))));
        ArrayList<String> friendAnswerThreePL = new ArrayList<>();
        friendAnswerThreePL.add(answerOnePL.get(Integer.parseInt(positions.get(2))));
        friendAnswerThreePL.add(answerTwoPL.get(Integer.parseInt(positions.get(2))));
        friendAnswerThreePL.add(answerThreePL.get(Integer.parseInt(positions.get(2))));
        friendAnswerThreePL.add(answerFourPL.get(Integer.parseInt(positions.get(2))));

        CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(friendIdSocialMedia, myIdSocialMedia, friendIdSocialMedia, true, selectedQuestions, *//*M1*//* Integer.parseInt(localFriendReplies.get(0)), questionsEN.get(Integer.parseInt(localFriendReplies.get(0))), myAnswerOneEN, questionsPL.get(Integer.parseInt(localFriendReplies.get(0))), myAnswerOnePL, intentMyMarkedAnswer.get(0), intentMyFriendMarkedAnswer.get(0), *//*M2*//* Integer.parseInt(localFriendReplies.get(1)), questionsEN.get(Integer.parseInt(localFriendReplies.get(1))), myAnswerTwoEN, questionsPL.get(Integer.parseInt(localFriendReplies.get(1))), myAnswerTwoPL, intentMyMarkedAnswer.get(1), intentMyFriendMarkedAnswer.get(1), *//*M3*//* Integer.parseInt(localFriendReplies.get(3)), questionsEN.get(Integer.parseInt(localFriendReplies.get(3))), myAnswerThreeEN, questionsPL.get(Integer.parseInt(localFriendReplies.get(3))), myAnswerThreePL, intentMyMarkedAnswer.get(2), intentMyFriendMarkedAnswer.get(2), *//*F1*//* Integer.parseInt(positions.get(0)), questionsEN.get(Integer.parseInt(positions.get(0))), friendAnswerOneEN, questionsPL.get(Integer.parseInt(positions.get(0))), friendAnswerOnePL, intentFriendMarkedAnswer.get(0), *//*F2*//* Integer.parseInt(positions.get(1)), questionsEN.get(Integer.parseInt(positions.get(1))), friendAnswerTwoEN, questionsPL.get(Integer.parseInt(positions.get(1))), friendAnswerTwoPL, intentFriendMarkedAnswer.get(1), *//*F3*//*Integer.parseInt(positions.get(2)), questionsEN.get(Integer.parseInt(positions.get(2))), friendAnswerThreeEN, questionsPL.get(Integer.parseInt(positions.get(2))), friendAnswerThreePL, intentFriendMarkedAnswer.get(2));
        //CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(friendIdSocialMedia, selectedQuestions, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        Call<CurrentQuestionsAPI> call = jsonKnowMoreAPI.updateCurrentQuestion(idCurrentQuestion, currentQuestionsAPI);
        call.enqueue(new Callback<CurrentQuestionsAPI>() {
            @Override
            public void onResponse(Call<CurrentQuestionsAPI> call, Response<CurrentQuestionsAPI> response) {

            }

            @Override
            public void onFailure(Call<CurrentQuestionsAPI> call, Throwable t) {

            }
        });
    }*/
}
