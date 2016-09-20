package com.en.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.en.mobilesafe.R;

/**
 * Created by en on 2016/9/19.
 */
public class SettingItemView extends RelativeLayout {
    private String  NAMESPACE="http://schemas.android.com/apk/res/com.en.mobilesafe";
    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        int attributeCount = attrs.getAttributeCount();
//        for (int i=0;i<attributeCount;i++){
//            String attributeName = attrs.getAttributeName(i);
//            String attributeValue = attrs.getAttributeValue(i);
//            System.out.println(attributeName+"="+attributeValue);
//        }

        mTitle = attrs.getAttributeValue(NAMESPACE, "title");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();
    }

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        //将自定义好的布局文件设置给当前的SettingItemView
        View view = View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
        setTitle(mTitle);//设置标题
    }

   public void setTitle(String title){
       tvTitle.setText(title);
   }

    public void setDesc(String desc){
        tvDesc.setText(desc);
    }

    /**
     * 判断当前勾选状态
     * @return
     */
    public boolean isChecked(){
        return cbStatus.isChecked();
    }

    public void setChecked( boolean check){
        cbStatus.setChecked(check);

        //根据选择的状态,更新文本描述
        if (check)
            setDesc(mDescOn);
        else
            setDesc(mDescOff);
    }
}
