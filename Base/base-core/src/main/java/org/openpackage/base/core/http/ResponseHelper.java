package org.openpackage.base.core.http;

import com.google.gson.Gson;

/**
 * Created by micfans on 23/11/2016.
 */

public class ResponseHelper {


    private static Gson gson = new Gson();

    public static IResponseBean parseResponse(String response, InnerResponseHandler innerResponseHandler){
        IResponseBean bean;
        switch (innerResponseHandler.getResponseType()){
            case IResponseHandler.TYPE_JSON:
                bean = (IResponseBean) gson.fromJson(response, innerResponseHandler.getBeanClass());
                break;
            case IResponseHandler.TYPE_TEXT:
                bean = new ResponseBean();
                break;
            case IResponseHandler.TYPE_XML:
                //TODO
            default:
                throw new IllegalArgumentException("Response type " + innerResponseHandler.getResponseType() + " not implement yet.");
        }
        bean.setReponse(response);
        return bean;
    }
}
