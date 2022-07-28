package com.example.avec.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.example.avec.R;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;

import java.util.ArrayList;

public class RemovePlaylistDialog {
    private final AlertDialog dialog;
    private final Playlist playlist;
    private Runnable successCallback = null;

    public RemovePlaylistDialog(Context ctx, Playlist playlist) {
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_remove_playlist);
        dialog = db.create();
        this.playlist = playlist;
    }

    public void show() {
        dialog.show();
        TextView title = dialog.findViewById(R.id.dpl_title);
        title.setText("Remove Playlist " + playlist.name);
        dialog.findViewById(R.id.dpl_cancel).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.dpl_confirm).setOnClickListener(v -> {
            Globals.playlists.remove(playlist);
            Globals.pref.savePlaylists();
            successCallback.run();
            dialog.dismiss();
        });
    }

    public void setSuccessCallback(Runnable callback) {
        this.successCallback = callback;
    }
}
