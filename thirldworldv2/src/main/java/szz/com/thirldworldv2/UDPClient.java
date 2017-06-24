package szz.com.thirldworldv2;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

    //服务器端口
    private static final int SERVER_PORT = 9103;
    public static final String SERVER_IP = "183.60.204.64";
    //本地端口
    private final int LOCAL_PORT = 9103;
    private DatagramSocket dSocket = null;

    InetAddress serverIp = null;

    public UDPClient(Handler hander) {
        super();
        mHander = hander;
        initServer();
        try {
            dSocket = new DatagramSocket(LOCAL_PORT); // 注意此处要先在配置文件里设置权限,否则会抛权限不足的异常
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dSocket.connect(serverIp, SERVER_PORT);
                    sendMsg("正在连接服务器(" + serverIp + ":" + SERVER_PORT + ")...\n");
                    receive();
                }
            }).start();
        } catch (SocketException e) {
            e.printStackTrace();
            sendMsg("服务器连接失败...\n" + e.getMessage());
        }
    }

    private void initServer() {
        if (serverIp == null) {
            try {
                serverIp = InetAddress.getByName(SERVER_IP); // 本机测试
                sendMsg("已找到服务器(" + serverIp + "),连接中...\n");
            } catch (UnknownHostException e) {
                sendMsg("未找到服务器...\n" + e.getMessage());
                e.printStackTrace();
                serverIp = null;
            }
        }
    }


    DatagramPacket dPacket;

    public void send(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        initServer();
        StringBuilder sb = new StringBuilder();
        int msg_len = msg.length();
        dPacket = new DatagramPacket(msg.getBytes(), msg_len,
                serverIp, SERVER_PORT);
        try {
            dSocket.send(dPacket);
            Log.d("tian", "msg==" + msg + "dpackage=" + dPacket.getData() + "dPacket.leng=" + dPacket.getLength());
            sb.append("消息: " + msg + " 发送成功!").append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            sb.append("消息发送失败.").append("\n");
        }
        sendMsg(sb.toString());
    }

    private boolean life = true;
    Handler mHander;

    public void receive() {

        byte[] msg = new byte[2048];
        DatagramPacket dPacket;
        while (life) {
            try {
                dPacket = new DatagramPacket(msg, msg.length);
                sendMsg("等待服务器数据\n");
                dSocket.receive(dPacket);
                String msg1 = new String(dPacket.getData(), dPacket.getOffset(),
                        dPacket.getLength())
                        + "dPacket.getLength()="
                        + dPacket.getLength();
                Log.d("tian msg sever received",
                        msg1);
                sendMsg(msg1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMsg(String sendInfo) {
        Message msg = mHander.obtainMessage();
        msg.arg1 = 1;
        msg.obj = sendInfo;
        mHander.sendMessage(msg);
    }
}