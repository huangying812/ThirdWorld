package szz.com.thirldworldv2;

import android.app.Application;
import android.content.Context;

/**
 * 作者：Ying.Huang on 2017/6/14 09:10
 * Version v1.0
 * 描述：
 */

public class ThirdWorldApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ContextHolder.init(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
