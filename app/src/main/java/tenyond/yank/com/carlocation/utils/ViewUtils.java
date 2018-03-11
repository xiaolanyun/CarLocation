package tenyond.yank.com.carlocation.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * AUTHOR: Dream
 * DATE:  2018/1/18
 * FUNC: View工具类
 */

public class ViewUtils {

    /**
     * 通过添加占位View的方式设置沉浸式状态栏，本方法在setContentView()前调用
     * @param activity 当前活动
     * @param color 颜色值
     */
    public static void setStatusBar(Activity activity,int color){
        Window window = activity.getWindow();
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            //设置一个半透明的状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //创建一个和状态栏等大的View用于占位
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            View statusBar = new View(activity);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));

            //将此占位View的颜色设置为和状态栏相同
            statusBar.setBackgroundColor(color);

            //在布局顶端添加占位View
            contentView.addView(statusBar,layoutParams);
        }else{

            //5.0过后可以直接设置沉浸式状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            //设置状态栏颜色
            window.setStatusBarColor(color);

            //6.0设置状态栏的图标颜色配合toolbar
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }



    }

    /**
     * 求得当前系统状态栏高度
     * @param activity 当前活动
     * @return 当前状态栏高度
     */
    public static int getStatusBarHeight(Activity activity){
        int result =0;
        int resourseId = activity.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourseId > 0){
            result = activity.getResources().getDimensionPixelSize(resourseId);
        }
        return  result;
    }

    public static void showToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
