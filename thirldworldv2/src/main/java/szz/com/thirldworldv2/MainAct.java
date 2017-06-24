package szz.com.thirldworldv2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.Observable;
import java.util.Observer;

public class MainAct extends Activity implements Observer {


    EditText msg_et = null;
    Button send_bt = null;
    TextView info_tv = null;
    private static final String TAG = "MainAct";
//    private UDPClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.udp_test);
        msg_et = (EditText) findViewById(R.id.edit_msg);
        send_bt = (Button) findViewById(R.id.send_bt);
        info_tv = (TextView) findViewById(R.id.receive_msg);
        info_tv.setText("source\n");
//        client = new UDPClient(mHander);
        /*
        // 开启服务器
        ExecutorService exec = Executors.newCachedThreadPool();
        UDPServer server = new UDPServer();
        exec.execute(server);*/
        // 发送消息
        send_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                /*myThread1 thread = new myThread1("22");
                new Thread(thread).start();*/

                String s = "盟军活力抢活力开始〓hy815〓123\t\n";
                String s1 = "激活2014图鉴〓hy815\t\r\n";

                Log.d(TAG, "MyThread  execu" + msg_et.getText().toString());
                String buf1 = msg_et.getText().toString();
                switch (count % 3) {
                    case 0:
                        buf1 = s;
                        break;
                    case 1:
                        buf1 = s1;
                        break;
                }
                try {
                    socket_cmd.send(buf1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        socket_cmd = new Socket_Cmd();
        socket_cmd.addObserver(this);
        new Thread(socket_cmd).start();
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 20; i++) {
            String buf1 = "TCP:I AM JDH" + i;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket_cmd.send(buf1.getBytes(), buf1.length());
        }*/
    }

    int count;
    Socket_Cmd socket_cmd;

    final Handler mHander = new Handler() {

        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //super.handleMessage(msg);
            String sendInfo = (String) msg.obj;
            info_tv.append(sendInfo);
            Log.d(TAG, "client.send()=");
        }
    };

    public static final String bm = "gbk";
    public static final String bm1 = "utf-8";

    @Override
    public void update(Observable o, Object arg) {
        DatagramPacket dPacket = arg instanceof DatagramPacket ? ((DatagramPacket) arg) : null;
        if (dPacket != null) {
            String res = null;
            try {
                res = new String(dPacket.getData(), dPacket.getOffset(),
                        dPacket.getLength(), bm);
                String msg1 = res
                        + "dPacket.getLength()="
                        + dPacket.getLength()
                        ;
                sendMsg(res + "\n");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    class myThread1 implements Runnable {

        private String threadName;

        public myThread1(String name) {
            this.threadName = name;
        }

        public void run() {
            Log.d(TAG, "MyThread  execu" + msg_et.getText().toString());
            String buf1 = msg_et.getText().toString();
            socket_cmd.send(buf1);
//            client.send(buf1);
            Log.d(TAG, "client.send()=");
        }
    }

    private void sendMsg(String sendInfo) {
        Message msg = mHander.obtainMessage();
        msg.arg1 = 1;
        msg.obj = sendInfo;
        mHander.sendMessage(msg);
    }
}

