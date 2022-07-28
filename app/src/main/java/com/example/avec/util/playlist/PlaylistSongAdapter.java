package com.example.avec.util.playlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.activity.PlaySongActivity;
import com.example.avec.dialog.AddToPlaylistDialog;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;
import com.example.avec.util.song.Song;

import java.util.ArrayList;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class PlaylistSongAdapter extends RecyclerView.Adapter {
    String name;
    Playlist playlist;
    ArrayList<Integer> toRemove = new ArrayList<>();

    public PlaylistSongAdapter(String name, Playlist songs) {
        this.name = name;
        this.playlist = songs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            SongViewHolder h = (SongViewHolder) holder;
            int index = playlist.getSongs()[position];
            Song song = Globals.songRegistry.songs.get(index);
            h.name.setText(song.name);
            h.artist.setText(song.artist);
            asyncFromURL(h.thumbnail, song.getThumbnailURL());

            h.container.setOnClickListener(v -> playSong(v.getContext(), song.index));
            h.thumbnail.setOnClickListener(v -> playSong(v.getContext(), song.index));

            Playlist favourites = Globals.playlists.get(0);
            if (favourites.contains(song.index)) {
                h.like.setAlpha(1f);
            } else {
                h.like.setAlpha(0.5f);
            }

            h.add.setOnClickListener(v -> {
                if (toRemove.contains(song.index)) {
                    // This warning should not be followed as it will otherwise attempt removal by index.
                    toRemove.remove(toRemove.indexOf(song.index));
                    h.add.setAlpha(1f);
                    h.add.setImageResource(R.drawable.playlist_add_check);
                    if (name.equals("Your Favourites")) {
                        favourites.add(song.index);
                        h.like.setAlpha(1f);
                    }
                } else {
                    toRemove.add(song.index);
                    h.add.setAlpha(0.5f);
                    h.add.setImageResource(R.drawable.playlist_add);
                    if (name.equals("Your Favourites")) {
                        favourites.remove(song.index);
                        h.like.setAlpha(0.5f);
                    }
                }
            });

            h.like.setOnClickListener(v -> {
                if (favourites.contains(song.index)) {
                    favourites.remove(song.index);
                    h.like.setAlpha(0.5f);
                    toRemove.add(song.index);
                    h.add.setAlpha(0.5f);
                    h.add.setImageResource(R.drawable.playlist_add);
                } else {
                    favourites.add(song.index);
                    h.like.setAlpha(1f);
                    // This warning should not be followed as it will otherwise attempt removal by index.
                    toRemove.remove(toRemove.indexOf(song.index));
                    h.add.setAlpha(1f);
                    h.add.setImageResource(R.drawable.playlist_add_check);
                }
            });
        }
    }

    private void playSong(Context ctx, int index) {
        Intent intent = new Intent(ctx, PlaySongActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("songs", playlist.getSongs());

        ctx.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView name;
        TextView artist;
        LinearLayout container;
        ImageButton add;
        ImageButton like;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.song_name);
            artist = itemView.findViewById(R.id.artist);
            container = itemView.findViewById(R.id.holder);
            add = itemView.findViewById(R.id.song_add);
            like = itemView.findViewById(R.id.song_like);
        }
    }
}
