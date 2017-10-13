package com.helper.west2ol.fzuhelper.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.helper.west2ol.fzuhelper.activity.MainContainerActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by coderqiang on 2017/10/13.
 */

public class DownloadAPK {
    private static final String TAG = "DownloadAPK";
    private static final boolean DEBUG = true;

    private static DownloadAPK mInstance;

    private String url="";

    private ProgressDialog downloadProDialog;
    private String appname = "fzuHelper.apk";

    public DownloadAPK(){}

    public static DownloadAPK getInstance(String url){
        if(mInstance == null){
            mInstance = new DownloadAPK();
        }
        mInstance.url=url;
        return mInstance;
    }

    public void download(final Context context){//下载异步线程
        if (DEBUG) Log.i(TAG, "start downloadApkFile");
        downloadProgress(context);
        new Download(context).execute();
    }

    public class Download extends AsyncTask<Void, Integer, Void> {

        private Context mContext;

        public Download(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground:DownLoad ");
            String savePath="";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                savePath= Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ appname;
            }else {
                savePath= Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ appname;
            }

            Log.i(TAG, "doInBackground: url"+url);
            String serverFilePath =url;//这里我模拟下载一个qq老版本APK，相当于从服务器获取
            try {
                URL serverURL = new URL(serverFilePath);
                HttpURLConnection connect = (HttpURLConnection) serverURL.openConnection();
                BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());
                File apkfile = new File(savePath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(apkfile));

                int n;
                byte[] buffer = new byte[1024];
                while((n=bis.read(buffer, 0, buffer.length))!=-1){
                    bos.write(buffer, 0, n);
                }
                bis.close();
                bos.close();
                connect.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                if (DEBUG) Log.e(TAG, "downloadFile exception !" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            install(mContext);
            downloadProDialog.dismiss();

        }
    }

    public void downloadProgress(Context context){//自己定义的一个下载的进度条
        downloadProDialog = new ProgressDialog(context);
        downloadProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        downloadProDialog.setTitle("正在下载");
        downloadProDialog.setMessage("下载中,请等待...");
        downloadProDialog.setMax(100);
        downloadProDialog.setProgress(0);
        downloadProDialog.setSecondaryProgress(0);
        downloadProDialog.setIndeterminate(false);
        downloadProDialog.setCancelable(false);
        downloadProDialog.show();
    }

    private void install(Context context){
        String fileName =Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ appname;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.helper.west2ol.fzuhelper.fileProvider", new File(fileName));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        DataCleanUtil.cleanSharedPreference(context);
        DataCleanUtil.cleanDatabases(context);
        context.startActivity(intent);
        ((MainContainerActivity)context).finish();

    }
}
