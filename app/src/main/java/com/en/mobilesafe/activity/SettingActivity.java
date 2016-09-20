package com.en.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.en.mobilesafe.R;
import com.en.mobilesafe.utils.ConstantValue;
import com.en.mobilesafe.utils.SpUtil;
import com.en.mobilesafe.view.SettingItemView;

/**
 * Created by en on 2016/9/19.
 */
public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        //sivUpdate.setTitle("自动更新设置");
        if (open_update){
       // sivUpdate.setDesc("自动更新已开启");
        sivUpdate.setChecked(true);}
        else {
           // sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前状态
            if (sivUpdate.isChecked())
            {
                //设置不勾选
                sivUpdate.setChecked(false);
                SpUtil.putBoolean(SettingActivity.this,ConstantValue.OPEN_UPDATE,false);
               // sivUpdate.setDesc("自动更新已关闭");

            } else {
              sivUpdate.setChecked(true);
                SpUtil.putBoolean(SettingActivity.this,ConstantValue.OPEN_UPDATE,true);
                //sivUpdate.setDesc("自动更新已开启");
            }

            }
        });
    }
}
