package com.example.avec.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import com.example.avec.R;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CreateNewPlaylistDialog extends AvecDialog {
    public CreateNewPlaylistDialog(Context ctx, Runnable successCallback) {
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_create_playlist);
        this.dialog = db.create();
        setSuccessCallback(successCallback);
    }

    public void show() {
        dialog.show();
        clear();

        ArrayList<String> playlistNames = Globals.playlists
                .stream()
                .map(p -> p.name)
                .collect(Collectors.toCollection(ArrayList::new));

        EditText nameField = dialog.findViewById(R.id.cpl_name);
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newName = charSequence.toString();
                if (newName.isEmpty() || newName.equals("Your Favourites") || playlistNames.contains(newName)) {
                    nameField.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    nameField.setTextColor(Color.parseColor("#ababab"));
                    nameField.setHintTextColor(Color.parseColor("#9a9a9a"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.confirm).setOnClickListener(v -> {
            String newName = nameField.getText().toString();

            if (!newName.isEmpty() && !newName.equals("Your Favourites") && !playlistNames.contains(newName)) {
                Playlist pl = new Playlist(newName);
                pl.pinned = ((CheckBox) dialog.findViewById(R.id.cpl_pinned)).isChecked();
                Globals.playlists.add(pl);
                Globals.pref.savePlaylists();
                succeed();
            }
        });
    }

    private void clear() {
        EditText nameField = dialog.findViewById(R.id.cpl_name);
        nameField.setText("");
    }
}
