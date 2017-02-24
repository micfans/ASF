package org.openpackage.base.core.http.async;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.base.core.http.IResponseBean;
import org.openpackage.base.core.http.InnerResponseHandler;
import org.openpackage.base.core.http.ResponseHelper;

import cz.msebera.android.httpclient.Header;

/**
 * Created by micfans on 23/11/2016.
 */

public class AsyncResponseHandler extends TextHttpResponseHandler {


    private static final Loger loger = Loger.getLoger(AsyncResponseHandler.class);


    private InnerResponseHandler innerResponseHandler;
    private String url;
    private RequestParams params;

    /**
     * @param innerResponseHandler
     */
    public AsyncResponseHandler(InnerResponseHandler innerResponseHandler) {
        this.innerResponseHandler = innerResponseHandler;
    }

    /**
     * @param innerResponseHandler
     * @param url
     * @param params
     */
    public AsyncResponseHandler(InnerResponseHandler innerResponseHandler, String url, RequestParams params) {
        this.innerResponseHandler = innerResponseHandler;
        this.url = url;
        this.params = params;
    }

    /**
     * @param statusCode
     * @param headers
     * @param responseString
     * @param throwable
     */
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        loger.w("Request " + url + " failed, statusCode is " + statusCode + ", responseString: " + responseString);
        if (innerResponseHandler != null) {
            try {
                innerResponseHandler.onFailure(String.valueOf(statusCode));
            } catch (Exception ex) {
                loger.e("Execute handler onFailure method on '" + innerResponseHandler.getClass() + "' error: " + ex.getMessage(), ex);
            }

        }
    }

    /**
     * @param statusCode
     * @param headers
     * @param responseString
     */
    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        loger.i("Response from " + url + ", status: " + statusCode + ", response content:" + responseString);
        IResponseBean bean = ResponseHelper.parseResponse(responseString, innerResponseHandler);
        if (innerResponseHandler != null) {
            try {
                innerResponseHandler.onResponse(bean);
            } catch (Exception ex) {
                loger.e("Execute handler onSuccess method on '" + innerResponseHandler.getClass() + "' error: " + ex.getMessage(), ex);
            }
        }
    }


    /**
     *
     */
    @Override
    public void onFinish() {
        super.onFinish();
        if (innerResponseHandler != null) {
            try {
                innerResponseHandler.onFinish();
            } catch (Exception ex) {
                loger.e("Execute handler onFinish method on '" + innerResponseHandler.getClass() + "' error: " + ex.getMessage(), ex);
            }
        }
    }
}
