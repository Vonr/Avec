package dev.qther.avec.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import dev.qther.avec.util.playlist.Playlist;

import java.util.ArrayList;

public class Preferences {
    SharedPreferences store;
    private boolean rightHanded;
    private boolean rightHandedInit;
    private boolean shuffle;
    private boolean shuffleInit;
    private RepeatMode repeat;
    private boolean repeatInit;

    public Preferences(Context ctx) {
        this.store = ctx.getSharedPreferences("avec_store", Context.MODE_PRIVATE);
    }

    public boolean isRightHanded() {
        if (!rightHandedInit) {
            this.rightHanded = store.getBoolean("right_handed", false);
            this.rightHandedInit = true;
        }
        return this.rightHanded;
    }

    public void setRightHanded(boolean rightHanded) {
        this.rightHanded = rightHanded;
        store.edit().putBoolean("right_handed", this.rightHanded).apply();
    }

    public boolean shouldShuffle() {
        if (!shuffleInit) {
            this.shuffle = store.getBoolean("shuffle", false);
            this.shuffleInit = true;
        }
        return this.shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        store.edit().putBoolean("shuffle", this.shuffle).apply();
    }

    public RepeatMode getRepeatMode() {
        if (!repeatInit) {
            this.repeat = RepeatMode.values()[store.getInt("repeat_mode", 0)];
            this.repeatInit = true;
        }
        return this.repeat;
    }

    public RepeatMode setRepeatMode(RepeatMode repeat) {
        this.repeat = repeat;
        this.repeatInit = true;
        store.edit().putInt("repeat_mode", this.repeat.ordinal()).apply();
        return this.repeat;
    }

    // Saves playlists by getting their string representation and storing
    // them in stored preferences in the format:
    //
    // name1 pinned1 song1,song2,song3...
    // name2 pinned2 song2,song5,song3...
    // ...
    public void savePlaylists(ArrayList<Playlist> playlists) {
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist : playlists) {
            sb.append(playlist.toString());
            sb.append('\n');
        }
        String str = sb.toString();
        Log.d("Preferences", "Saving playlists: " + str);
        store.edit().putString("playlists", str).apply();
//        store.edit().putString("playlists", "").apply();
    }

    // Overload of savePlaylists to save Globals.playlists by default
    public void savePlaylists() {
        savePlaylists(Globals.playlists);
    }


    // Reads playlists from stored preferences in the format:
    //
    // name1 pinned1 song1,song2,song3...
    // name2 pinned2 song2,song5,song3...
    // ...
    //
    // into an ArrayList of Playlist objects.
    public ArrayList<Playlist> loadPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        String str = store.getString("playlists", "");
        if (str.isEmpty()) {
            playlists.add(new Playlist("Your Favourites"));
            return playlists;
        }
        String[] lines = str.split("\n");
        for (String line : lines) {
            if (line.startsWith(",") || line.startsWith(" ")) continue;
            playlists.add(Playlist.fromString(line));
        }
        if (playlists.stream().map(p -> p.name).noneMatch(name -> name.equals("Your Favourites"))) {
            Playlist favourites = new Playlist("Your Favourites", true);
            playlists.add(0, favourites);
        }
        return playlists;
    }

    public enum RepeatMode {
        OFF,
        ONE,
    }
}
