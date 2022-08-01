package com.example.avec.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.avec.R;
import com.example.avec.util.Globals;
import com.example.avec.util.Preferences;
import com.example.avec.util.song.SongAdapter;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar to make the app fullscreen.
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        // Initialize Preferences and saved SongCollections
        Globals.pref = new Preferences(getBaseContext());
        Globals.playlists = Globals.pref.loadPlaylists();

        // Create a new Song Adapter of the global Song Registry
        SongAdapter songAdapter = new SongAdapter(Globals.songRegistry.songs);
        // Set the Song Adapter to the Recycler View
        RecyclerView recyclerView = findViewById(R.id.song_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(songAdapter);

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        EditText query = findViewById(R.id.search_query);
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                String trimmed = str.trim();
                if (!trimmed.equals(str)) {
                    query.setText(trimmed);
                    query.setSelection(trimmed.length());
                }
                songAdapter.search(query.getText().toString());
                songAdapter.notifyDataSetChanged();
            }
        });
        ImageButton search = findViewById(R.id.search);
        search.setOnClickListener(v -> query.requestFocus());
        query.requestFocus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Globals.pref.savePlaylists(Globals.playlists);
    }
}
