package com.mateusz.jasiak.knowmore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonKnowMoreAPI {
    @GET("players")
    Call<List<PlayersDataAPI>> getPlayersData();

    @POST("players")
    Call<PlayersDataAPI> addPlayer(@Body PlayersDataAPI playersDataAPI);
}
