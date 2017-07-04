package szz.com.baselib.rest;

import android.text.TextUtils;
import android.util.Log;

import szz.com.baselib.application.ContextHolder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import szz.com.baselib.EventUtil;
import szz.com.baselib.R;
import szz.com.baselib.entity.events.ConnectState;
import szz.com.baselib.entity.events.GetServerPack;


/**
 * @author jdh
 * @file ServerSocket.java
 * @brief 信令端口类
 * @date 2016/7/9
 * @update 2016/7/10
 */

public class ServerSocket implements Runnable {

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
    private static final int MAX_DATA_PACKET_LENGTH = 1024 * 5;
    private ExecutorService mSendExecutor;

    //端口
    private Socket mTcpSocket;
    private boolean flagOpen = true;
    //输入流  
    private InputStream mInputStream;
    //输出流
    private OutputStream strSend;

    //网络连接标志
    private volatile boolean mFlagConnect = false;
    //接收数据缓存  
    private byte[] mBufferReceive = new byte[MAX_DATA_PACKET_LENGTH];
    //接收数据包  
    private DatagramPacket dPacket;
    private volatile boolean isSending;
    private volatile LinkedList<String> pendingCmd = new LinkedList<>();
    //当前发送的数据
    private String mCurCmd;
    private HeartBeatThread mHeartBeatThread;

    /**
     * @brief 构造函数
     */
    public ServerSocket() {
        mSendExecutor = Executors.newSingleThreadExecutor();
        //接收包  
        dPacket = new DatagramPacket(mBufferReceive, MAX_DATA_PACKET_LENGTH);
    }

    /**
     * @brief 发送数据
     */
    public synchronized void addAndSend(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        pendingCmd.addLast(data);
        //发送
        send();
    }

    public void send() {
        if (isSending || pendingCmd.isEmpty()) {
            return;
        }
        SendThread sendThread = new SendThread();
        mSendExecutor.execute(sendThread);
    }

    @Override
    public void run() {
        //连接服务器  
        //端口  
        try {
            mTcpSocket = new Socket(SERVER_IP, SERVER_PORT_CMD);
            InetAddress localAddress = mTcpSocket.getLocalAddress();
            int localPort = mTcpSocket.getLocalPort();
            log("本地地址：" + localAddress + ":" + localPort);
            mInputStream = mTcpSocket.getInputStream();
            strSend = mTcpSocket.getOutputStream();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (flagOpen) {
                        try {
                            int read = mInputStream.read(dPacket.getData());
                            dPacket.setLength(read);
                            //通知观察者
                            通知观察者(ServerSocket.this.dPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //检测连接状态
        while (flagOpen) {
            log("检测连接状态:" + mFlagConnect);
            postConnectState();
            try {
                sendHeartBeat();
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!mFlagConnect) {
                SocketAddress address = new InetSocketAddress(SERVER_IP, SERVER_PORT_CMD);
                try {
                    mTcpSocket.connect(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendHeartBeat() {
        if (mHeartBeatThread == null) {
            mHeartBeatThread = new HeartBeatThread();
        }
        mSendExecutor.execute(mHeartBeatThread);
    }

    public void postConnectState() {
        EventUtil.post(new ConnectState(mFlagConnect));
    }

    private void 通知观察者(DatagramPacket dPacket) {
        try {
            String msg = new String(dPacket.getData(), 0, dPacket.getLength(), bm);
            log("服务器返回：" + msg);
            if (msg.startsWith("签到〓")) {
                setConnectState(true);
            } else {
                EventUtil.post(new GetServerPack(msg, mCurCmd));
                isSending = false;
                send();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setConnectState(boolean connect) {
        if (!mFlagConnect && connect) {
            send();
        }
        if (mFlagConnect ^ connect) {
            mFlagConnect = connect;
            postConnectState();
        }
    }

    public boolean isConnected() {
        return mFlagConnect;
    }

    public void release() {
        flagOpen = false;
        mSendExecutor.shutdown();
        mSendExecutor = null;
        safeClose(mInputStream);
        safeClose(mTcpSocket);
    }

    /**
     * @brief 发送线程
     */
    private class SendThread implements Runnable {

        /**
         */
        public SendThread() {
        }

        @Override
        public void run() {
            try {
                isSending = true;
                log("准备发送:" + mCurCmd);
                mCurCmd = pendingCmd.removeFirst();
                strSend.write(mCurCmd.getBytes(bm));
                strSend.flush();
                String arg = "发送：'" + mCurCmd + "'到服务器完成~";
                log(arg);
            } catch (Exception e) {
                pendingCmd.addFirst(mCurCmd);
                e.printStackTrace();
                setConnectState(false);
            } finally {
            }
        }
    }

    /**
     * @brief 发送线程
     */
    private class HeartBeatThread implements Runnable {

        /**
         */
        public HeartBeatThread() {
        }

        @Override
        public void run() {
            try {
                log("准备发送心跳包");
                strSend.write(ContextHolder.getString(R.string.heart_beat).getBytes(bm));
                strSend.flush();
                log("心跳包发送完成~");
            } catch (Exception e) {
                e.printStackTrace();
                setConnectState(false);
            } finally {
            }
        }
    }

    private void safeClose(Closeable strSend) {
        if (strSend != null) {
            try {
                strSend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String TAG = "ServerSocket";

    private void log(String arg) {
        Log.e(TAG, arg);
    }
}