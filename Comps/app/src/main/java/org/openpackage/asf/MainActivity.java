package org.openpackage.asf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    /**
     *
     */
    private static List<String> compDemos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCompDemos();
    }

    private void initCompDemos() {
        getActivities();
        ViewGroup demos = (ViewGroup) this.findViewById(R.id.demos);
        for (String clstr : compDemos) {
            Class _cls = null;
            try{
                _cls = Class.forName(clstr);
            }catch(Exception ex){
                continue;
            }
            final Class cls = _cls;
            Button textView = new Button(this);
            String cn = cls.getName().substring(20);
            textView.setText(cn);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, cls);
                    MainActivity.this.startActivity(intent);
                }
            });
            demos.addView(textView);
        }
    }

    private void getActivities() {
        compDemos.add("org.openpackage.asf.comp.c1.MainActivity");
        compDemos.add("org.openpackage.asf.comp.c2.MainActivity");
    }
}
