package dev.qther.avec.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class ImageLoader {
    // Cache of images to prevent unnecessary network requests
    private static final Map<String, Drawable> drawableCache = new HashMap<>();
    public static ExecutorService ex = new ForkJoinPool(4);

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

    // Loads an image from a URL and sets it to an ImageView on another thread.
    public static void asyncFromURL(ImageView view, String url) {
        ex.submit(new ImageLoader.LoadImageFromURLTask(view, url));
    }

    static class LoadImageFromURLTask implements Runnable {
        ImageView view;
        String url;

        LoadImageFromURLTask(ImageView view, String url) {
            this.view = view;
            this.url = url;
        }

        @Override
        public void run() {
            view.setImageDrawable(ImageLoader.fromURL(url));
        }
    }
}
