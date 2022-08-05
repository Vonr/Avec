package dev.qther.avec.util;

import dev.qther.avec.util.playlist.Playlist;
import dev.qther.avec.util.song.SongPlayer;

import java.util.ArrayList;

// Globals contains static references to shared objects.
public class Globals {
    public static SongPlayer sp = new SongPlayer();
    public static Preferences pref;
    public static ArrayList<Playlist> playlists;
}
