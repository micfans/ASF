package org.openpackage.base.core.http;

import android.content.Context;

import org.openpackage.base.core.http.async.AsyncHttpRequester;

/**
 * Created by micfans on 23/11/2016.
 */

public class HttpRequesterFactory {

    /**
     * Use AsyncHttpClient
     */
    public static final String TYPE_ASYNC_HTTP_CLIENT = "TYPE_ASYNC_HTTP_CLIENT";

    /**
     *
     * @param type
     * @return
     */
    public static IHttpRequester getHttpClient(Context context, String type){
        switch (type){
            case TYPE_ASYNC_HTTP_CLIENT:
                return createAsyncRequestor(context);
            default:
                throw new IllegalArgumentException("Unknown requester type for " + type );
        }
    }

    /**
     *
     * @return
     */
    private static AsyncHttpRequester createAsyncRequestor(Context context){
        return new AsyncHttpRequester(context);
    }
}
