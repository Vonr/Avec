package com.example.avec.util.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.util.AutoHolder;

import java.util.ArrayList;

import static com.example.avec.util.ImageLoader.asyncFromURL;

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
            h.pl_select.setChecked(selected[position]);
            h.pl_name.setText(pl.name);
            h.pl_name.setOnClickListener(v -> {
                boolean newState = !h.pl_select.isChecked();
                h.pl_select.setChecked(newState);
                selected[position] = newState;
            });
            h.pl_select.setOnClickListener(v -> selected[position] = h.pl_select.isChecked());

            if (size > 0) {
                asyncFromURL(h.pl_tb1, pl.get(0).getThumbnailURL());
                if (size < 2) return;
                asyncFromURL(h.pl_tb2, pl.get(1).getThumbnailURL());
                if (size < 3) return;
                asyncFromURL(h.pl_tb3, pl.get(2).getThumbnailURL());
                if (size < 4) return;
                asyncFromURL(h.pl_tb4, pl.get(3).getThumbnailURL());
            }
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
        ImageView pl_tb1;
        ImageView pl_tb2;
        ImageView pl_tb3;
        ImageView pl_tb4;
        TextView pl_name;
        CheckBox pl_select;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
