package com.example.avec.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.dialog.CreateNewPlaylistDialog;
import com.example.avec.util.Globals;
import com.example.avec.util.playlist.PlaylistAdapter;

public class PlaylistsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar to make the app fullscreen.
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_playlists);

        // Sort Global.playlists by pinned and name
        Globals.playlists.sort((playlist, t1) -> {
            if (playlist.pinned && "Your Favourites".equals(playlist.name)) {
                return -1;
            } else if (playlist.pinned && !t1.pinned) {
                return -1;
            } else if (!playlist.pinned && t1.pinned) {
                return 1;
            } else if ("Your Favourites".equals(playlist.name)) {
                return -1;
            } else {
                return playlist.name.compareTo(t1.name);
            }
        });

        // Create a new Playlist Adapter of the global Song Registry
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(Globals.playlists);
        // Set the Playlist Adapter to the Recycler View
        RecyclerView recyclerView = findViewById(R.id.playlists);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playlistAdapter);

        // Set the onClickListener for the back button
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Set the onClickListener for the add button
        ImageButton add = findViewById(R.id.pl_add);
        CreateNewPlaylistDialog dialog = new CreateNewPlaylistDialog(this, () -> {
            playlistAdapter.notifyItemInserted(Globals.playlists.size());
            manager.requestLayout();
        });
        add.setOnClickListener(v -> dialog.show());
    }
}
