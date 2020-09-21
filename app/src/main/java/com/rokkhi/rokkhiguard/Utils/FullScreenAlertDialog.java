package com.rokkhi.rokkhiguard.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.rokkhi.rokkhiguard.R;


public class FullScreenAlertDialog {
    Dialog mdialog;
    public FullScreenAlertDialog(Context context) {
        mdialog = new Dialog(context);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mdialog.setCancelable(false);
    }


    public void initdialog(Context context) {

        mdialog = new Dialog(context);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mdialog.setCancelable(false);

    }

    public void showdialog() {
        mdialog.show();
    }

    public void dismissdialog() {
        mdialog.dismiss();
    }


}