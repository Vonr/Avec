package com.example.avec.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.example.avec.R;

public class LyricsDialog extends AvecDialog {
    String lyrics;

    public LyricsDialog(Context ctx) {
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_lyrics);
        this.dialog = db.create();
    }

    public void setLyrics(String lyrics) {
        this.lyrics = !"".equals(lyrics.trim()) ? lyrics : "Lyrics not available.";

        if (dialog.isShowing()) {
            succeed();
        }
    }

    public void show() {
        dialog.show();

        TextView lyricsView = dialog.findViewById(R.id.lyrics);
        lyricsView.setMovementMethod(new ScrollingMovementMethod());
        lyricsView.setText(lyrics);
        dialog.findViewById(R.id.done).setOnClickListener(v -> succeed());
    }
}
