package com.example.avec.util.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.util.AutoHolder;

import java.util.Arrays;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class SongSelectAdapter extends RecyclerView.Adapter {
    int[] playlist;
    private final boolean[] selected;

    public SongSelectAdapter(int[] songs) {
        this.playlist = songs;
        this.selected = new boolean[SongRegistry.songs.size()];
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
            Song song = SongRegistry.songs.get(position);
            h.song_name.setText(song.name);
            h.artist.setText(song.artist);
            asyncFromURL(h.thumbnail, song.getThumbnailURL());

            h.holder.setOnClickListener(v -> h.song_select.performClick());
            h.song_select.setChecked(selected[position]);
            h.song_select.setOnClickListener(v -> selected[position] = h.song_select.isChecked());
        }
    }

    public boolean[] getSelected() {
        return selected;
    }

    @Override
    public int getItemCount() {
        return SongRegistry.songs.size();
    }

    static class SongViewHolder extends AutoHolder {
        ImageView thumbnail;
        TextView song_name;
        TextView artist;
        LinearLayout holder;
        CheckBox song_select;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
