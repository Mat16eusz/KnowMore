package com.mateusz.jasiak.knowmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerFriendAdapterRecyclerView extends RecyclerView.Adapter<PlayerFriendAdapterRecyclerView.PlayerFriendViewHolder> {

    @NonNull
    @Override
    public PlayerFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_friend,
                viewGroup, false);

        PlayerFriendViewHolder playerFriendViewHolder = new PlayerFriendViewHolder(view);

        return playerFriendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerFriendViewHolder playerFriendViewHolder, int i) {
        PlayerFriendRecyclerView currentItem = playerFriendRecyclerViews.get(i);

        playerFriendViewHolder.nameRecyclerView.setText(currentItem.getNameRecyclerView());
        playerFriendViewHolder.idSocialMedia.setText(currentItem.getIdSocialMediaRecyclerView());
        //TODO: Docelowo dodać jeszcze id i awatar.
    }

    @Override
    public int getItemCount() {
        return playerFriendRecyclerViews.size();
    }

    public ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViews; //TODO: Przenieść do góry

    public PlayerFriendAdapterRecyclerView(ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViewArrayList) {
        playerFriendRecyclerViews = playerFriendRecyclerViewArrayList;
    }

    public static class PlayerFriendViewHolder extends RecyclerView.ViewHolder {

        public TextView idSocialMedia;
        public TextView nameRecyclerView;

        public PlayerFriendViewHolder(@NonNull View itemView) {
            super(itemView);

            idSocialMedia = itemView.findViewById(R.id.idSocialMediaRecyclerView);
            nameRecyclerView = itemView.findViewById(R.id.nameRecyclerView);
            //TODO: Docelowo dodać jeszcze id i awatar.
        }
    }
}
