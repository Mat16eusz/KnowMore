package com.mateusz.jasiak.knowmore;

import static com.mateusz.jasiak.knowmore.APIClient.getClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private ArrayList<String> positions = new ArrayList<>(); //TODO: Pobierać dla konkretnej pary z bazy danych
    private ArrayList<Integer> myMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> myFriendMarkedAnswer = new ArrayList<>();
    private ArrayList<Integer> friendMarkedAnswer = new ArrayList<>();
    private int question = 0;
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
    private ArrayList<String> friendReplies = new ArrayList<>();
    private ArrayList<String> localFriendReplies = new ArrayList<>();
    private String idCurrentQuestion;
    private boolean orFirstGame = true;
    private ArrayList<ArrayList<String>> selectedQuestions = new ArrayList<>();
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

        friendReplies = intent.getStringArrayListExtra("KEY_FRIEND_REPLIES");
        selectedQuestions = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("KEY_SELECTED_QUESTIONS");

        questionView = findViewById(R.id.questionView);
        answerButton1 = findViewById(R.id.answerButton1);
        answerButton2 = findViewById(R.id.answerButton2);
        answerButton3 = findViewById(R.id.answerButton3);
        answerButton4 = findViewById(R.id.answerButton4);

        positions.add(myIdSocialMedia);
        positions.add(friendIdSocialMedia);

        if (friendReplies != null) {
            orFirstGame = false;
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
                    break;
                }
            }

            if (Locale.getDefault().getLanguage().equals("pl")) {
                questionView.setText(questionsPL.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton1.setText(answerOnePL.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton2.setText(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton3.setText(answerThreePL.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton4.setText(answerFourPL.get(Integer.parseInt(localFriendReplies.get(0))));
            } else {
                questionView.setText(questionsEN.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton1.setText(answerOneEN.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton2.setText(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton3.setText(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(0))));
                answerButton4.setText(answerFourEN.get(Integer.parseInt(localFriendReplies.get(0))));
            }
        } else {
            positions.add(getQuestion());
        }
    }

    public void answerButton1(View view) {
        if (orFirstGame) {
            showQuestions(1);
        } else {
            showQuestions(1);
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
        }
    }

    public void answerButton2(View view) {
        if (orFirstGame) {
            showQuestions(2);
        } else {
            showQuestions(2);
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
        }
    }

    public void answerButton3(View view) {
        if (orFirstGame) {
            showQuestions(3);
        } else {
            showQuestions(3);
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
        }
    }

    public void answerButton4(View view) {
        if (orFirstGame) {
            showQuestions(4);
        } else {
            showQuestions(4);
            showSelectedAnswers(markedAnswer);
            markedAnswer++;
        }
    }

    private void showQuestions(int markedAnswer) {
        int delayMillis = 2000;

        if (question != 2) {
            if (orFirstGame) {
                myMarkedAnswer.add(markedAnswer);
                question++;
                positions.add(getQuestion());
            } else {
                myFriendMarkedAnswer.add(markedAnswer);
                question++;

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (Locale.getDefault().getLanguage().equals("pl")) {
                            questionView.setText(questionsPL.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton1.setText(answerOnePL.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton2.setText(answerTwoPL.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton3.setText(answerThreePL.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton4.setText(answerFourPL.get(Integer.parseInt(localFriendReplies.get(question + question))));
                        } else {
                            questionView.setText(questionsEN.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton1.setText(answerOneEN.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton2.setText(answerTwoEN.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton3.setText(answerThreeEN.get(Integer.parseInt(localFriendReplies.get(question + question))));
                            answerButton4.setText(answerFourEN.get(Integer.parseInt(localFriendReplies.get(question + question))));
                        }
                        answerButton1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        answerButton4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }, delayMillis);
            }
        } else {
            if (orFirstGame) {
                myMarkedAnswer.add(markedAnswer);
                getCurrentQuestions();
                Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                QuestionsActivity.this.startActivity(intent);
            } else {
                myFriendMarkedAnswer.add(markedAnswer);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        updateCurrentQuestion();
                        //getCurrentQuestions(); //TODO: Edit najprawdopodobniej do funkcji dodać parametr i przekazywać orFirstGame
                        Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                        QuestionsActivity.this.startActivity(intent);
                    }
                }, delayMillis);
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

    private void getCurrentQuestions() { //TODO: Zabezpieczyć gdy kolekcja jest pusta.
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        Call<List<CurrentQuestionsAPI>> call = jsonKnowMoreAPI.getWhoIsTurn();
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

    private String getQuestion() {
        Random random = new Random();
        Integer position = null;
        if (selectedQuestions != null) {
            for (int i = 0; i < selectedQuestions.size(); i++) {
                for (int j = 0; j < selectedQuestions.get(i).size(); j++) {
                    if ((selectedQuestions.get(i).get(j).equals(myIdSocialMedia) && selectedQuestions.get(i).get(j + 1).equals(friendIdSocialMedia)) || (selectedQuestions.get(i).get(j).equals(friendIdSocialMedia) && selectedQuestions.get(i).get(j + 1).equals(myIdSocialMedia))) {
                        positionI = i;

                        position = random.nextInt(idQuestions.size());
                        if (question == 2) {
                            position = random.nextInt(idQuestions.size());
                            int k = 0;
                            while (selectedQuestions.get(i).get(k).equals(position.toString())) {
                                if (k == selectedQuestions.get(i).size()) {
                                    break;
                                }
                                position = random.nextInt(idQuestions.size());
                                k++;
                            }
                        }
                        /*if (question == 3) {
                            while (Integer.parseInt(positions.get(2)) == position) {
                                position = random.nextInt(idQuestions.size());
                            }
                        }
                        if (question == 4) {
                            while (Integer.parseInt(positions.get(2)) == position || Integer.parseInt(positions.get(3)) == position) {
                                position = random.nextInt(idQuestions.size());
                            }
                        }*/

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

                        selectedQuestions.get(i).add(position.toString());
                    }
                }
            }
        } else {
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

        CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(myIdSocialMedia, friendIdSocialMedia, friendIdSocialMedia, false, positions, Integer.parseInt(positions.get(2)), questionsEN.get(Integer.parseInt(positions.get(2))), myAnswerOneEN, questionsPL.get(Integer.parseInt(positions.get(2))), myAnswerOnePL, myMarkedAnswer.get(0), null, Integer.parseInt(positions.get(3)), questionsEN.get(Integer.parseInt(positions.get(3))), myAnswerTwoEN, questionsPL.get(Integer.parseInt(positions.get(3))), myAnswerTwoPL, myMarkedAnswer.get(1), null, Integer.parseInt(positions.get(4)), questionsEN.get(Integer.parseInt(positions.get(4))), myAnswerThreeEN, questionsPL.get(Integer.parseInt(positions.get(4))), myAnswerThreePL, myMarkedAnswer.get(2), null, /*friend*/ null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null); //TODO: Nulle zamienić.
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

    private void updateCurrentQuestion() {
        JsonKnowMoreAPI jsonKnowMoreAPI = getClient().create(JsonKnowMoreAPI.class);

        // TODO: positionI + 1 usunąć +1 z parametru.
        CurrentQuestionsAPI currentQuestionsAPI = new CurrentQuestionsAPI(friendIdSocialMedia, true, positions, myFriendMarkedAnswer.get(0), myFriendMarkedAnswer.get(1), myFriendMarkedAnswer.get(2), /*friend*/ null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
                    List<CurrentQuestionsAPI> currentQuestionsAPI = response.body();

                    for (CurrentQuestionsAPI currentQuestionsAPIs : currentQuestionsAPI) {
                        if ((currentQuestionsAPIs.getId().equals(idCurrentQuestion))) {
                            myMarkedAnswer.add(currentQuestionsAPIs.getMyMarkedAnswerOne());
                            myMarkedAnswer.add(currentQuestionsAPIs.getMyMarkedAnswerTwo());
                            myMarkedAnswer.add(currentQuestionsAPIs.getMyMarkedAnswerThree());
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
