package com.example.avec.util.playlist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.activity.PlaylistActivity;
import com.example.avec.dialog.CreateNewPlaylistDialog;
import com.example.avec.dialog.RemovePlaylistDialog;
import com.example.avec.util.Globals;

import java.util.ArrayList;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class PlaylistAdapter extends RecyclerView.Adapter {
    ArrayList<Playlist> playlists;

    public PlaylistAdapter(ArrayList<Playlist> playlist) {
        this.playlists = playlist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);

        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistViewHolder) {
            PlaylistViewHolder h = (PlaylistViewHolder) holder;
            Playlist pl = playlists.get(position);
            Log.d("PlaylistAdapter", "onBindViewHolder: " + pl.name);
            int size = pl.size();
            h.name.setText(pl.name);
            h.button.setOnClickListener(v -> {
                Log.d("PlaylistAdapter", "onBindViewHolder: " + pl.name);
                enterPlaylist(v.getContext(), pl);
            });
//            h.grid.setOnClickListener(v -> enterPlaylist(v.getContext(), pl));
            h.pin.setAlpha(pl.pinned ? 1f : 0.5f);
            h.pin.setOnClickListener(v -> {
                pl.pinned = !pl.pinned;
                h.pin.setAlpha(pl.pinned ? 1f : 0.5f);
                Globals.pref.savePlaylists(Globals.playlists);
            });

            if (pl.name.equals("Your Favourites")) {
                h.remove.setEnabled(false);
                h.remove.setVisibility(View.INVISIBLE);
            }

            h.remove.setOnClickListener(v -> {
                RemovePlaylistDialog dialog = new RemovePlaylistDialog(v.getContext(), pl);
                dialog.setSuccessCallback(() -> notifyItemRemoved(position));
                dialog.show();
            });

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

    private void enterPlaylist(Context ctx, Playlist pl) {
        Intent intent = new Intent(ctx, PlaylistActivity.class);
        int index = -1;
        for (int i = 0; i < Globals.playlists.size(); i++) {
            if (Globals.playlists.get(i) == pl) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            Log.e("PlaylistAdapter", "Playlist not found in Globals.playlists");
            return;
        }
        intent.putExtra("index", index);

        Log.d("PlaylistAdapter", "Entering playlist " + pl.name);
        ctx.startActivity(intent);
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
        ImageButton pin;
        ImageButton remove;
        Button button;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            tb1 = itemView.findViewById(R.id.pl_tb1);
            tb2 = itemView.findViewById(R.id.pl_tb2);
            tb3 = itemView.findViewById(R.id.pl_tb3);
            tb4 = itemView.findViewById(R.id.pl_tb4);
            name = itemView.findViewById(R.id.pl_name);
            pin = itemView.findViewById(R.id.pl_pin);
            remove = itemView.findViewById(R.id.pl_remove);
            button = itemView.findViewById(R.id.pl_button);
        }
    }
}
