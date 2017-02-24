package org.openpackage.asf.comp.c1;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.openpackage.asf.base.util.ViewIdGener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micfans on 22/11/2016.
 */

public class AnimScrollCompView extends CompView {

    /**
     * View
     */
    private View rootView;
    private ViewGroup wrapView;


    private int viewHeight = 0;
    private ValueAnimator animation;

    private int mc = 0;


    /**
     * @param context
     */
    public AnimScrollCompView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AnimScrollCompView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public AnimScrollCompView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        rootView = inflate(getContext(), R.layout.c1_animscroll_main, this);
        wrapView = (ViewGroup) rootView.findViewById(R.id.wrapView);
        ListView listView = initNextListView(null);
        initNextListView(listView);
    }

    /**
     * @param prevListView
     * @return
     */
    private ListView initNextListView(ListView prevListView) {
        ListView listView = new ListView(getContext());
        List list = getNextTexts();
        CompListAdapter listViewAdapter = new CompListAdapter(this.getContext(), list, list.size());
        listView.setAdapter(listViewAdapter);

        if (viewHeight == 0) {
            View temp = listView.getAdapter().getView(0, null, listView);
            temp.measure(0, 0);
            viewHeight = (temp.getMeasuredHeight() + listView.getDividerHeight()) * viewRows;
        }

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp1.height = viewHeight;
        if (prevListView != null) {
            lp1.addRule(RelativeLayout.BELOW, prevListView.getId());
        }
        listView.setDivider(null);
        listView.setLayoutParams(lp1);
        listView.setVerticalScrollBarEnabled(false);
        listView.setId(findListViewId());

        wrapView.addView(listView);
        return listView;
    }

    /**
     * @return
     */
    private int findListViewId() {
        return ViewIdGener.generateViewId();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void caclHeight() {

        LayoutParams lp2 = (LayoutParams) wrapView.getLayoutParams();
        lp2.height = viewHeight;

        wrapView.setLayoutParams(lp2);

        animation = ValueAnimator.ofInt(0, viewHeight);
        animation.setInterpolator(new AnticipateInterpolator());
        animation.setDuration(scrollDuration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (wrapView.getChildCount() > 1) {
                    wrapView.removeViewAt(0);
                    initNextListView((ListView) wrapView.getChildAt(0));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @Override
    protected void scroll() {
        this.post(new Runnable() {
            @Override
            public void run() {
                animation.cancel();
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final ListView listView = (ListView) wrapView.getChildAt(0);
                        final LayoutParams lp = (LayoutParams) listView.getLayoutParams();
                        int n = (int) animation.getAnimatedValue();
                        int c = mc - n;
                        lp.topMargin = c;
                        listView.setLayoutParams(lp);
                    }
                });
                animation.start();
            }
        });
    }


    /**
     * @return
     */
    private synchronized List getNextTexts() {
        List list = new ArrayList();
        int all = texts.size();
        if (currentRow > all) {
            currentRow = 0;
        }
        int cll = currentRow + viewRows;
        for (int i = currentRow; i < cll && i < all; i++) {
            list.add(texts.get(i));
        }
        cll = cll - all;
        if (cll > 0) {
            for (currentRow = 0; currentRow < cll; currentRow++) {
                list.add(texts.get(currentRow));
            }
        } else {
            currentRow += viewRows;
        }
        return list;
    }


    // ----------- Params -------------- //

    public void setAnimationInterpolator(TimeInterpolator interpolator) {
        animation.setInterpolator(interpolator);
    }

    public TimeInterpolator getAnimationInterpolator() {
        return animation.getInterpolator();
    }
}
