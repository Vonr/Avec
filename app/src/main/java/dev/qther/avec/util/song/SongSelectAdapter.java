package dev.qther.avec.util.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.qther.avec.R;
import dev.qther.avec.util.AutoHolder;

import java.util.Arrays;

import static dev.qther.avec.util.ImageLoader.asyncFromURL;

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
                .inflate(R.layout.item_playlist_select_song, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            SongViewHolder h = (SongViewHolder) holder;
            Song song = SongRegistry.songs.get(position);
            h.name.setText(song.name);
            h.artist.setText(song.artist);
            asyncFromURL(h.thumbnail, song.getThumbnailURL());

            h.holder.setOnClickListener(v -> h.select.performClick());
            h.select.setChecked(selected[position]);
            h.select.setOnClickListener(v -> selected[position] = h.select.isChecked());
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
        TextView name;
        TextView artist;
        LinearLayout holder;
        CheckBox select;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
