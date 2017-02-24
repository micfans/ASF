package org.openpackage.asf.comp.c1;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.openpackage.asf.base.util.Loger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by micfans on 21/11/2016.
 */

public abstract class CompView extends RelativeLayout {


    private static final Loger loger = Loger.getLoger(CompView.class);


    /**
     * 是否修复结束行为空行
     */
    protected boolean fixEnd = false;
    /**
     * 是否自动启动
     */
    protected boolean autoStart = true;
    /**
     * 每页条数
     */
    protected int viewRows;
    /**
     * 滚动间隔时间（ms）
     */
    protected int scrollInterval;
    /**
     * 滚动时间（ms）
     */
    protected int scrollDuration;
    /**
     * 适配器（class nmme）
     */
    protected String listAdapterClassName;




    protected Class listAdapterClass;
    //current row
    protected int currentRow = 0;
    //records
    protected List texts = new ArrayList();
    //timer
    private Timer timer = new Timer();


    // ------------ demo value -------------- //

    {
        List<String> demos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            demos.add(i + ". this is demo text.");
        }
        this.setTexts(demos);
    }

    // ------------ demo value -------------- //


    /**
     * @param context
     */
    public CompView(Context context) {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public CompView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CompView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
        init();
    }

    /**
     *
     */
    public void start() {
        stop();
        if (texts.size() > viewRows) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    scroll();
                }
            }, scrollInterval, scrollInterval);
        }
    }

    /**
     *
     */
    public void stop() {
        timer.cancel();
    }


    /**
     *
     */
    protected abstract void initView();


    /**
     *
     * @param context
     * @param attrs
     */
    private void initParams(Context context, AttributeSet attrs){
        TypedArray arr = context.obtainStyledAttributes(attrs,
                R.styleable.c1);
        this.fixEnd = arr.getBoolean(R.styleable.c1_c1_fixEnd, false);
        this.autoStart = arr.getBoolean(R.styleable.c1_c1_autoStart, true);
        this.scrollDuration = arr.getInt(R.styleable.c1_c1_scrollDuration, 1000);
        this.scrollInterval = arr.getInt(R.styleable.c1_c1_scrollInteval, 3000);
        this.viewRows = arr.getInt(R.styleable.c1_c1_viewRows, 3);
        this.listAdapterClassName = arr.getString(R.styleable.c1_c1_listItemAdapter);

        if(scrollInterval <= scrollDuration){
            throw new IllegalArgumentException("Attribute scrollInteval must be greater than scrollDuration.");
        }

        if(listAdapterClassName == null){
            listAdapterClassName = CompListAdapter.class.getName();
        }
        try {
            listAdapterClass =  Class.forName(listAdapterClassName);
            if(!CompListAdapter.class.isAssignableFrom(listAdapterClass)){
                throw new IllegalArgumentException("Attribute listAdapterClassName must extends ComListAdapter or not set.");
            }
        }catch (ClassNotFoundException ex){
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**
     *
     */
    private void init() {
        initView();
        caclHeight();
        initListener();
        initTimer();
    }


    /**
     *
     */
    protected abstract void initListener();

    /**
     *
     */
    private void fixTexts() {
        if (fixEnd || viewRows > texts.size()) {
            int v = texts.size() % viewRows;
            for (int i = 0; i < viewRows - v; i++) {
                texts.add("");
            }
        }
    }

    /**
     *
     */
    protected abstract void caclHeight();

    /**
     *
     */
    private void initTimer() {
        fixTexts();
        if(autoStart) {
            start();
        }
    }

    /**
     *
     */
    protected abstract void scroll();

    /**
     *
     * @param list
     * @param size
     * @return
     */
    protected CompListAdapter createListViewAdapter(List list, int size){
        CompListAdapter adapter = null;
        try {
            loger.i("Creating instance of " + listAdapterClass);
            adapter = (CompListAdapter) listAdapterClass.getConstructor(Context.class, List.class, int.class).newInstance(this.getContext(), list, size);
        }catch(Exception ex){
            loger.e(ex.getMessage(), ex);
        }
        return adapter;
    }

    // --------------- params ---------------- //

    /**
     * @return
     */
    public List getTexts() {
        return texts;
    }

    /**
     * @param texts
     */
    public void setTexts(List<String> texts) {
        this.texts = texts;
    }
}
