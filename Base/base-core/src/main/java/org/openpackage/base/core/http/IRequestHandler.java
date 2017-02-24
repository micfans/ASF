package org.openpackage.base.core.http;

/**
 * Created by micfans on 23/11/2016.
 */

public interface IRequestHandler {

    boolean cancel();

    boolean isCanceled();

    boolean isFinished();

}
