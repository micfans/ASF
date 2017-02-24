package org.openpackage.asf.base.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by micfans on 22/11/2016.
 */

public class ViewIdGener {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     *
     * @return
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
