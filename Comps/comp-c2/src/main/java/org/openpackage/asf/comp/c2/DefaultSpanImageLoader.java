package org.openpackage.asf.comp.c2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.style.ImageSpan;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.span.Span;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by micfans on 30/11/2016.
 */

public class DefaultSpanImageLoader implements SpanImageLoader {

    private static final Loger loger = Loger.getLoger(DefaultSpanImageLoader.class);

    /**
     *
     * @param context
     * @param drawable
     * @param cacheFile
     */
    protected void saveImage(Context context, Drawable drawable, File cacheFile){
        if(cacheFile.exists()){
            return;
        }
        loger.i("Saving cache file: " + cacheFile.getPath());
        cacheFile.getParentFile().mkdirs();
        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheFile);
            fos.write(bitmapData);
            fos.flush();
        } catch (Exception e) {
            loger.w("Can not save cache image: " + cacheFile.getPath(), e);
        }finally {
            if(fos != null){
                try {
                    fos.close();
                }catch (IOException ex){}
            }
        }
    }

    /**
     *
     * @param context
     * @param span
     */
    @Override
    public void loadImage(final Context context, final ImageLoadSpan span) {
        new AsyncTask<Object, Void, Drawable>() {

            @Override
            protected Drawable doInBackground(Object... params) {
                String u = span.loadUrl();
                Drawable drawable = null;
                try {
                    loger.i("Downloading image from " + u);
                    URL url = new URL(u);
                    drawable = Drawable.createFromStream(url.openStream(), "");
                    if(url.getProtocol().startsWith("http")){
                        saveImage(context, drawable, span.getCacheFile());
                    }
                } catch (Exception ex) {
                    loger.e("Can not get image from url " + ex);
                }
                return drawable;
            }

            @Override
            protected void onPostExecute(Drawable dx) {
                if (dx == null) {
                    dx = context.getResources().getDrawable(R.drawable.test);
                }
                dx.setBounds(0, 0, span.getImageBoundWidth(), span.getImageBoundHeight());
                ImageSpan imageSpan = new ImageSpan(dx, ImageSpan.ALIGN_BOTTOM);
                span.updateLoadCompleted(true);
                span.addAdditionSpan(imageSpan);
            }
        }.execute();
    }
}
