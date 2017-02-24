package org.openpackage.base.core.http.wrap;

import org.openpackage.base.core.http.IHttpRequester;
import org.openpackage.base.core.http.IResponseBean;
import org.openpackage.base.core.http.IResponseHandler;
import org.openpackage.base.core.http.InnerResponseHandler;
import org.openpackage.base.core.http.ResponseBean;

import java.util.List;

/**
 * Created by micfans on 23/11/2016.
 */

public class QueueRequester {

    /**
     * 一个失败，则不再进行下一个
     */
    public static final int FAILD_MODE_ONE = 1;
    /**
     * 一个失败，继续进行下一个
     */
    public static final int FAILD_MODE_ALL = 0;

    /**
     * fail mode
     */
    private int faildMode;
    /**
     * request method(post/get)
     */
    private String method;
    /**
     * HttpRequester to use
     */
    private IHttpRequester requester;
    /**
     * all ris size
     */
    private int all;
    /**
     * curr ri
     */
    private int curr;
    /**
     * current fail num.
     */
    private int failds;
    /**
     * current success nu.
     */
    private int successes;

    /**
     *
     * @param requester
     * @param method
     * @param faildMode
     */
    public QueueRequester(IHttpRequester requester, String method, int faildMode){
        this.requester = requester;
        this.faildMode = faildMode;
        this.method = method;
    }

    /**
     *
     * @param requestInfoList
     * @param responseHandler
     */
    public void posts(List<RequestInfo> requestInfoList, final IResponseHandler responseHandler){
        all = requestInfoList.size();
        if(requestInfoList.isEmpty()){
            responseHandler.onFinish();
            return;
        }
        while(curr < all && curr > 0){
            final RequestInfo ri = requestInfoList.get(curr);
            requester.req(method, ri.getUrl(), ri.getParams(), new InnerResponseHandler() {
                @Override
                public void onFailure(String statusCode) {
                    failds ++;
                    if(faildMode == FAILD_MODE_ONE){
                        curr = -1;
                    }
                    ri.getInnerResponseHandler().onFailure(statusCode);
                }

                @Override
                public void onResponse(IResponseBean responseBean) {
                    successes ++;
                    ri.getInnerResponseHandler().onResponse(responseBean);
                }

                @Override
                public void onFinish() {
                    curr ++;
                    ri.getInnerResponseHandler().onFinish();
                }
            });
        }
        if(successes == all) {
            responseHandler.onResponse(new ResponseBean());
        }else{
            responseHandler.onFailure(String.valueOf(failds));
        }
    }

}
