package com.example.avec.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final Map<String, Drawable> drawableCache = new HashMap<>();

    private static Drawable fromURL(String url) {
        if (drawableCache.containsKey(url)) {
            return drawableCache.get(url);
        }
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, null);
            drawableCache.put(url, d);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void asyncFromURL(ImageView imageView, String url) {
        Globals.ex.submit(new ImageLoader.LoadImageFromURLTask(imageView, url));
    }

    static class LoadImageFromURLTask implements Runnable {
        ImageView thumbnail;
        String url;

        LoadImageFromURLTask(ImageView thumbnail, String url) {
            this.thumbnail = thumbnail;
            this.url = url;
        }

        @Override
        public void run() {
            thumbnail.setImageDrawable(ImageLoader.fromURL(url));
        }
    }
}
