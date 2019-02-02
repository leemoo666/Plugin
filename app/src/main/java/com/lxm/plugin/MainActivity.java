package com.lxm.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lxm.mvvm.ActivityTimeManger;
import com.lxm.mvvm.AutoWired;
import com.lxm.mvvm.Method;
import com.lxm.mvvm.Repository;

@Repository
public class MainActivity extends Activity {

    @AutoWired
    TextView tvMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPrint();
    }

    @Method
    void initPrint(){
        Log.i("lxm","init");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
