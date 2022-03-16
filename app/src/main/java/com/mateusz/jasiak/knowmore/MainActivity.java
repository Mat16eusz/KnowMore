package com.mateusz.jasiak.knowmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

//TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
//import com.facebook.gamingservices.FriendFinderDialog;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    //Google login
    ActivityMainBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    //TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
    //private static final String TAG = "Test";
    //----------------------------------------------------------------------------------------------
    //Rozwijana lista z graczami
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> players = new ArrayList<String>();

    ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViewArrayList;
    private RecyclerView recyclerView;
    private PlayerFriendAdapterRecyclerView adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        autoCompleteTextView = findViewById(R.id.editTextTextPersonName);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, players);

        autoCompleteTextView.setAdapter(arrayAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loadData();
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
    void getPlayers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
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
                        players.add(playersDataAPIs.getName() + "#" + playersDataAPIs.getIdSocialMedia() + "#" + playersDataAPIs.getPersonPhoto());
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

    public void addPlayerToListFriends(View view) {
        try {
            String text;
            String idSocialMedia = "";
            String name = "";
            String avatar = "";
            int i = 0;
            int j, textLength;
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
                textLength = text.length();
                avatar = text.substring(j + 1, textLength);

                if (playerFriendRecyclerViewArrayList.size() == 0) {
                    insertItem(idSocialMedia, name, avatar);
                } else {
                    for (i = 0; i < playerFriendRecyclerViewArrayList.size(); i++) {
                        if (((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.idSocialMediaRecyclerView)).getText().toString().equals(idSocialMedia)) {
                            counterIdSocialMedia++;
                        }
                    }

                    if (counterIdSocialMedia == 0) {
                        insertItem(idSocialMedia, name, avatar);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.wrong_name, Toast.LENGTH_LONG).show();
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
        Type type = new TypeToken<ArrayList<PlayerFriendRecyclerView>>() {}.getType();
        playerFriendRecyclerViewArrayList = gson.fromJson(json, type);

        if (playerFriendRecyclerViewArrayList == null) {
            playerFriendRecyclerViewArrayList = new ArrayList<>();
        }
    }

    //TODO: Raczej do usunięcia. To było pod zapraszanie zanjomych na FB nie do końca działało.
    /*public void Test(View view) {
        FriendFinderDialog dialog = new FriendFinderDialog(this);
        // if we want to get notified when the dialog is closed
        // we can register a Callback
        dialog.registerCallback(
                this.callbackManager,
                new FacebookCallback<FriendFinderDialog.Result>() {
                    @Override
                    public void onSuccess(FriendFinderDialog.Result friendFinderResult) {
                        Log.e(MainActivity.TAG, "Player Finder Dialog closed");
                    }

                    @Override
                    public void onCancel() {}

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("GamingServicesFBCallback", exception.toString());
                    }
                });
        // open the dialog
        dialog.show();
    }*/
}
