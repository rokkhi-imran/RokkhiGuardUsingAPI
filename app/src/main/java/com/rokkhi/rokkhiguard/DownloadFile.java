package com.rokkhi.rokkhiguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile extends AsyncTask<String, Integer, String> {
    String downloadLink;
    ProgressDialog progressDialog;
    Context context;

    public DownloadFile(String downloadLink, ProgressDialog progressDialog, Context context) {
        this.downloadLink = downloadLink;
        this.progressDialog = progressDialog;
        this.context=context;

    }

    @Override
    protected String doInBackground(String... strings) {

        int count;
        try {
            Log.e("TAG", "download Link: " + downloadLink);

            URL url = new URL(downloadLink);
            URLConnection conexion = url.openConnection();
            conexion.setRequestProperty("connection", "close");
            conexion.connect();

            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conexion.getContentLength();

            Log.e("TAG", "doInBackground: lenghtOfFile "+lenghtOfFile);
            // downlod the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(
                    Environment.getExternalStorageDirectory().getAbsolutePath() +"/guardAPK.apk"
            );
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / lenghtOfFile));
                Log.e("TAG", "doInBackground: Count =  " + count);
                Log.e("TAG", "doInBackground: total =  " + total);
                Log.e("TAG", "doInBackground: total % =  " + (total * 100 / lenghtOfFile));

                output.write(data, 0, count);

            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute: started Downloaded" );
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("TAG", "onPostExecute: s = "+s );
        progressDialog.dismiss();
        install();
        super.onPostExecute(s);



    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
//        progressDialog.setMessage(values[0].toString()+" "+"Downloading new Apk...");
        Log.e("TAG", "onProgressUpdate: "+values[0].toString() );

    }

    private void install() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/guardAPK.apk");

        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri downloadedApk = FileProvider.getUriForFile(context,  BuildConfig.APPLICATION_ID+".com.vansuita.pickimage.provider", file);
                intent.setDataAndType(downloadedApk, type);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            context.startActivity(intent);
        } else {
            Toast.makeText(context, "ّFile not found!", Toast.LENGTH_SHORT).show();
        }
    }
}