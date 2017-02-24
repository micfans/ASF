package org.openpackage.base.core.http.async;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.base.core.http.AbstractHttpRequester;
import org.openpackage.base.core.http.IHttpRequester;
import org.openpackage.base.core.http.InnerResponseHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.client.BasicCookieStore;

/**
 * Created by micfans on 23/11/2016.
 */

public class AsyncHttpRequester extends AbstractHttpRequester {


    private static final Loger loger = Loger.getLoger(AsyncHttpRequester.class);

    private AsyncHttpClient client = new AsyncHttpClient();


    private Context context;
    private boolean attachCookie = false;

    public AsyncHttpRequester(Context context) {
        this.context = context;
    }


    private RequestParams convertParams(Map params) {
        RequestParams rp = new RequestParams();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            rp.put(key, params.get(key));
        }
        return rp;
    }


    @Override
    public void setCookieEnabled(boolean cookieEnabled) {
        attachCookie = cookieEnabled;
    }

    /**
     * @param url
     * @param params
     * @param innerResponseHandler
     * @return
     */
    public AsyncRequestHandler post(String url, Map params, InnerResponseHandler innerResponseHandler) {
        return this.req(IHttpRequester.POST, url, params, innerResponseHandler);
    }

    /**
     * @param url
     * @param params
     * @param innerResponseHandler
     * @return
     */
    @Override
    public AsyncRequestHandler get(String url, Map params, InnerResponseHandler innerResponseHandler) {
        return this.req(IHttpRequester.GET, url, params, innerResponseHandler);
    }

    /**
     * @param url
     * @param params
     * @param innerResponseHandler
     * @return
     */
    @Override
    public AsyncRequestHandler req(String method, String url, Map params, InnerResponseHandler innerResponseHandler) {
        RequestHandle rh = this.req(method, url, convertParams(params), innerResponseHandler);
        return new AsyncRequestHandler(rh);
    }

    /**
     * @param url
     * @param params
     * @param innerResponseHandler
     * @return
     */
    public RequestHandle req(String method, String url, RequestParams params, InnerResponseHandler innerResponseHandler) {
        loger.i("Req url: " + url + ", params are: " + params + ", handler bean is: " + innerResponseHandler.getBeanClass());
        putCookies();
        RequestHandle rh;
        if (method.equals(IHttpRequester.GET))
            rh = client.get(context, url, params, new AsyncResponseHandler(innerResponseHandler, url, params));
        else {
            rh = client.post(context, url, params, new AsyncResponseHandler(innerResponseHandler, url, params));
        }
        saveCookies();
        return rh;
    }

    /**
     */
    private void putCookies() {
        if (attachCookie) {
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            BasicCookieStore bcs = new BasicCookieStore();
            bcs.addCookies(myCookieStore.getCookies().toArray(new Cookie[]{}));
            client.setCookieStore(bcs);
            loger.i("Print put cookies begin.");
            for (Cookie c : bcs.getCookies()) {
                loger.i("Cookie domain=" + c.getDomain() + ", path=" + c.getPath() + ", name=" + c.getName() + ", value=" + c.getValue());
            }
            loger.i("Print put cookies end.");
        }
    }

    /**
     */
    private void saveCookies() {
        if (attachCookie) {
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(myCookieStore);
            List<Cookie> cookies = myCookieStore.getCookies();
            loger.i("Print save cookies begin.");
            for (Cookie c : cookies) {
                loger.i("Cookie domain=" + c.getDomain() + ", path=" + c.getPath() + ", name=" + c.getName() + ", value=" + c.getValue());
            }
            loger.i("Print save cookies end.");
        }
    }
}
