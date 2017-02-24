package org.openpackage.asf.comp.c1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micfans on 21/11/2016.
 */

public class CompListAdapter extends BaseAdapter {


    private List list = new ArrayList();
    private Context context;
    private int size;

    CompListAdapter(Context context,  List list, int size) {
        this.context = context;
        this.list = list;
        this.size = size;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.c1_list_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        String text = list.get(position % list.size()).toString();
        textView.setText(text);
        return view;
    }

}
