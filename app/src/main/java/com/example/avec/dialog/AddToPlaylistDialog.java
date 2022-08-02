package com.example.avec.dialog;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;
import com.example.avec.util.playlist.PlaylistSelectAdapter;
import com.example.avec.util.song.SongRegistry;

public class AddToPlaylistDialog extends AvecDialog {
    private final int song;

    public AddToPlaylistDialog(Context ctx, int song) {
        this.song = song;
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_add_to_playlist);
        this.dialog = db.create();
    }

    public void show() {
        dialog.show();
        clear();

        TextView title = dialog.findViewById(R.id.atpl_title);
        title.setText("Add " + SongRegistry.songs.get(song).name);
        // Create a new Playlist Adapter of the global Song Registry
        PlaylistSelectAdapter playlistSelectAdapter = new PlaylistSelectAdapter(Globals.playlists, song);
        // Set the Playlist Adapter to the Recycler View
        RecyclerView recyclerView = dialog.findViewById(R.id.atpl_list);
        Log.d("AddToPlaylistDialog", "Recycler View: " + recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(dialog.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playlistSelectAdapter);

        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.confirm).setOnClickListener(v -> {
            for (Playlist playlist : Globals.playlists) {
                if (playlistSelectAdapter.isSelected(playlist)) {
                    playlist.add(song);
                    Globals.pref.savePlaylists();
                }
            }
            succeed();
        });
    }

    private void clear() {
        TextView title = dialog.findViewById(R.id.atpl_title);
        title.setText("");
    }
}
