package com.example.avec.util.song;

public class Song {
    // Since no validation of songs are required, all fields are made public.
    // This makes them easier to access and reduces clutter in the code.
    public int index;
    public String name;
    public String artist;
    public String thumbnail;
    public String url;

    public Song(int index, String name, String artist, String thumbnail, String url) {
        this.index = index;
        this.name = name;
        this.artist = artist;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public String getThumbnailURL() {
        return "https://i.scdn.co/image/" + this.thumbnail;
    }

    public String getTrackURL() {
        return "https://p.scdn.co/mp3-preview/" + this.url;
    }
}
