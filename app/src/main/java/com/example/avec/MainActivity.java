package com.example.avec;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.activity.PlaylistsActivity;
import com.example.avec.activity.SearchActivity;
import com.example.avec.activity.SettingsActivity;
import com.example.avec.util.Globals;
import com.example.avec.util.Preferences;
import com.example.avec.util.song.SongAdapter;
import com.example.avec.util.song.SongRegistry;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar to make the app fullscreen.
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Initialize Preferences and saved SongCollections
        Globals.pref = new Preferences(getBaseContext());
        Globals.playlists = Globals.pref.loadPlaylists();

        // Create a new Song Adapter of the global Song Registry
        SongAdapter songAdapter = new SongAdapter(SongRegistry.songs);
        // Set the Song Adapter to the Recycler View
        RecyclerView recyclerView = findViewById(R.id.song_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(songAdapter);

        initButtons();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void initButtons() {
        ConstraintLayout menu = findViewById(R.id.menu);
        ImageButton menu_button = findViewById(R.id.menu_button);
        menu_button.setOnClickListener(v -> {
            menu.setVisibility(View.VISIBLE);
            menu.bringToFront();
            menu.forceLayout();
        });
        Button menu_out = findViewById(R.id.menu_out);
        menu_out.setOnClickListener(v -> menu.setVisibility(View.INVISIBLE));
        ImageButton menu_close = findViewById(R.id.menu_close);
        menu_close.setOnClickListener(v -> menu.setVisibility(View.INVISIBLE));
        Button menu_discover = findViewById(R.id.menu_discover);
        menu_discover.setOnClickListener(v -> menu.setVisibility(View.INVISIBLE));
        Button menu_playlists = findViewById(R.id.menu_playlists);
        menu_playlists.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlaylistsActivity.class);
            startActivity(intent);
        });
        Button menu_settings = findViewById(R.id.menu_settings);
        menu_settings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        ImageButton search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Globals.pref.savePlaylists(Globals.playlists);
    }
}
