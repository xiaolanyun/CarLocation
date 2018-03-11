package tenyond.yank.com.carlocation.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import tenyond.yank.com.carlocation.R;

/**
 * AUTHOR: Dream
 * DATE:  2018/3/7
 * EMAIL: YANK.TENYOND@GMAIL.COM
 * FUNC:
 */

public class InfoPopWindow extends PopupWindow {



    public InfoPopWindow(final Activity context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_window,null);


        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.update();
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#6fffffff"));
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.Widget_AppCompat_PopupWindow);

    }

    public void showPopWindow(View parent){
        if(!this.isShowing()){
            this.showAtLocation(parent, Gravity.BOTTOM,0,0);
        }else {
            this.dismiss();
        }
    }


    public void dismisses(){
        this.dismiss();
    }


}
