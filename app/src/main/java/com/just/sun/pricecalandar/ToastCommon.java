package com.just.sun.pricecalandar;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by pc-004 on 2015/8/14.
 */
public class ToastCommon {
    private static Toast toast;

    private ToastCommon() {
    }

    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            toast.cancel();
            toast = null;//toast隐藏后，将其置为null
        }
    };

    /**
     * 显示Toast
     *
     * @param context
     * @param root
     * @param tvString
     */

    public static void toastShortShow(Context context, ViewGroup root, String tvString) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_diy, root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        //        ImageView mImageView = (ImageView) layout.findViewById(R.id.iv);
        //        mImageView.setBackgroundResource(R.drawable.ic_launcher);
        text.setText(tvString);
        mHandler.removeCallbacks(r);
        if (toast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
            toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
        }
        //        mHandler.postDelayed(r, 1000);//延迟1秒隐藏toast
        mHandler.postDelayed(r, 1000);//延迟1秒隐藏toast

        toast.show();
    }
}
