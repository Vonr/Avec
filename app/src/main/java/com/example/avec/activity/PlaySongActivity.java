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

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class PlaySongActivity extends AppCompatActivity {
    UpdateUI updateSeekBar;
    Song song;
    int[] songs;
    ArrayList<Integer> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int index = getIntent().getIntExtra("index", -1);
        if (index == -1) {
            Log.e("PlaySongActivity", "No index provided");
            return;
        }

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        if (Globals.pref.isRightHanded()) {
            setContentView(R.layout.activity_play_song_right_handed);
        } else {
            setContentView(R.layout.activity_play_song);
        }

        songs = getIntent().getIntArrayExtra("songs");
        song = Globals.songRegistry.songs.get(index);

        playSong(index);

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
        ImageButton shuffle = findViewById(R.id.psShuffle);
        shuffle.setAlpha(Globals.pref.shouldShuffle() ? 1 : 0.5f);
        shuffle.setOnClickListener(v -> {
            boolean state = Globals.pref.shouldShuffle();
            Globals.pref.setShuffle(!state);
            shuffle.setAlpha(!state ? 1 : 0.5f);
        });
        ImageButton repeat = findViewById(R.id.psRepeat);
        repeat.setAlpha(Globals.pref.getRepeatMode() == Preferences.RepeatMode.ONE ? 1 : 0.5f);
        repeat.setOnClickListener(v -> {
            Preferences.RepeatMode state = Globals.pref.getRepeatMode();
            // Set the new state to the next RepeatMode.
            Preferences.RepeatMode newMode = Globals.pref.setRepeatMode(
                    Preferences.RepeatMode.values()[
                            (state.ordinal() + 1) % Preferences.RepeatMode.values().length]);
            // Set the image to the new state.
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

        updateSeekBar = new UpdateUI();
        new Handler(Looper.getMainLooper()).post(updateSeekBar);
    }

    private void playSong(int index) {
        ImageButton playPause = findViewById(R.id.psPlayPause);
        playPause.setImageResource(R.drawable.pause);

        song = Globals.songRegistry.songs.get(index);
        TextView name = findViewById(R.id.psName);
        name.setText(song.name);
        TextView artist = findViewById(R.id.psArtist);
        artist.setText(song.artist);
        ImageView thumbnail = findViewById(R.id.psThumb);
        asyncFromURL(thumbnail, song.getThumbnailURL());
        Globals.sp.play(song);

        ImageButton next = findViewById(R.id.psNext);
        next.setOnClickListener(v -> {
            int plIndex = getIndexInPlaylist(index);
            nextSong(plIndex);
        });
        ImageButton prev = findViewById(R.id.psPrev);
        prev.setOnClickListener(v -> {
            int plIndex = getIndexInPlaylist(index);
            previousSong(plIndex);
        });
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
        history.add(currentIndexInPlaylist);
        if (Globals.pref.shouldShuffle()) {
            randomSong(currentIndexInPlaylist);
            return;
        }

        int realIndex = (currentIndexInPlaylist >= songs.length - 1) ? 0 : currentIndexInPlaylist + 1;
        playSong(songs[realIndex]);
    }

    private void previousSong(int currentIndexInPlaylist) {
        if (Globals.pref.shouldShuffle()) {
            if (history.size() > 0) {
                int lastIndex = history.remove(history.size() - 1);
                playSong(songs[lastIndex]);
                return;
            }
            randomSong(currentIndexInPlaylist);
            return;
        }

        int realIndex = (currentIndexInPlaylist <= 0) ? songs.length - 1 : currentIndexInPlaylist - 1;
        playSong(songs[realIndex]);
    }

    private void randomSong(int currentIndexInPlaylist) {
        int randomIndex = currentIndexInPlaylist;
        if (songs.length > 1) {
            while (randomIndex == currentIndexInPlaylist) {
                randomIndex = ThreadLocalRandom.current().nextInt(0, songs.length);
            }
        }
        playSong(songs[randomIndex]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Globals.sp.end();

        if (updateSeekBar != null) {
            updateSeekBar.stopped = false;
        }
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
        public boolean stopped;

        @Override
        public void run() {
            long delayMillis = Globals.sp.getDuration() / 200;
            new Handler(Looper.getMainLooper()).postDelayed(new UpdateUI(), delayMillis);

            if (!Globals.sp.hasSong || !Globals.sp.isPlaying()) {
                return;
            }
            SeekBar seekBar = findViewById(R.id.psSeekBar);
            seekBar.setProgress(Globals.sp.getProgress());
            TextView currentPosition = findViewById(R.id.psCurrentPosition);
            currentPosition.setText(Globals.sp.getCurrentPositionString());
            TextView timeLeft = findViewById(R.id.psTimeLeft);
            timeLeft.setText(Globals.sp.getTimeLeftString());
        }
    }
}
