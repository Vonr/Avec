package com.example.avec.util;

import com.example.avec.util.playlist.Playlist;
import com.example.avec.util.song.SongPlayer;
import com.example.avec.util.song.SongRegistry;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Globals {
    public static SongRegistry songRegistry = new SongRegistry();
    public static SongPlayer sp = new SongPlayer();
    public static Preferences pref;
    public static ArrayList<Playlist> playlists;

    public static ExecutorService ex = new ForkJoinPool(4);
}
