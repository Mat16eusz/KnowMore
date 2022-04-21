package com.mateusz.jasiak.knowmore;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayerFriendAdapterRecyclerView extends RecyclerView.Adapter<PlayerFriendAdapterRecyclerView.PlayerFriendViewHolder> {

    private ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViews;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class PlayerFriendViewHolder extends RecyclerView.ViewHolder {

        public TextView idSocialMedia;
        public TextView nameRecyclerView;
        public ImageView avatar;

        public PlayerFriendViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            idSocialMedia = itemView.findViewById(R.id.idSocialMediaRecyclerView);
            nameRecyclerView = itemView.findViewById(R.id.nameRecyclerView);
            avatar = itemView.findViewById(R.id.avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public PlayerFriendAdapterRecyclerView(ArrayList<PlayerFriendRecyclerView> playerFriendRecyclerViewArrayList) {
        playerFriendRecyclerViews = playerFriendRecyclerViewArrayList;
    }

    @Override
    public PlayerFriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_friend, viewGroup, false);
        PlayerFriendViewHolder playerFriendViewHolder = new PlayerFriendViewHolder(view, onItemClickListener);

        return playerFriendViewHolder;
    }

    @Override
    public void onBindViewHolder(PlayerFriendViewHolder holder, int position) {
        PlayerFriendRecyclerView currentItem = playerFriendRecyclerViews.get(position);

        holder.idSocialMedia.setText(currentItem.getIdSocialMediaRecyclerView());
        holder.nameRecyclerView.setText(currentItem.getNameRecyclerView());
        //TODO: Update w API adresu do awataru -> obecnie gdy użytkownik zmieni awatar na API pozoostaje link z pierwszego logowania
        if (currentItem.getAvatar().equals("")) {
            Picasso.get().load(R.drawable.avatar).into(holder.avatar);
        } else {
            Picasso.get().load("https://lh3.googleusercontent.com" + currentItem.getAvatar()).into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return playerFriendRecyclerViews.size();
    }
}
