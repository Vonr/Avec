package dev.qther.avec.util.song;

import java.util.ArrayList;

public class SongRegistry {
    public static final ArrayList<Song> songs = new ArrayList<>();
    private static int lastIndex;

    public static void song(String name, String artist, String url, String thumbnail) {
        songs.add(new Song(lastIndex++, name, artist, thumbnail, url));
    }

    static {
        song("Shape of You",
                "Ed Sheeran",
                "09e92af512355474ccf074988ea208ce6eb90a2b",
                "ab67616d0000b273ba5db46f4b838ef6027e6f96");
        song("Perfect",
                "Ed Sheeran",
                "229419b7fe43f4aa963e8ed8eecabc4b87c4958e",
                "ab67616d0000b273ba5db46f4b838ef6027e6f96");
        song("Castle on the Hill",
                "Ed Sheeran",
                "6c1cbae0f9942c8b42d5d7c4b9d1ab8154b1500b",
                "ab67616d0000b273ba5db46f4b838ef6027e6f96");
        song("The A Team",
                "Ed Sheeran",
                "cbaa51bb2bf76548446f23e968933cc352ad4cad",
                "ab67616d0000b273d4e0fdd4c41a4f9bfd884301");
        song("Photograph",
                "Ed Sheeran",
                "34704823c55ae09f26988b106784f884bb781068",
                "ab67616d0000b27313b3e37318a0c247b550bccd");
        song("Separate Ways",
                "Journey",
                "856c42947e29c4cc51a03d2d27afc19c6c4119d1",
                "ab67616d0000b273e8b5ac6f64e16164f96865f8");
        song("Water Fountain",
                "Alec Benjamin",
                "8faedbf41379ef2197699049a0b30328228ea7cb",
                "ab67616d0000b273459d675aa0b6f3b211357370");
        song("Loafers",
                "BoyWithUke",
                "8ea248456fcd1314bb128cbb09a3f4aa5d1ff81a",
                "ab67616d0000b2734d1226cc7373b9c09cde1bc3");
        song("Two Moons",
                "BoyWithUke",
                "8bc0d81ba093e77ea37917d8336dd18fae3d9e08",
                "ab67616d0000b2733b57e2257933b78b7292de23");
        song("Haha, Hi",
                "BoyWithUke",
                "520be77edff8731767a951f5f260705e221dfa7d",
                "ab67616d0000b2733b57e2257933b78b7292de23");
        song("Death Bed",
                "Powfu",
                "eb52a920dd6fafb07ae1f9e5e60f9279ee854b89",
                "ab67616d0000b273bf01fd0986a195d485922167");
        song("the fire in your eyes keeps me warm",
                "Powfu",
                "5c5e46af94b0af920f56aa2649262cd3b7a89425",
                "ab67616d0000b273bf01fd0986a195d485922167");
        song("Glimpse Of Us",
                "Joji",
                "071c22f355ed0d03fdc176dcb25a487f5ffb661c",
                "ab67616d0000b273f798d46201c266747be5db2e");
        song("Losing Interest",
                "Shiloh Dynasty",
                "e78e374a2a4d77e743a8a12a3811ae0427e6ecc3",
                "ab67616d0000b273b0cdc9a7183f4c0eea03b69e");
        song("Luna",
                "shinigami",
                "f884ced90fc37b9703eb1529168220f3a205ecde",
                "ab67616d0000b273463511d43e2f6379481fcae0");
        song("facade",
                "atlas",
                "6d79f7df71646ac8503d269e73a23126c867290c",
                "ab67616d0000b273c49779ac33322e62be90d6ec");
        song("Notion - Acoustic",
                "The Rare Occasions",
                "c165b8c1cf199e8f9002db3e542c78a4a4df1f53",
                "ab67616d0000b2739604938dd7e72bf220e59858");

    }
}
