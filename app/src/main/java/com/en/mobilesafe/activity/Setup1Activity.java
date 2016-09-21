package com.en.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.en.mobilesafe.R;

/**
 * Created by en on 2016/9/21.
 */
public class Setup1Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
    public void nextOnClick(View view){
        startActivity(new Intent(Setup1Activity.this,Setup2Activity.class));
    }
}
