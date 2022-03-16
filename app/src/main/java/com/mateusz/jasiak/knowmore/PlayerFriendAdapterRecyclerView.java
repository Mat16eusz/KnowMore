package com.mateusz.jasiak.knowmore;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerFriendAdapterRecyclerView extends RecyclerView.Adapter<PlayerFriendAdapterRecyclerView.PlayerFriendViewHolder> {

    private ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViews;

    public static class PlayerFriendViewHolder extends RecyclerView.ViewHolder {

        public TextView idSocialMedia;
        public TextView nameRecyclerView;

        public PlayerFriendViewHolder(View itemView) {
            super(itemView);

            idSocialMedia = itemView.findViewById(R.id.idSocialMediaRecyclerView);
            nameRecyclerView = itemView.findViewById(R.id.nameRecyclerView);
        }
    }

    public PlayerFriendAdapterRecyclerView(ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViewArrayList) {
        playerFriendRecyclerViews = playerFriendRecyclerViewArrayList;
    }

    @Override
    public PlayerFriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_friend, viewGroup, false);
        PlayerFriendViewHolder playerFriendViewHolder = new PlayerFriendViewHolder(view);

        return playerFriendViewHolder;
    }

    @Override
    public void onBindViewHolder(PlayerFriendViewHolder holder, int position) {
        PlayerFriendRecyclerView currentItem = playerFriendRecyclerViews.get(position);

        holder.idSocialMedia.setText(currentItem.getIdSocialMediaRecyclerView());
        holder.nameRecyclerView.setText(currentItem.getNameRecyclerView());
    }

    @Override
    public int getItemCount() {
        return playerFriendRecyclerViews.size();
    }
}
