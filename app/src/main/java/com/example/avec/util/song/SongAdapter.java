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
import com.example.avec.dialog.CreateNewPlaylistDialog;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;

import java.util.ArrayList;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class SongAdapter extends RecyclerView.Adapter {
    int[] playlist;

    public SongAdapter(int[] songs) {
        this.playlist = songs;
    }

    public SongAdapter(ArrayList<Song> songCollection) {
        int[] indices = new int[songCollection.size()];
        for (int i = 0; i < songCollection.size(); i++) {
            indices[i] = songCollection.get(i).index;
        }
        this.playlist = indices;
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
            int index = playlist[position];
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
                AddToPlaylistDialog dialog = new AddToPlaylistDialog(v.getContext(), playlist[position]);
                dialog.show();
            });

            h.like.setOnClickListener(v -> {
                if (favourites.contains(song.index)) {
                    favourites.remove(song.index);
                    h.like.setAlpha(0.5f);
                } else {
                    favourites.add(song.index);
                    h.like.setAlpha(1f);
                }
            });
        }
    }

    private void playSong(Context ctx, int index) {
        Intent intent = new Intent(ctx, PlaySongActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("songs", playlist);

        ctx.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return playlist.length;
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
