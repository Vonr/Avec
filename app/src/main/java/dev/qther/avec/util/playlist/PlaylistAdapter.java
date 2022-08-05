package dev.qther.avec.util.playlist;

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
import dev.qther.avec.R;
import dev.qther.avec.activity.PlaylistActivity;
import dev.qther.avec.dialog.RemovePlaylistDialog;
import dev.qther.avec.util.AutoHolder;
import dev.qther.avec.util.Globals;

import java.util.ArrayList;

import static dev.qther.avec.util.ImageLoader.asyncFromURL;

public class PlaylistAdapter extends RecyclerView.Adapter {
    ArrayList<Playlist> playlists;

    public PlaylistAdapter(ArrayList<Playlist> playlist) {
        this.playlists = playlist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistViewHolder) {
            PlaylistViewHolder h = (PlaylistViewHolder) holder;
            Playlist pl = playlists.get(position);
            Log.d("PlaylistAdapter", "onBindViewHolder: " + pl.name);
            int size = pl.size();
            h.pl_name.setText(pl.name);
            h.pl_button.setOnClickListener(v -> {
                Log.d("PlaylistAdapter", "onBindViewHolder: " + pl.name);
                enterPlaylist(v.getContext(), pl);
            });
            h.pl_pin.setAlpha(pl.pinned ? 1f : 0.5f);
            h.pl_pin.setOnClickListener(v -> {
                pl.pinned = !pl.pinned;
                h.pl_pin.setAlpha(pl.pinned ? 1f : 0.5f);
                Globals.pref.savePlaylists(Globals.playlists);
            });

            if (pl.name.equals("Your Favourites")) {
                h.pl_remove.setEnabled(false);
                h.pl_remove.setVisibility(View.INVISIBLE);
            }

            h.pl_remove.setOnClickListener(v -> {
                RemovePlaylistDialog dialog = new RemovePlaylistDialog(v.getContext(), pl, () -> notifyItemRemoved(position));
                dialog.show();
            });

            if (size < 1) return;
            asyncFromURL(h.pl_tb1, pl.get(0).getThumbnailURL());
            if (size < 2) return;
            asyncFromURL(h.pl_tb2, pl.get(1).getThumbnailURL());
            if (size < 3) return;
            asyncFromURL(h.pl_tb3, pl.get(2).getThumbnailURL());
            if (size < 4) return;
            asyncFromURL(h.pl_tb4, pl.get(3).getThumbnailURL());
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

    static class PlaylistViewHolder extends AutoHolder {
        ImageView pl_tb1;
        ImageView pl_tb2;
        ImageView pl_tb3;
        ImageView pl_tb4;
        TextView pl_name;
        ImageButton pl_pin;
        ImageButton pl_remove;
        Button pl_button;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
