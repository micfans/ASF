package org.openpackage.base.core.http.wrap;

import org.openpackage.base.core.http.InnerResponseHandler;

import java.util.Map;

/**
 * Created by micfans on 23/11/2016.
 */

public class RequestInfo {

    private String url;
    private Map<String,Object> params;
    private InnerResponseHandler innerResponseHandler;

    public InnerResponseHandler getInnerResponseHandler() {
        return innerResponseHandler;
    }

    public void setInnerResponseHandler(InnerResponseHandler innerResponseHandler) {
        this.innerResponseHandler = innerResponseHandler;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
