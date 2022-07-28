package com.example.avec.util.playlist;

import android.util.Log;
import com.example.avec.util.Globals;
import com.example.avec.util.song.Song;

import java.util.ArrayList;

public class Playlist {
    public String name;
    public boolean pinned;
    private final ArrayList<Integer> songs = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String name, int[] songs) {
        this.name = name;
        for (int index : songs) {
            this.songs.add(index);
        }
    }

    public boolean add(int index) {
        if (songs.contains(index)) {
            return false;
        }
        boolean success = songs.add(index);
        if (success) {
            Globals.pref.savePlaylists(Globals.playlists);
        }
        return success;
    }

    public boolean remove(Song song) {
        boolean success = Globals.songRegistry.songs.remove(song);
        if (success) {
            Globals.pref.savePlaylists(Globals.playlists);
        }
        return success;
    }

    public boolean remove(int song) {
        boolean success = false;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i) == song) {
                songs.remove(i);
                success = true;
                break;
            }
        }
        if (success) {
            Globals.pref.savePlaylists(Globals.playlists);
        }
        return success;
    }

    public void removeByIndex(int index) {
        Globals.songRegistry.songs.remove(index);
    }

    public Song get(int index) {
        return Globals.songRegistry.songs.get(songs.get(index));
    }

    public boolean contains(int index) {
        return songs.contains(index);
    }

    public int size() {
        return songs.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(' ');
        sb.append(pinned);
        sb.append(' ');
        for (int song : songs) {
            sb.append(song);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Playlist fromString(String str) {
        Log.d("Playlist", "Parsing playlist: " + str);
        int separator = str.lastIndexOf(' ');
        String nameAndPinned = str.substring(0, separator);
        Playlist playlist;
        int nameSep = nameAndPinned.lastIndexOf(' ');
        if (nameSep == -1) {
            playlist = new Playlist(nameAndPinned);
            nameSep = separator;
            playlist.pinned = str.substring(nameSep + 1).equals("true");
        } else {
            playlist = new Playlist(nameAndPinned.substring(0, nameSep));
            playlist.pinned = nameAndPinned.substring(nameSep + 1).equals("true");
        }
        Log.d("Playlist", "Name: " + playlist.name);

        Log.d("Playlist", "nameSep: " + nameSep);
        String[] indices = str.substring(separator + 1).split(",");
        for (String index : indices) {
            try {
                int song = Integer.parseInt(index);
                if (Globals.songRegistry.songs.size() > song) {
                    playlist.songs.add(song);
                }
            } catch (NumberFormatException ignored) {}
        }
        return playlist;
    }

    public int[] getSongs() {
        int[] songs = new int[this.songs.size()];
        for (int i = 0; i < this.songs.size(); i++) {
            songs[i] = this.songs.get(i);
        }
        return songs;
    }
}
