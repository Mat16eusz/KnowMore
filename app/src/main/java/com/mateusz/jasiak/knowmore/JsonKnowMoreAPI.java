package com.mateusz.jasiak.knowmore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonKnowMoreAPI {
    //String BASE_URL = "http://10.0.2.2:3000/";             // localhost
    String BASE_URL = "https://know-more-mj.herokuapp.com/"; // Heroku

    @GET("players")
    Call<List<PlayersDataAPI>> getPlayersData();

    @POST("players")
    Call<PlayersDataAPI> addPlayer(@Body PlayersDataAPI playersDataAPI);

    @PUT("players/{id}")
    Call<PlayersDataAPI> updatePlayer(@Path("id") String id, @Body PlayersDataAPI playersDataAPI);

    @GET("invitations")
    Call<List<PlayerInvitationAPI>> getPlayerInvitation();

    @POST("invitations")
    Call<PlayerInvitationAPI> addPlayerInvite(@Body PlayerInvitationAPI playerInvitationAPI);

    @DELETE("invitations/{id}")
    Call<Void>deletePlayerInvite(@Path("id") String id);

    @GET("questions")
    Call<List<QuestionsAPI>> getQuestions();

    @GET("currentQuestions")
    Call<List<CurrentQuestionsAPI>> getWhoIsTurn();

    @GET("currentQuestions")
    Call<List<CurrentQuestionsAPI>> getCurrentQuestion();

    @POST("currentQuestions")
    Call<CurrentQuestionsAPI> addCurrentQuestions(@Body CurrentQuestionsAPI currentQuestionsAPI);

    @PUT("currentQuestions/{id}")
    Call<CurrentQuestionsAPI> updateCurrentQuestion(@Path("id") String id, @Body CurrentQuestionsAPI currentQuestionsAPI);
}
