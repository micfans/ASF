package org.openpackage.asf.comp.c1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.openpackage.asf.base.util.Loger;

/**
 * Created by micfans on 22/11/2016.
 */

public class ListScrollCompView extends CompView {

    private static final Loger loger = Loger.getLoger(ListScrollCompView.class);

    /**
     * View
     */
    private View rootView;
    private ListView listView;
    private CompListAdapter listViewAdapter;


    private int viewHeight = 0;


    public ListScrollCompView(Context context) {
        super(context);
    }

    public ListScrollCompView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListScrollCompView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void initView() {
        rootView = inflate(getContext(), R.layout.c1_listscrol_main, this);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listViewAdapter = super.createListViewAdapter(getTexts(), Integer.MAX_VALUE);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    protected void initListener() {

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void caclHeight() {
        if(this.isInEditMode()){

        }else {
            for (int i = 0; i < viewRows; i++) {
                View temp = listViewAdapter.getView(i, null, listView);
                temp.measure(0, 0);
                viewHeight += temp.getMeasuredHeight() + listView.getDividerHeight();
            }
            LayoutParams layoutParams = (LayoutParams) listView.getLayoutParams();
            layoutParams.height = viewHeight;
            listView.setLayoutParams(layoutParams);
        }
    }

    /**
     *
     */
    @Override
    protected void scroll() {
        this.post(new Runnable() {
            @Override
            public void run() {
                currentRow = currentRow + viewRows;
                listView.smoothScrollToPositionFromTop(currentRow, 0, scrollDuration);

            }
        });
    }
}
