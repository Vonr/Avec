package dev.qther.avec.util.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.qther.avec.R;
import dev.qther.avec.util.AutoHolder;

import java.util.ArrayList;

import static dev.qther.avec.util.ImageLoader.asyncFromURL;

public class PlaylistSelectAdapter extends RecyclerView.Adapter {
    private ArrayList<Playlist> playlists;
    private final boolean[] selected;

    public PlaylistSelectAdapter(ArrayList<Playlist> playlists, int song) {
        this.playlists = playlists;
        this.selected = new boolean[playlists.size()];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = playlists.get(i).contains(song);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_select, parent, false);

        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistViewHolder) {
            PlaylistViewHolder h = (PlaylistViewHolder) holder;
            Playlist pl = playlists.get(position);
            int size = pl.size();
            h.select.setChecked(selected[position]);
            h.name.setText(pl.name);
            h.name.setOnClickListener(v -> {
                boolean newState = !h.select.isChecked();
                h.select.setChecked(newState);
                selected[position] = newState;
            });
            h.select.setOnClickListener(v -> selected[position] = h.select.isChecked());

            // Load images in background
            if (size < 1) return;
            asyncFromURL(h.thumbnail1, pl.get(0).getThumbnailURL());
            if (size < 2) return;
            asyncFromURL(h.thumbnail2, pl.get(1).getThumbnailURL());
            if (size < 3) return;
            asyncFromURL(h.thumbnail3, pl.get(2).getThumbnailURL());
            if (size < 4) return;
            asyncFromURL(h.thumbnail4, pl.get(3).getThumbnailURL());
        }
    }

    public boolean isSelected(Playlist playlist) {
        return selected[playlists.indexOf(playlist)];
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class PlaylistViewHolder extends AutoHolder {
        ImageView thumbnail1;
        ImageView thumbnail2;
        ImageView thumbnail3;
        ImageView thumbnail4;
        TextView name;
        CheckBox select;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
