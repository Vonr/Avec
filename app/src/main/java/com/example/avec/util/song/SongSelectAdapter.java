package com.example.avec.util.song;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.util.Globals;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class SongSelectAdapter extends RecyclerView.Adapter {
    int[] playlist;
    private boolean[] selected;

    public SongSelectAdapter(int[] songs) {
        this.playlist = songs;
        this.selected = new boolean[Globals.songRegistry.songs.size()];
        for (int i = 0; i < selected.length; i++) {
            int finalI = i;
            this.selected[i] = Arrays.stream(songs).anyMatch(x -> x == finalI);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_select_song_item, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            SongViewHolder h = (SongViewHolder) holder;
            Song song = Globals.songRegistry.songs.get(position);
            h.name.setText(song.name);
            h.artist.setText(song.artist);
            asyncFromURL(h.thumbnail, song.getThumbnailURL());

            h.container.setOnClickListener(v -> h.select.performClick());
            h.select.setChecked(selected[position]);
            h.select.setOnClickListener(v -> selected[position] = h.select.isChecked());
        }
    }

    public boolean[] getSelected() {
        return selected;
    }

    @Override
    public int getItemCount() {
        return Globals.songRegistry.songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView name;
        TextView artist;
        LinearLayout container;
        CheckBox select;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.song_name);
            artist = itemView.findViewById(R.id.artist);
            container = itemView.findViewById(R.id.holder);
            select = itemView.findViewById(R.id.song_select);
        }
    }
}
