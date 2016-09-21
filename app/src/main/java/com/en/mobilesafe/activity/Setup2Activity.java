package com.en.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;

import com.en.mobilesafe.R;
import com.en.mobilesafe.utils.ConstantValue;
import com.en.mobilesafe.utils.SpUtil;
import com.en.mobilesafe.view.SettingItemView;

/**
 * Created by en on 2016/9/21.
 */
public class Setup2Activity extends Activity {

    private SettingItemView simSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        simSave = (SettingItemView) findViewById(R.id.sim_save);
        boolean isSimSave= SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY,false);
    }
}
