package com.en.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.en.mobilesafe.R;
import com.en.mobilesafe.utils.ConstantValue;
import com.en.mobilesafe.utils.SpUtil;
import com.en.mobilesafe.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by en on 2016/8/6.
 */
public class SplashActivity extends Activity {

    private RelativeLayout sp_root;
    private TextView versionName_textView;
    private int mLocalVersionCode;//本地版本号
    private String downloadUrl;//下载地址
    private String versionName;//版本名
    private String versionCode;//版本号
    private String versionDes;//描述


    private static final int UPDATE_VERSION = 100;//更新新版本的状态码

    private static final int ENTER_HOME = 101;//进入应用程序主界面状态码


    private static final int URL_ERROR = 102;//url出错

    /**
     * IO出错
     */
    private static final int IO_ERROR = 103;
    /**
     * JSON语法错误
     */
    protected static final int JSON_ERROR = 104;
    /**
     * 信息传递
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    // 弹出对话框,提示用户更新
                    showUpdateDialog();
                    Log.i(TAG, UPDATE_VERSION + "UPDATE_VERSION");
                    break;
                case ENTER_HOME:
                    enterHome();
                default:

                    Log.i(TAG, "测试");
                    break;
            }
        }
    };

    protected void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 显示更新对话框
     */

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(versionDes);

        //立即更新

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
         //稍后再说
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                enterHome();

            }
        });

        //取消更新
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        //显示对话框
        builder.show();
    }

    /**
     * 下载APK
     */
    private void downLoadApk() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取sd卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobilesafe.apk";

            //发送请求，获取apk,并放置到指定的路径
            HttpUtils httpUtils = new HttpUtils();

            httpUtils.download(downloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i(TAG, "下载成功");
                    File file = responseInfo.result;
                    // 提示用户安装k
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Log.i(TAG, "下载失败");
                }

                @Override
                public void onStart() {
                    super.onStart();
                    Log.i(TAG, "刚刚开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);

                    Log.i(TAG, "下载中........");
                    Log.i(TAG, "total = " + total);
                    Log.i(TAG, "current = " + current);
                }
            });
        }
    }

    /**
     * 安装Apk
     *
     * @param file 要安装的文件
     */
    private void installApk(File file) {
        // 系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        /*
         * //文件作为数据源 intent.setData(Uri.fromFile(file)); //设置安装的类型
		 * intent.setType("application/vnd.android.package-archive");
		 */
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        // startActivity(intent);
        startActivityForResult(intent, 0);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        initUI();
        // 初始化动画
        initAnimation();

        // 初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        // 应用版本名称
        versionName_textView.setText("版本号：" + getVersionNameOrCode("name"));//

        // 获得版本号
        mLocalVersionCode = (int) getVersionNameOrCode("code");

        // 核对版本号


        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false))
            checkVersion();
        else {
            //直接进入应用程序主界面
            //enterHome();
            //消息机制
         mHandler.sendEmptyMessageDelayed(ENTER_HOME, 2000);
        }
    }

    /**
     * 版本检查
     */
    private void checkVersion() {
        new Thread() {

            @Override
            public void run() {
                // 发送请求获取数据,参数则为请求json的链接地址
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();

                try {
                    URL url = new URL("http://192.168.1.189:8080/update.json");
                    // HttpURLConnection
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        //请求超时
                        connection.setConnectTimeout(2000);
                        //读取超时
                        connection.setReadTimeout(2000);

                        int responseCode = connection.getResponseCode();
                        Log.i(TAG, "responseCode:" + responseCode);

                        if (responseCode != -1) {
                            InputStream inputStream = connection.getInputStream();
                            String json = StreamUtil.streamToString(inputStream);

                            Log.i(TAG, json);

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                downloadUrl = jsonObject.getString("downloadUrl");
                                versionName = jsonObject.getString("versionName");
                                versionDes = jsonObject.getString("versionDes");
                                versionCode = jsonObject.getString("versionCode");

                                Log.i(TAG, downloadUrl);
                                Log.i(TAG, versionName);
                                Log.i(TAG, versionDes);
                                Log.i(TAG, versionCode);
                                if (Integer.parseInt(versionCode) > mLocalVersionCode)
                                    msg.what = UPDATE_VERSION;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }


    /**
     * @param nc nc = "name" or nc = "code"
     * @return nc = "name" 时返回 versionName； nc = "code" 时返回 versionCode
     */
    private Object getVersionNameOrCode(String nc) {
        // 包管理者对象packageManager
        PackageManager pm = getPackageManager();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            if (TextUtils.equals("name", nc))
                return packageInfo.versionName;
            if (TextUtils.equals("code", nc))
                return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        // 初始化动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        // 淡入淡出设置为2秒
        alphaAnimation.setDuration(2000);
        //
        sp_root.startAnimation(alphaAnimation);
    }


    /**
     * 初始化控件
     */
    private void initUI() {

        versionName_textView = (TextView) findViewById(R.id.tv);

        sp_root = (RelativeLayout) findViewById(R.id.sp_root);

    }

}
