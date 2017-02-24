package org.openpackage.base.core.http;

/**
 * Created by micfans on 23/11/2016.
 */

public interface IResponseHandler {

    public static final int TYPE_JSON = 0;
    public static final int TYPE_XML = 1;
    public static final int TYPE_TEXT = 2;

    void onFailure(String statusCode);

    void onResponse(IResponseBean responseBean);

    void onFinish();

}
