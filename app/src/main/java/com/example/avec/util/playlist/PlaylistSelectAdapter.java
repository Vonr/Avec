package com.example.avec.util.playlist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class PlaylistSelectAdapter extends RecyclerView.Adapter {
    private ArrayList<Playlist> playlists;
    private boolean[] selected;

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
                .inflate(R.layout.playlist_select_item, parent, false);

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

            if (size > 0) {
                asyncFromURL(h.tb1, pl.get(0).getThumbnailURL());
                if (size < 2) return;
                asyncFromURL(h.tb2, pl.get(1).getThumbnailURL());
                if (size < 3) return;
                asyncFromURL(h.tb3, pl.get(2).getThumbnailURL());
                if (size < 4) return;
                asyncFromURL(h.tb4, pl.get(3).getThumbnailURL());
            }
        }
    }

    public boolean isSelected(Playlist playlist) {
        Log.d("PlaylistSelectAdapter", "isSelected: " + playlist.name + " of " + playlists);
        Log.d("PlaylistSelectAdapter", "isSelected: " + Arrays.toString(selected));
        return selected[playlists.indexOf(playlist)];
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView tb1;
        ImageView tb2;
        ImageView tb3;
        ImageView tb4;
        TextView name;
        CheckBox select;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            tb1 = itemView.findViewById(R.id.pl_tb1);
            tb2 = itemView.findViewById(R.id.pl_tb2);
            tb3 = itemView.findViewById(R.id.pl_tb3);
            tb4 = itemView.findViewById(R.id.pl_tb4);
            name = itemView.findViewById(R.id.pl_name);
            select = itemView.findViewById(R.id.pl_select);
        }
    }
}
