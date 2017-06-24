package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Observable;

/**
 * @author jdh
 * @file ServerSocket.java
 * @brief 信令端口类
 * @date 2016/7/9
 * @update 2016/7/10
 */

public class ServerSocket extends Observable implements Runnable {

    public static final String bm = "gbk";
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
    private DatagramPacket dPacket;

    /**
     * @brief 构造函数
     */
    public ServerSocket() {
        //接收包  
        dPacket = new DatagramPacket(Buffer_Receive, MAX_DATA_PACKET_LENGTH);
    }

    /**
     * @brief 发送数据
     */
    public synchronized void send(String data) {

        //判断是否连接服务器
        if (!Flag_Connect) {
            return;
        }

        log("准备发送:"+data);
        //发送
        Thread_Send thread_send = new Thread_Send(data);
        new Thread(thread_send).start();
    }

    byte[] buf;
    @Override
    public void run() {
        //连接服务器  
        //端口  
        try {
            Tcp_Socket = new Socket(SERVER_IP, SERVER_PORT_CMD);
            InetAddress localAddress = Tcp_Socket.getLocalAddress();
            int localPort = Tcp_Socket.getLocalPort();
            log("本地地址："+localAddress +":"+localPort);
            Buffer_Receive_Stream = Tcp_Socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*final SocketChannel channel = Tcp_Socket.getChannel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                buf = new byte[MAX_DATA_PACKET_LENGTH];
                while (true){

                    try {
                        int len = channel.read(ByteBuffer.wrap(buf));
                        log(new String(buf, 0, len));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
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
                                int read = Buffer_Receive_Stream.read(dPacket.getData());
                                dPacket.setLength(read);
//                                System.out.println(new String(Packet_Receive.getData(),0,Packet_Receive.getLength()));  
                                //通知观察者  
                                setChanged();
                                notifyObservers(dPacket);
                                log("服务器返回：" + new String(dPacket.getData(), dPacket.getOffset(),
                                        dPacket.getLength(), bm));
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
            log("检测连接状态:"+Flag_Connect);
            try {
                Thread.sleep(10000);
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
//        BufferedWriter strSend;
        OutputStream strSend;
        //发送数据缓存
        byte[] data;

        /**
         */
        public Thread_Send(String data) {
            try {
                this.data = data.getBytes(bm);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //输出流
            try {
                strSend = Tcp_Socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                try {
                    strSend.write(data);
                } catch (IOException e) {
                    strSend = Tcp_Socket.getOutputStream();
                    strSend.write(data);
                }
                strSend.flush();
                //通知观察者
                setChanged();
                String arg = "发送：'" + new String(data,bm) + "'到服务器完成~";
                log(arg);
                notifyObservers(arg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void log(String arg) {
        System.err.println(arg);
    }

}  