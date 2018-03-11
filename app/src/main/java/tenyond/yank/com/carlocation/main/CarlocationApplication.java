package tenyond.yank.com.carlocation.main;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

/**
 * AUTHOR: Dream
 * DATE:  2018/3/6
 * EMAIL: YANK.TENYOND@GMAIL.COM
 * FUNC:
 */

public class CarlocationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
