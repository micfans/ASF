package org.openpackage.base.core.http;

/**
 * Created by micfans on 23/11/2016.
 */

public class ResponseBean implements IResponseBean {

    private String response;

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public void setReponse(String response) {
        this.response = this.getResponse();
    }

    @Override
    public String getResponse() {
        return response;
    }
}
