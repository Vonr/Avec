package com.example.avec.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.avec.R;
import com.example.avec.util.Globals;
import com.example.avec.util.Preferences;
import com.example.avec.util.song.Song;
import com.example.avec.util.song.SongPlayer;
import com.example.avec.util.song.SongRegistry;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class PlaySongActivity extends AppCompatActivity {
    UpdateUI updateUI;
    Song song;
    int[] songs;
    ArrayList<Integer> history = new ArrayList<>();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the index of the song from the intent
        int index = getIntent().getIntExtra("index", -1);
        if (index == -1) {
            Log.e("PlaySongActivity", "No index provided");
            return;
        }

        // Populate and hide ActionBar
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Set content depending on right handed mode.
        if (Globals.pref.isRightHanded()) {
            setContentView(R.layout.activity_play_song_right_handed);
        } else {
            setContentView(R.layout.activity_play_song);
        }

        songs = getIntent().getIntArrayExtra("songs");
        song = SongRegistry.songs.get(index);

        playSong(index);

        // SeekBar drag behaviour
        SeekBar seekBar = findViewById(R.id.psSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    SongPlayer sp = Globals.sp;
                    sp.seek((int) (progress / 100D * sp.getDuration()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Play/Pause button behaviour
        ImageButton playPause = findViewById(R.id.psPlayPause);
        playPause.setOnClickListener(v -> {
            SongPlayer sp = Globals.sp;
            if (sp.isPlaying()) {
                sp.pause();
                playPause.setImageResource(R.drawable.play);
            } else {
                sp.unpause();
                playPause.setImageResource(R.drawable.pause);
            }
        });

        // Shuffle button behaviour
        ImageButton shuffle = findViewById(R.id.psShuffle);
        shuffle.setAlpha(Globals.pref.shouldShuffle() ? 1 : 0.5f);
        shuffle.setOnClickListener(v -> {
            boolean state = Globals.pref.shouldShuffle();
            Globals.pref.setShuffle(!state);
            shuffle.setAlpha(!state ? 1 : 0.5f);
        });

        // Repeat button behaviour
        ImageButton repeat = findViewById(R.id.psRepeat);
        repeat.setAlpha(Globals.pref.getRepeatMode() == Preferences.RepeatMode.ONE ? 1 : 0.5f);
        repeat.setOnClickListener(v -> {
            Preferences.RepeatMode state = Globals.pref.getRepeatMode();
            // Set the new state to the next RepeatMode
            Preferences.RepeatMode newMode = Globals.pref.setRepeatMode(
                    Preferences.RepeatMode.values()[
                            (state.ordinal() + 1) % Preferences.RepeatMode.values().length]);
            // Set the image to the new state
            switch (newMode) {
                case OFF:
                    repeat.setImageResource(R.drawable.repeat_one);
                    repeat.setAlpha(0.5f);
                    break;
                case ONE:
                    repeat.setImageResource(R.drawable.repeat_one);
                    repeat.setAlpha(1f);
                    break;
            }
        });

        updateUI = new UpdateUI();
        handler.post(updateUI);
    }

    private void playSong(int index) {
        // Get image button
        ImageButton playPause = findViewById(R.id.psPlayPause);
        playPause.setImageResource(R.drawable.pause);

        // Get song from index
        song = SongRegistry.songs.get(index);
        TextView name = findViewById(R.id.psName);

        // Set title to song name
        name.setText(song.name);
        // Set artist to song name
        TextView artist = findViewById(R.id.psArtist);
        artist.setText(song.artist);

        // Set album art to song album art
        ImageView thumbnail = findViewById(R.id.psThumb);
        asyncFromURL(thumbnail, song.getThumbnailURL());

        // Play song
        Globals.sp.play(song);

        // Next button
        ImageButton next = findViewById(R.id.psNext);
        next.setOnClickListener(v -> {
            int plIndex = getIndexInPlaylist(index);
            nextSong(plIndex);
        });

        // Prev button
        ImageButton prev = findViewById(R.id.psPrev);
        prev.setOnClickListener(v -> {
            int plIndex = getIndexInPlaylist(index);
            previousSong(plIndex);
        });

        // Set completion listener
        Globals.sp.setOnCompletionListener(mp -> {
            int plIndex = getIndexInPlaylist(index);
            switch (Globals.pref.getRepeatMode()) {
                case ONE:
                    playSong(songs[plIndex]);
                    return;
                case OFF:
                    nextSong(plIndex);
                    break;
            }
        });
    }

    private void nextSong(int currentIndexInPlaylist) {
        // Add the current song to the history
        history.add(currentIndexInPlaylist);

        // Play random song if shuffle is on
        if (Globals.pref.shouldShuffle()) {
            randomSong(currentIndexInPlaylist);
            return;
        }

        // Play next song otherwise
        int realIndex = (currentIndexInPlaylist >= songs.length - 1) ? 0 : currentIndexInPlaylist + 1;
        playSong(songs[realIndex]);
    }

    private void previousSong(int currentIndexInPlaylist) {
        // If shuffling, play song in history or random song if history is empty
        if (Globals.pref.shouldShuffle()) {
            if (history.size() > 0) {
                int lastIndex = history.remove(history.size() - 1);
                playSong(songs[lastIndex]);
                return;
            }
            randomSong(currentIndexInPlaylist);
            return;
        }

        // Play previous song otherwise
        int realIndex = (currentIndexInPlaylist <= 0) ? songs.length - 1 : currentIndexInPlaylist - 1;
        playSong(songs[realIndex]);
    }

    private void randomSong(int currentIndexInPlaylist) {
        // Get random song index
        int randomIndex = currentIndexInPlaylist;
        if (songs.length > 1) {
            while (randomIndex == currentIndexInPlaylist) {
                randomIndex = ThreadLocalRandom.current().nextInt(0, songs.length);
            }
        }

        // Play song at random index
        playSong(songs[randomIndex]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // End the song player
        Globals.sp.end();

        // Remove callbacks to the UpdateUI instance
        handler.removeCallbacks(updateUI);
    }

    public void onClick(View v) {
        finish();
    }

    private int getIndexInPlaylist(int index) {
        int plIndex = -1;
        for (int i = 0; i < songs.length; i++) {
            if (songs[i] == index) {
                plIndex = i;
                break;
            }
        }
        return plIndex;
    }

    class UpdateUI implements Runnable {
        @Override
        public void run() {
            long delayMillis = Globals.sp.getDuration() / 200;
            handler.postDelayed(new UpdateUI(), delayMillis);

            if (!Globals.sp.hasSong || !Globals.sp.isPlaying()) {
                return;
            }

            // Update seek bar
            SeekBar seekBar = findViewById(R.id.psSeekBar);
            seekBar.setProgress(Globals.sp.getProgress());

            // Update time
            TextView currentPosition = findViewById(R.id.psCurrentPosition);
            currentPosition.setText(Globals.sp.getCurrentPositionString());
            TextView timeLeft = findViewById(R.id.psTimeLeft);
            timeLeft.setText(Globals.sp.getTimeLeftString());
        }
    }
}
