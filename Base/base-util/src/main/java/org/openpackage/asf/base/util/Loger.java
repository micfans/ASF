package org.openpackage.asf.base.util;

import android.util.Log;

/**
 * Created by micfans on 9/28/16.
 */

public class Loger {

    /**
     *
     */
    private Class clazz;
    /**
     *
     */
    private String root;

    /**
     *
     */
    public static boolean trace = true;

    /**
     *
     * @return
     */
    private static boolean trace() {
        return trace;
    }

    /**
     *
     * @param clazz
     */
    private Loger(Class clazz) {
        this.clazz = clazz;
        this.root = clazz.getSimpleName();
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static Loger getLoger(Class clazz) {
        return new Loger(clazz);
    }

    /**
     *
     * @param content
     * @return
     */
    private String renderContent(String content) {
        String[] infos = getAutoJumpLogInfos();
        return infos[0] + "." + infos[1] + " " + infos[2] + ": " + content;
    }

    /**
     * 获取打印信息所在方法名，行号等信息
     * @return
     */
    private static String[] getAutoJumpLogInfos() {
        String[] infos = new String[] { "", "", "" };
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 6) {
            return infos;
        } else {
            StackTraceElement e5 = elements[5];
            infos[0] = e5.getClassName().substring(
                    e5.getClassName().lastIndexOf(".") + 1);
            infos[1] = e5.getMethodName() + "()";
            infos[2] = " at (" + e5.getClassName() + ".java:"
                    + e5.getLineNumber() + ")";
            return infos;
        }
    }


    /**
     *
     * @param content
     */
    public void d(String content) {
        if (trace())
            Log.d(root, renderContent(content));
    }

    /**
     *
     * @param content
     * @param t
     */
    public void d(String content, Throwable t) {
        if (trace())
            Log.d(root, renderContent(content), t);
    }

    /**
     *
     * @param content
     */
    public void i(String content) {
        if (trace())
            Log.i(root, renderContent(content));
    }

    /**
     *
     * @param content
     * @param t
     */
    public void i(String content, Throwable t) {
        if (trace())
            Log.i(root, renderContent(content), t);
    }

    /**
     *
     * @param content
     */
    public void w(String content) {
        if (trace())
            Log.w(root, renderContent(content));
    }

    /**
     *
     * @param content
     * @param t
     */
    public void w(String content, Throwable t) {
        if (trace())
            Log.w(root, renderContent(content), t);
    }

    /**
     *
     * @param content
     * @param t
     */
    public void e(String content, Throwable t) {
        Log.e(root, renderContent(content), t);
    }

    /**
     *
     * @param content
     */
    public void e(String content) {
        Log.e(root, renderContent(content));
    }
}
