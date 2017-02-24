package org.openpackage.base.core.http.async;

import com.loopj.android.http.RequestHandle;

import org.openpackage.base.core.http.IRequestHandler;

/**
 * Created by micfans on 23/11/2016.
 */

public class AsyncRequestHandler implements IRequestHandler {

    private RequestHandle requestHandle;

    AsyncRequestHandler(RequestHandle requestHandle){
        this.requestHandle = requestHandle;
    }

    @Override
    public boolean cancel() {
        return requestHandle.cancel(true);
    }

    @Override
    public boolean isCanceled() {
        return requestHandle.isCancelled();
    }

    @Override
    public boolean isFinished() {
        return requestHandle.isFinished();
    }
}
