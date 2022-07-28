package com.example.avec.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.dialog.AddSongsToPlaylistDialog;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.Playlist;
import com.example.avec.util.playlist.PlaylistSongAdapter;

public class PlaylistActivity extends AppCompatActivity {
    PlaylistSongAdapter playlistSongAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int index = getIntent().getIntExtra("index", -1);
        if (index == -1) {
            Log.e("PlaylistActivity", "No index provided");
            return;
        }

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_playlist);

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

        ImageButton add = findViewById(R.id.pl_add_songs);
        AddSongsToPlaylistDialog dialog = new AddSongsToPlaylistDialog(this, index, playlistSongAdapter);
        add.setOnClickListener(v -> dialog.show());
    }

    @Override
    public void finish() {
        super.finish();
        playlistSongAdapter.remove();
    }

    public void onClick(View v) {
        finish();
    }
}
