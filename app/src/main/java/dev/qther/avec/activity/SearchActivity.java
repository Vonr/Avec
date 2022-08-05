package dev.qther.avec.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.qther.avec.R;
import dev.qther.avec.util.Globals;
import dev.qther.avec.util.Preferences;
import dev.qther.avec.util.song.SongAdapter;
import dev.qther.avec.util.song.SongRegistry;

public class SearchActivity extends AppCompatActivity {
    @SuppressLint("NotifyDataSetChanged")
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
        SongAdapter songAdapter = new SongAdapter(SongRegistry.songs);
        // Set the Song Adapter to the Recycler View
        RecyclerView recyclerView = findViewById(R.id.song_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(songAdapter);

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        EditText query = findViewById(R.id.search_query);
        query.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                songAdapter.search(query.getText().toString());
                songAdapter.notifyDataSetChanged();
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        query.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String queryText = query.getText().toString();
                if (queryText.isEmpty()) {
                    finish();
                }
                songAdapter.search(queryText);
                songAdapter.notifyDataSetChanged();
                query.clearFocus();
                imm.hideSoftInputFromWindow(query.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        ImageButton search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            if (!query.hasFocus()) {
                query.requestFocus();
                imm.showSoftInput(query, 0);
                return;
            }
            String queryText = query.getText().toString();
            if (queryText.isEmpty()) {
                finish();
            }
            query.clearFocus();
            imm.hideSoftInputFromWindow(query.getWindowToken(), 0);
            songAdapter.search(queryText);
            songAdapter.notifyDataSetChanged();
        });
        query.requestFocus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Globals.pref.savePlaylists(Globals.playlists);
    }
}
