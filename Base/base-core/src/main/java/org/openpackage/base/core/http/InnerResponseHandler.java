package org.openpackage.base.core.http;


/**
 * Created by Administrator on 2016/8/11 0011.
 */
public abstract class InnerResponseHandler implements IResponseHandler {

    private Class beanClass;
    private int responseType;

    public InnerResponseHandler(){
        beanClass = ResponseBean.class;
        responseType = IResponseHandler.TYPE_TEXT;
    }

    public InnerResponseHandler( Class beanClass, int responseType){
        this.beanClass = beanClass;
        this.responseType = responseType;
    }

    public Class<IResponseBean> getBeanClass(){
        return beanClass;
    }

    public int getResponseType(){
        return responseType;
    }
}
