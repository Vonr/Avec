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
import com.example.avec.util.AutoHolder;
import com.example.avec.util.Globals;
import com.example.avec.util.song.Song;
import com.example.avec.util.song.SongRegistry;

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
                .inflate(R.layout.item_song, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            SongViewHolder h = (SongViewHolder) holder;
            int index = playlist.getSongs()[position];
            Song song = SongRegistry.songs.get(index);
            h.song_name.setText(song.name);
            h.artist.setText(song.artist);
            asyncFromURL(h.thumbnail, song.getThumbnailURL());

            h.holder.setOnClickListener(v -> playSong(v.getContext(), song.index));
            h.thumbnail.setOnClickListener(v -> playSong(v.getContext(), song.index));

            Playlist favourites = Globals.playlists.get(0);
            if (favourites.contains(song.index)) {
                h.song_like.setAlpha(1f);
            } else {
                h.song_like.setAlpha(0.5f);
            }

            h.song_add.setOnClickListener(v -> {
                if (toRemove.contains(song.index)) {
                    // This warning should not be followed as it will otherwise attempt removal by index.
                    toRemove.remove(toRemove.indexOf(song.index));
                    h.song_add.setAlpha(1f);
                    h.song_add.setImageResource(R.drawable.playlist_add_check);
                    if (name.equals("Your Favourites")) {
                        favourites.add(song.index);
                        h.song_like.setAlpha(1f);
                    }
                } else {
                    toRemove.add(song.index);
                    h.song_add.setAlpha(0.5f);
                    h.song_add.setImageResource(R.drawable.playlist_add);
                    if (name.equals("Your Favourites")) {
                        favourites.remove(song.index);
                        h.song_like.setAlpha(0.5f);
                    }
                }
            });
            if (playlist.contains(song.index)) {
                h.song_add.setAlpha(1f);
                h.song_add.setImageResource(R.drawable.playlist_add_check);
            } else {
                h.song_add.setAlpha(0.5f);
                h.song_add.setImageResource(R.drawable.playlist_add);
            }

            h.song_like.setOnClickListener(v -> {
                if (favourites.contains(song.index)) {
                    favourites.remove(song.index);
                    h.song_like.setAlpha(0.5f);
                    toRemove.add(song.index);
                    h.song_add.setAlpha(0.5f);
                    h.song_add.setImageResource(R.drawable.playlist_add);
                } else {
                    favourites.add(song.index);
                    h.song_like.setAlpha(1f);
                    // This warning should not be followed as it will otherwise attempt removal by index.
                    toRemove.remove(toRemove.indexOf(song.index));
                    h.song_add.setAlpha(1f);
                    h.song_add.setImageResource(R.drawable.playlist_add_check);
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

    public void remove() {
        // This warning should not be followed as it will otherwise attempt removal by index.
        for (int i = 0; i < toRemove.size(); i++) {
            playlist.remove(toRemove.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    static class SongViewHolder extends AutoHolder {
        ImageView thumbnail;
        TextView song_name;
        TextView artist;
        LinearLayout holder;
        ImageButton song_add;
        ImageButton song_like;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
