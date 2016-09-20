package com.en.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.en.mobilesafe.R;
import com.en.mobilesafe.utils.ConstantValue;
import com.en.mobilesafe.utils.SpUtil;
import com.en.mobilesafe.utils.ToastUtil;

public class HomeActivity extends Activity {
    private GridView gvHome;

    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

    private int[] mPics = new int[]{R.mipmap.home_safe, R.mipmap.home_callmsgsafe,
            R.mipmap.home_apps, R.mipmap.home_taskmanager,
            R.mipmap.home_netmanager, R.mipmap.home_trojan,
            R.mipmap.home_sysoptimize, R.mipmap.home_tools,
            R.mipmap.home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gvHome = (GridView) findViewById(R.id.gv_home);

        gvHome.setAdapter(new HomeAdapter());

        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0:
                       //手机防盗
                       showPasswordDailog();
                       break;

                   case 8:
                       //设置中心
                       startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                 default:
                     break;
               }

            }
        });
    }

    private void showPasswordDailog() {
        String str=SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,"");
        if(TextUtils.isEmpty(str))
            showPasswordSetDailog();
        else
            showPasswordInputDailog(str);
    }

    private void showPasswordInputDailog(final String savePassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_input_password, null);

        dialog.setView(view);

        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        //设置确定事件
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=etPassword.getText().toString();
                String str="";
                if (TextUtils.isEmpty(password)){
                    ToastUtil.show(getApplicationContext(),"密码不能为空");
                }
                else if (!password.equals(savePassword)){

                        ToastUtil.show(getApplicationContext(),"密码输入不正确,请重新输入");
                }
                else {

                    ToastUtil.show(getApplicationContext(),"登录成功");
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showPasswordSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_set_password, null);

        dialog.setView(view);

        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        //设置确定事件
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=etPassword.getText().toString();
                String passwordConfirm=  etPasswordConfirm.getText().toString();
                String str="";
                if (TextUtils.isEmpty(password)){
                    ToastUtil.show(getApplicationContext(),"密码不能为空");
                }
                else if (!password.equals(passwordConfirm)){
                    if  (TextUtils.isEmpty(passwordConfirm))
                        ToastUtil.show(getApplicationContext(),"确认密码不能为空");
                    else
                        ToastUtil.show(getApplicationContext(),"两次输入密码不一致");
                }
                else {
                    ToastUtil.show(getApplicationContext(),"设置成功");
                    SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD,password);
                    dialog.dismiss();
                }
            }
        });

         btnCancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
             }
         });
        dialog.show();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= View.inflate(HomeActivity.this,R.layout.home_list_item,null);

            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            tvItem.setText(mItems[position]);
            ivItem.setImageResource(mPics[position]);
            return view;
        }
    }
}
