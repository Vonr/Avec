package com.example.avec.util.song;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class SongRegistry {
    public ArrayList<Song> songs;
    private int lastIndex;

    public SongRegistry() {
        songs = new ArrayList<>();
        populate();
    }

    private final Song SHAPE_OF_YOU = song(
            "Shape of You",
            "Ed Sheeran",
            "ab67616d0000b273ba5db46f4b838ef6027e6f96",
            "09e92af512355474ccf074988ea208ce6eb90a2b");
    private final Song PERFECT = song(
            "Perfect",
            "Ed Sheeran",
            "ab67616d0000b273ba5db46f4b838ef6027e6f96",
            "229419b7fe43f4aa963e8ed8eecabc4b87c4958e");
    private final Song CASTLE_ON_THE_HILL = song(
            "Castle on the Hill",
            "Ed Sheeran",
            "ab67616d0000b273ba5db46f4b838ef6027e6f96",
            "6c1cbae0f9942c8b42d5d7c4b9d1ab8154b1500b");
    private final Song THE_A_TEAM = song(
            "The A Team",
            "Ed Sheeran",
            "ab67616d0000b273d4e0fdd4c41a4f9bfd884301",
            "cbaa51bb2bf76548446f23e968933cc352ad4cad");
    private final Song PHOTOGRAPH = song(
            "Photograph",
            "Ed Sheeran",
            "ab67616d0000b27313b3e37318a0c247b550bccd",
            "34704823c55ae09f26988b106784f884bb781068");
    private final Song SEPARATE_WAYS = song(
            "Separate Ways",
            "Journey",
            "ab67616d0000b273e8b5ac6f64e16164f96865f8",
            "856c42947e29c4cc51a03d2d27afc19c6c4119d1");
    private final Song LOAFERS = song(
            "Loafers",
            "BoyWithUke",
            "ab67616d0000b2734d1226cc7373b9c09cde1bc3",
            "8ea248456fcd1314bb128cbb09a3f4aa5d1ff81a");
    private final Song WATER_FOUNTAIN = song(
            "Water Fountain",
            "Alec Benjamin",
            "ab67616d0000b273459d675aa0b6f3b211357370",
            "8faedbf41379ef2197699049a0b30328228ea7cb");

    public void populate() {
        register(SHAPE_OF_YOU,
                PERFECT,
                CASTLE_ON_THE_HILL,
                THE_A_TEAM,
                PHOTOGRAPH,
                SEPARATE_WAYS,
                LOAFERS,
                WATER_FOUNTAIN);
    }

    public Song song(String name, String artist, String thumbnail, String url) {
        return new Song(lastIndex++, name, artist, thumbnail, url);
    }

    public void register(Song... song) {
        songs.addAll(Arrays.asList(song));
    }
}
