package org.openpackage.asf.comp.c2.span.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.DataSpanSupportable;
import org.openpackage.asf.comp.c2.DataSpannableString;
import org.openpackage.asf.comp.c2.DefaultSpanImageLoader;
import org.openpackage.asf.comp.c2.ImageLoadSpan;
import org.openpackage.asf.comp.c2.SpanImageLoader;
import org.openpackage.asf.comp.c2.span.DataSpan;
import org.openpackage.asf.comp.c2.span.RenderSpan;
import org.openpackage.asf.comp.c2.span.SpanObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by micfans on 30/11/2016.
 */

public class EmojiSpan extends DataSpan implements RenderSpan, ImageLoadSpan {

    private static final Loger loger = Loger.getLoger(EmojiSpan.class);

    /**
     *
     */
    public static final String SPAN_TYPE = "emoji";
    /**
     * 图片加载器
     */
    private static SpanImageLoader imageLoader = new DefaultSpanImageLoader();
    /**
     * 是否加载完毕
     */
    private boolean isLoaded = false;

    /**
     * 图片根地址
     */
    private String urlRoot = "http://www.haotu.net/icon/203428/little-boy-pink/";

    /**
     *
     * @param context
     * @param data
     */
    public EmojiSpan(Context context, SpanObject data) {
        super(context, data);
    }

    /**
     *
     * @param context
     * @param data
     * @param clickable
     */
    public EmojiSpan(Context context, SpanObject data, boolean clickable) {
        super(context, data, clickable);
    }

    /**
     *
     * @return
     */
    @Override
    public SpanImageLoader getSpanImageLoader(){
        return imageLoader;
    }

    /**
     *
     * @return
     */
    @Override
    public int getImageBoundWidth() {
        return 70;
    }

    /**
     *
     * @return
     */
    @Override
    public int getImageBoundHeight() {
        return 70;
    }

    /**
     *
     * @return
     */
    @Override
    public String loadUrl() {
        File file = getCacheFile();
        if (file.exists()) {
            return "file://" + file.getAbsolutePath();
        }
        return urlRoot + data.getText() + "/png";
    }

    /**
     *
     * @return
     */
    public File getCacheFile() {
        return new File(new File(context.getCacheDir(), SPAN_TYPE), getSpanText());
    }

    /**
     *
     * @return
     */
    @Override
    public String getSpanType() {
        return SPAN_TYPE;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isWhole() {
        return true;
    }

    /**
     *
     * @param dataSpannableString
     * @param textWeakReference
     */
    @Override
    public void apply(DataSpannableString dataSpannableString, WeakReference<DataSpanSupportable> textWeakReference) {
        super.apply(dataSpannableString, textWeakReference);
        if (!isLoaded) {
            //createImageSpan(dataSpannableString);
            imageLoader.loadImage(context, this);
        }
    }

    /**
     *
     * @param dataSpannableString
     */
    @Deprecated
    protected void createImageSpan(final DataSpannableString dataSpannableString) {
        new Thread() {
            public void run() {
                Spanned hotSpan = Html.fromHtml("<img src='" + loadUrl() + "'/> ", new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        Drawable drawable = null;
                        try {
                            URL url = new URL(source);
                            loger.i("Downloading image from " + source);
                            drawable = Drawable.createFromStream(url.openStream(), "");
                        } catch (Exception ex) {
                            loger.e("Can not get image from url " + ex.getMessage(), ex);
                        }
                        return drawable;
                    }
                }, null);

                EmojiSpan.this.applySpan(dataSpannableString, hotSpan);
            }
        }.start();
    }

    /**
     *
     * @param completed
     */
    @Override
    public void updateLoadCompleted(boolean completed) {
        isLoaded = completed;
    }
}
