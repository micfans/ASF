package org.openpackage.base.core.http;

/**
 * Created by micfans on 23/11/2016.
 */

public interface IResponseBean {

    boolean isSuccess();

    void setReponse(String response);

    String getResponse();
}
