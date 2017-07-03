package szz.com.baselib.entity.events;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.thirdworld.application.ContextHolder;

/**
 * 作者：Ying.Huang on 2017/6/24 12:00
 * Version v1.0
 * 描述：
 */

public class LogStr {

    public final String logMsg;


    public static LogStr newInstance(@StringRes int resId, Object... args){

        String msg = ContextHolder.getString(resId,args);
        return newInstance(msg);
    }

    public static LogStr newInstance(String msg){
        return new LogStr(msg);
    }

    public LogStr(@NonNull String logMsg) {
        this.logMsg = logMsg;
    }
}
