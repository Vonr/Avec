package com.example.avec.util.song;

import android.media.AudioAttributes;
import android.media.MediaPlayer;

import java.io.IOException;

// This class is a wrapper around MediaPlayer for convenience
public class SongPlayer {
    private final MediaPlayer mediaPlayer;
    public boolean hasSong = false;

    public SongPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
    }

    public void play(Song song) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getTrackURL());
            hasSong = true;
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void unpause() {
        if (!isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void end() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        hasSong = false;
    }

    public void seek(int seconds) {
        if (!mediaPlayer.isPlaying()) {
            end();
            unpause();
        }
        mediaPlayer.seekTo(seconds);
    }

    synchronized public int getDuration() {
        if (!hasSong) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    synchronized public int getProgress() {
        return (int) Math.ceil(mediaPlayer.getCurrentPosition() * 100D / getDuration());
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mediaPlayer.setOnCompletionListener(listener);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public String getCurrentPositionString() {
        return secondsToString((int) Math.round(getCurrentPosition() / 1000D));
    }

    public int getTimeLeft() {
        return getDuration() - getCurrentPosition();
    }

    public String getTimeLeftString() {
        return secondsToString((int) Math.round(getTimeLeft() / 1000D));
    }

    public String secondsToString(int seconds) {
        int minutes = seconds / 60;
        int secondsLeft = seconds % 60;
        return minutes + ":" + ((secondsLeft <= 9) ? "0" : "") + secondsLeft;
    }
}
