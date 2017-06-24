package szz.com.thirldworldv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Observable;

/**
 * @author jdh
 * @file Socket_Cmd.java
 * @brief 信令端口类
 * @date 2016/7/9
 * @update 2016/7/10
 */

public class Socket_Cmd extends Observable implements Runnable {

    //常量  
    //服务器IP  
    private final String SERVER_IP = "183.60.204.64";
    //语音端口  
    private final int SERVER_PORT_CMD = 9103;
    //本地端口  
    private final int LOCAL_PORT_CMD = 9103;

    //网络相关变量  
    //最大帧长  
    private static final int MAX_DATA_PACKET_LENGTH = 1024;

    //端口  
    Socket Tcp_Socket;
    //输入流  
    InputStream Buffer_Receive_Stream;
    //网络连接标志  
    private boolean Flag_Connect = false;
    //接收数据缓存  
    private byte[] Buffer_Receive = new byte[MAX_DATA_PACKET_LENGTH];
    //接收数据包  
    private DatagramPacket Packet_Receive;

    /**
     * @brief 构造函数
     */
    public Socket_Cmd() {
        //接收包  
        Packet_Receive = new DatagramPacket(Buffer_Receive, MAX_DATA_PACKET_LENGTH);
    }

    /**
     * @brief 发送数据
     */
    public synchronized void send(String data) {
        //判断是否连接服务器  
        if (!Flag_Connect) {
            return;
        }

        //发送  
        Thread_Send thread_send = new Thread_Send(data);
        new Thread(thread_send).start();
    }

    @Override
    public void run() {
        //连接服务器  
        //端口  
        try {
            Tcp_Socket = new Socket(SERVER_IP, SERVER_PORT_CMD);
            Buffer_Receive_Stream = Tcp_Socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //开启监听线程  
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        //判断当前是否连接  
                        if (!Flag_Connect) {
                            continue;
                        }

                        //监听  
                        try {
                            if (Buffer_Receive_Stream.available() > 0) {
                                Packet_Receive.setLength(Buffer_Receive_Stream.read(Packet_Receive.getData()));
//                                System.out.println(new String(Packet_Receive.getData(),0,Packet_Receive.getLength()));  
                                //通知观察者  
                                setChanged();
                                notifyObservers(Packet_Receive);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //检测连接状态  
        while (true) {
            Flag_Connect = Tcp_Socket.isConnected();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("连接状态:" + Flag_Connect);  
            if (!Flag_Connect) {
                SocketAddress address = new InetSocketAddress(SERVER_IP, SERVER_PORT_CMD);
                try {
                    Tcp_Socket.connect(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @brief 发送线程
     */
    private class Thread_Send implements Runnable {
        //输出流  
        OutputStream strSend;
        //发送数据缓存
        byte[] data;
        /**
         */
        public Thread_Send(String data) {
            try {
                //输出流
                this.data = data.getBytes("gbk");
                strSend = Tcp_Socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                /*Buffer_Send.write(Buf_Send, 0, Len);
                Buffer_Send.flush();*/
                strSend.write(data);
                strSend.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}  