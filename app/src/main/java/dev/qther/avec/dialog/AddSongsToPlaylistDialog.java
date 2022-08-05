package dev.qther.avec.dialog;

import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.qther.avec.R;
import dev.qther.avec.util.Globals;
import dev.qther.avec.util.playlist.Playlist;
import dev.qther.avec.util.playlist.PlaylistSongAdapter;
import dev.qther.avec.util.song.SongSelectAdapter;

public class AddSongsToPlaylistDialog extends AvecDialog {
    private final int playlist;
    private final PlaylistSongAdapter playlistSongAdapter;

    public AddSongsToPlaylistDialog(Context ctx, int playlist, PlaylistSongAdapter playlistSongAdapter) {
        this.playlist = playlist;
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_add_songs_to_playlist);
        this.dialog = db.create();
        this.playlistSongAdapter = playlistSongAdapter;
    }

    public void show() {
        dialog.show();

        // Create a new Playlist Adapter of the global Song Registry
        SongSelectAdapter songSelectAdapter = new SongSelectAdapter(Globals.playlists.get(playlist).getSongs());
        // Set the Playlist Adapter to the Recycler View
        RecyclerView recyclerView = dialog.findViewById(R.id.astpl_list);
        Log.d("AddSongsToPlaylistDialog", "Recycler View: " + recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(dialog.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(songSelectAdapter);

        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.confirm).setOnClickListener(v -> {
            boolean[] selected = songSelectAdapter.getSelected();
            Playlist pl = Globals.playlists.get(playlist);
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    pl.add(i);
                    this.playlistSongAdapter.notifyItemInserted(pl.size());
                }
            }
            succeed();
        });
    }
}
