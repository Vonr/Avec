package dev.qther.avec.util.playlist;

import android.util.Log;
import dev.qther.avec.util.Globals;
import dev.qther.avec.util.song.Song;
import dev.qther.avec.util.song.SongRegistry;

import java.util.ArrayList;

public class Playlist {
    public String name;
    public boolean pinned;
    private final ArrayList<Integer> songs = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String name, boolean pinned) {
        this.name = name;
        this.pinned = pinned;
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
        boolean success = SongRegistry.songs.remove(song);
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
        SongRegistry.songs.remove(index);
    }

    public Song get(int index) {
        return SongRegistry.songs.get(songs.get(index));
    }

    public boolean contains(int index) {
        return songs.contains(index);
    }

    public int size() {
        return songs.size();
    }

    // Returns a string representation of the playlist in the format
    // "name pinned song1,song2,song3..."
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(' ');
        sb.append(pinned);
        sb.append(' ');
        if (songs.isEmpty()) {
            return sb.toString();
        }
        for (int song : songs) {
            sb.append(song);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    // Converts from a string in the following format
    // "name pinned song1,song2,song3..."
    // to a Playlist object.
    public static Playlist fromString(String str) {
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

        String[] indices = str.substring(separator + 1).split(",");
        for (String index : indices) {
            try {
                int song = Integer.parseInt(index);
                if (SongRegistry.songs.size() > song) {
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
