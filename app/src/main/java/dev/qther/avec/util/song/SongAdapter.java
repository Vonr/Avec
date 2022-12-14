package dev.qther.avec.util.song;

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
import dev.qther.avec.R;
import dev.qther.avec.activity.PlaySongActivity;
import dev.qther.avec.dialog.AddToPlaylistDialog;
import dev.qther.avec.util.AutoHolder;
import dev.qther.avec.util.Globals;
import dev.qther.avec.util.playlist.Playlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.qther.avec.util.ImageLoader.asyncFromURL;

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
                .inflate(R.layout.item_song, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            SongViewHolder h = (SongViewHolder) holder;
            Song song = sorted.get(position);
            h.name.setText(song.name);
            h.artist.setText(song.artist);
            asyncFromURL(h.thumbnail, song.getThumbnailURL());

            h.holder.setOnClickListener(v -> playSong(v.getContext(), song.index));
            h.thumbnail.setOnClickListener(v -> playSong(v.getContext(), song.index));

            h.add.setOnClickListener(v -> {
                AddToPlaylistDialog dialog = new AddToPlaylistDialog(v.getContext(), song.index);
                dialog.setSuccessCallback(() -> notifyDataSetChanged());
                dialog.show();
            });

            Playlist favourites = Globals.playlists.stream().filter(pl -> "Your Favourites".equals(pl.name)).findFirst().orElse(null);
            if (favourites.contains(song.index)) {
                h.like.setAlpha(1f);
            } else {
                h.like.setAlpha(0.67f);
            }
            h.like.setOnClickListener(v -> {
                if (favourites.contains(song.index)) {
                    favourites.remove(song.index);
                    h.like.setAlpha(0.67f);
                } else {
                    favourites.add(song.index);
                    h.like.setAlpha(1f);
                }
                Log.d("SongAdapter", "favourites: " + favourites);
            });
        }
    }

    private void playSong(Context ctx, int index) {
        Intent intent = new Intent(ctx, PlaySongActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("songs", sorted.stream().mapToInt(s -> s.index).toArray());

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
        TextView name;
        TextView artist;
        LinearLayout holder;
        ImageButton add;
        ImageButton like;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
