package szz.com.thirldworldv2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class MsgReceiveService extends Service {


    private UdpHelper udphelper;
    private Thread tReceived;

    public MsgReceiveService() {
    }

    public void onCreate() {//用于创建线程
        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        //传递WifiManager对象，以便在UDPHelper类里面使用MulticastLock
        udphelper = new UdpHelper(manager);
//        udphelper.addObserver(MsgReceiveService.this);
        tReceived = new Thread(udphelper);
        tReceived.start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
