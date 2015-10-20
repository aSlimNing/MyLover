package cn.bzu.internet.mylover;

import android.app.Application;

import com.github.pedrovgs.lynx.LynxShakeDetector;

/**
 * Created by monster on 15-10-19-019.
 */
public class RApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LynxShakeDetector lynxShakeDetector = new LynxShakeDetector(this);
        lynxShakeDetector.init();
    }
}
