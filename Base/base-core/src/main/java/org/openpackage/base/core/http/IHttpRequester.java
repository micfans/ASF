package org.openpackage.base.core.http;

import java.util.Map;

/**
 * Created by micfans on 23/11/2016.
 */

public interface IHttpRequester {

    public static final String GET = "GET";
    public static final String POST = "POST";

    void setCookieEnabled(boolean cookieEnabled);

    IRequestHandler req(String method, String url, Map params, InnerResponseHandler innerResponseHandler);

    IRequestHandler post(String url, Map params, InnerResponseHandler innerResponseHandler);

    IRequestHandler get(String url, Map params, InnerResponseHandler innerResponseHandler);
}
