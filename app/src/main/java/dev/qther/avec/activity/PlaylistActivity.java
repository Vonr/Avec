package dev.qther.avec.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.qther.avec.R;
import dev.qther.avec.dialog.AddSongsToPlaylistDialog;
import dev.qther.avec.util.Globals;
import dev.qther.avec.util.playlist.Playlist;
import dev.qther.avec.util.playlist.PlaylistSongAdapter;

public class PlaylistActivity extends AppCompatActivity {
    PlaylistSongAdapter playlistSongAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the index of the playlist from the intent
        int index = getIntent().getIntExtra("index", -1);
        if (index == -1) {
            Log.e("PlaylistActivity", "No index provided");
            return;
        }

        // Populate and hide ActionBar
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_playlist);

        // Get playlist
        Playlist pl = Globals.playlists.get(index);

        // Set the title of the screen to the playlist's name
        EditText title = findViewById(R.id.pl_title);
        title.setText(pl.name);
        title.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if ("Your Favourites".equals(pl.name)) {
                    if (!"Your Favourites".equals(s.toString())) {
                        title.setText("Your Favourites");
                    }
                    title.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                    Toast.makeText(PlaylistActivity.this, "You can't change the name of your favourites playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        title.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String newTitle = title.getText().toString();
                if (newTitle.isEmpty()) {
                    Toast.makeText(PlaylistActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (Globals.playlists.stream().anyMatch(p -> p.name.equals(newTitle))) {
                    Toast.makeText(PlaylistActivity.this, "A playlist with that name already exists", Toast.LENGTH_SHORT).show();
                    return true;
                }
                pl.name = newTitle;
                Globals.pref.savePlaylists();
                title.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        // Create a new Song Adapter of the global Song Registry
        playlistSongAdapter = new PlaylistSongAdapter(pl.name, pl);
        // Set the Song Adapter to the Recycler View
        RecyclerView recyclerView = findViewById(R.id.pl_songs);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playlistSongAdapter);

        // Set the onClickListener for the add button
        ImageButton add = findViewById(R.id.pl_add_songs);
        AddSongsToPlaylistDialog dialog = new AddSongsToPlaylistDialog(this, index, playlistSongAdapter);
        add.setOnClickListener(v -> dialog.show());
    }

    @Override
    public void finish() {
        // Clean up
        playlistSongAdapter.remove();

        // Go back to the previous activity, but with an intent to refresh the playlist list
        Intent intent = new Intent(this, PlaylistsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onClick(View v) {
        finish();
    }
}
