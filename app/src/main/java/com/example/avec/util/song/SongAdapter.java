package com.example.avec.util.song;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.avec.util.AutoHolder;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class SongAdapter extends RecyclerView.Adapter {
    ArrayList<Song> playlist;
    String query;
    ArrayList<Song> sorted;

    public SongAdapter(ArrayList<Song> playlist) {
        this.playlist = playlist;
        this.query = "";
        this.sorted = new ArrayList<>(playlist);
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
            Song song = sorted.get(position);
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
                AddToPlaylistDialog dialog = new AddToPlaylistDialog(v.getContext(), song.index);
                dialog.show();
            });

            h.song_like.setOnClickListener(v -> {
                if (favourites.contains(song.index)) {
                    favourites.remove(song.index);
                    h.song_like.setAlpha(0.5f);
                } else {
                    favourites.add(song.index);
                    h.song_like.setAlpha(1f);
                }
            });
        }
    }

    private void playSong(Context ctx, int index) {
        Intent intent = new Intent(ctx, PlaySongActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("songs", playlist.stream().mapToInt(s -> s.index).toArray());

        ctx.startActivity(intent);
    }

    public void search(String query) {
        Log.d("SongAdapter", "Old Sorted: " + Arrays.toString(sorted.stream().map(s -> s.name).toArray()));
        if (query.isEmpty()) {
            sorted.clear();
            sorted.addAll(playlist);
            return;
        }
        List<Song> newSearch = SongSearcher.search(query, playlist);
        sorted.clear();
        sorted.addAll(newSearch);
        Log.d("SongAdapter", "New Sorted: " + Arrays.toString(sorted.stream().map(s -> s.name).toArray()));
    }

    @Override
    public int getItemCount() {
        return sorted.size();
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
