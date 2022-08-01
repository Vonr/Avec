package com.example.avec.util.song;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SongSearcher {
    public static List<Song> search(String query, List<Song> songs) {
        double queryLength = query.length();
        String lowerQuery = query.toLowerCase();
        return songs.stream()
                .filter(s -> s.name.toLowerCase().contains(lowerQuery))
                .sorted((s1, s2) -> {
                    String name1 = s1.name.toLowerCase();
                    String name2 = s2.name.toLowerCase();

                    Pattern wordPat = Pattern.compile("\\b" + lowerQuery + "\\b");
                    boolean isWord1 = wordPat.matcher(name1).find();
                    boolean isWord2 = wordPat.matcher(name2).find();
                    if (isWord1) {
                        return -1;
                    } else if (isWord2) {
                        return 1;
                    }

                    Pattern beginPat = Pattern.compile("\\b" + lowerQuery);
                    boolean isBeginning1 = beginPat.matcher(name1).find();
                    boolean isBeginning2 = beginPat.matcher(name2).find();
                    if (isBeginning1) {
                        return -1;
                    } else if (isBeginning2) {
                        return 1;
                    }
                    return (int) (queryLength / s1.name.length() - queryLength / s2.name.length());
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
