package org.openpackage.asf.comp.c1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c1_activity_main);
        setTitle(this.getLocalClassName());
    }

    public void stopListScroll(View view){
        ListScrollCompView _view = (ListScrollCompView)this.findViewById(R.id.listScorllView);
        _view.stop();
    }


    public void startListScroll(View view){
        ListScrollCompView _view = (ListScrollCompView)this.findViewById(R.id.listScorllView);
        _view.start();
    }

    public void stopAnimScroll(View view){
        AnimScrollCompView _view = (AnimScrollCompView)this.findViewById(R.id.animScrollView);
        _view.stop();
    }


    public void startAnimScroll(View view){
        AnimScrollCompView _view = (AnimScrollCompView)this.findViewById(R.id.animScrollView);
        _view.start();
    }
}
