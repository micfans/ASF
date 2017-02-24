package org.openpackage.asf.comp.c2;

import org.openpackage.asf.comp.c2.span.Span;

import java.io.File;

/**
 * Created by micfans on 30/11/2016.
 */

public interface LoadSpan extends Span {

    void updateLoadCompleted(boolean completed);

    String loadUrl();

    File getCacheFile();
}
