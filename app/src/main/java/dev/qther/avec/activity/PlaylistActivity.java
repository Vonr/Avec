package dev.qther.avec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
        TextView title = findViewById(R.id.pl_title);
        title.setText(pl.name);

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
