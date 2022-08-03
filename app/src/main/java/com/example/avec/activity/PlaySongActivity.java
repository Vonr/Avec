package com.example.avec.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.avec.R;
import com.example.avec.dialog.AddToPlaylistDialog;
import com.example.avec.dialog.LyricsDialog;
import com.example.avec.util.Globals;
import com.example.avec.util.Preferences;
import com.example.avec.util.song.Song;
import com.example.avec.util.song.SongPlayer;
import com.example.avec.util.song.SongRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.avec.util.ImageLoader.asyncFromURL;

public class PlaySongActivity extends AppCompatActivity {
    UpdateUI updateUI;
    Song song;
    int[] songs;
    ArrayList<Integer> history = new ArrayList<>();
    Handler handler = new Handler(Looper.getMainLooper());
    ExecutorService ex = new ForkJoinPool(1);
    LyricsDialog dialog;

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

        dialog = new LyricsDialog(this);
        dialog.setLyrics("");
        Button lyricsButton = findViewById(R.id.lyrics);
        lyricsButton.setOnClickListener(v -> dialog.show());

        songs = getIntent().getIntArrayExtra("songs");
        song = SongRegistry.songs.get(index);

        playSong(index);

        // SeekBar drag behaviour
        SeekBar seekBar = findViewById(R.id.seek_bar);
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
        ImageButton playPause = findViewById(R.id.play_pause);
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
        ImageButton shuffle = findViewById(R.id.shuffle);
        shuffle.setAlpha(Globals.pref.shouldShuffle() ? 1 : 0.67f);
        shuffle.setOnClickListener(v -> {
            boolean state = Globals.pref.shouldShuffle();
            Globals.pref.setShuffle(!state);
            shuffle.setAlpha(!state ? 1 : 0.67f);
        });

        // Repeat button behaviour
        ImageButton repeat = findViewById(R.id.repeat);
        repeat.setAlpha(Globals.pref.getRepeatMode() == Preferences.RepeatMode.ONE ? 1 : 0.67f);
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
                    repeat.setAlpha(0.67f);
                    break;
                case ONE:
                    repeat.setImageResource(R.drawable.repeat_one);
                    repeat.setAlpha(1f);
                    break;
            }
        });

        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            AddToPlaylistDialog dialog = new AddToPlaylistDialog(v.getContext(), song.index);
            dialog.show();
        });

        updateUI = new UpdateUI();
        handler.post(updateUI);
    }

    private void playSong(int index) {
        // Get image button
        ImageButton playPause = findViewById(R.id.play_pause);
        playPause.setImageResource(R.drawable.pause);

        // Get song from index
        song = SongRegistry.songs.get(index);
        TextView name = findViewById(R.id.name);

        // Set title to song name
        name.setText(song.name);
        // Set artist to song name
        TextView artist = findViewById(R.id.artist);
        artist.setText(song.artist);

        // Set album art to song album art
        ImageView thumbnail = findViewById(R.id.thumbnail);
        asyncFromURL(thumbnail, song.getThumbnailURL());

        // Play song
        Globals.sp.play(song);

        // Next button
        ImageButton next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            int plIndex = getIndexInPlaylist(index);
            nextSong(plIndex);
        });

        // Prev button
        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(v -> {
            int plIndex = getIndexInPlaylist(index);
            previousSong(plIndex);
        });

        dialog.setLyrics("");
        ex.submit(() -> {
            try {
                String lyrics;
                String url = "https://www.musixmatch.com/lyrics/"
                        + song.artist.replaceAll(" ", "-")
                        + "/" + song.name.replaceAll(" ", "-");
                Document doc = Jsoup.connect(url).get();
                doc.outputSettings(new Document.OutputSettings().prettyPrint(false));

                Elements e = doc.select(".mxm-lyrics__content");
                StringBuilder lyricsBuilder = new StringBuilder();
                for (Element element : e) {
                    lyricsBuilder.append(element.wholeText()).append('\n');
                }

                lyrics = lyricsBuilder.toString();
                dialog.setLyrics(lyrics);
            } catch (IOException | NullPointerException e) {
                dialog.setLyrics("");
            }
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
            SeekBar seekBar = findViewById(R.id.seek_bar);
            seekBar.setProgress(Globals.sp.getProgress());

            // Update time
            TextView currentPosition = findViewById(R.id.psCurrentPosition);
            currentPosition.setText(Globals.sp.getCurrentPositionString());
            TextView timeLeft = findViewById(R.id.psTimeLeft);
            timeLeft.setText(Globals.sp.getTimeLeftString());
        }
    }
}
