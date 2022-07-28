package com.example.avec.dialog;

import android.content.Context;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.example.avec.R;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;

public class RemovePlaylistDialog extends AvecDialog {
    private final Playlist playlist;

    public RemovePlaylistDialog(Context ctx, Playlist playlist, Runnable successCallback) {
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_remove_playlist);
        dialog = db.create();
        this.playlist = playlist;
        this.successCallback = successCallback;
    }

    public void show() {
        dialog.show();
        TextView title = dialog.findViewById(R.id.dpl_title);
        title.setText("Remove Playlist " + playlist.name);
        dialog.findViewById(R.id.dpl_cancel).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.dpl_confirm).setOnClickListener(v -> {
            Globals.playlists.remove(playlist);
            Globals.pref.savePlaylists();
            succeed();
        });
    }
}
