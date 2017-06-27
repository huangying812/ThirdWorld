package com.example;


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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author jdh
 * @file ServerSocket.java
 * @brief 信令端口类
 * @date 2016/7/9
 * @update 2016/7/10
 */

public class QuanDi implements Runnable {

    public static final String bm = "gbk";
    //盟军读取个人圈地运动〓hy814〓123\t183.60.204.64\t183.60.204.64
    //盟军圈地运动本次开通〓hy814〓123\t183.60.204.64\t183.60.204.64
    //盟军圈地读取最新服务器数据\t183.60.204.64\t183.60.204.64
    //盟军圈地掷骰子〓hy814〓123\t183.60.204.64\t183.60.204.64
    //盟军圈地开始进行建造升级〓hy814〓123〓10005\t183.60.204.64\t183.60.204.64
    public static String cmdReadS = "盟军圈地读取最新服务器数据\t183.60.204.64\t183.60.204.64";
    public static String cmdReadP = "盟军读取个人圈地运动〓%1$s〓%2$s\t183.60.204.64\t183.60.204.64";
    public static String cmdEnable = "盟军圈地运动本次开通〓%1$s〓%2$s\t183.60.204.64\t183.60.204.64";
    public static String cmdZhiSZ = "盟军圈地掷骰子〓%1$s〓%2$s\t183.60.204.64\t183.60.204.64";
    public static String cmdZhiZD = "盟军圈地掷骰子指定〓%1$s〓%2$s〓%d\t183.60.204.64\t183.60.204.64";
    public static String cmdBuild = "盟军圈地开始进行建造升级〓%1$s〓%2$s〓%d\t183.60.204.64\t183.60.204.64";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
    private static final SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
    //常量
    //服务器IP  
    private final String SERVER_IP = "183.60.204.64";
    //语音端口  
    private final int SERVER_PORT_CMD = 9103;
    //本地端口  
    private final int LOCAL_PORT_CMD = 9103;

    //网络相关变量  
    //最大帧长  
    private static final int MAX_DATA_PACKET_LENGTH = 1024 * 50;
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
    private String mAccount;
    private int mCurMapZuobiao;
    private int mCurMapIndex;
    private int mId;
    private int mZijin;
    private Date mTimeCanOper;
    private Date mTimeCur;
    private int mTimes;
    private int mMaxBuild;

    public static void main(String... args) {
//        log(getDate("2017-06-26 19:28:09").toString());
        QuanDi quanDi = new QuanDi();
        quanDi.account = "hy815";
        quanDi.pwd = "123";
        new Thread(quanDi).start();
    }

    public static Date getDate(String strDate) {
        if (strDate != null && !strDate.isEmpty()) {
            try {
                return sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Date(0);
    }

    public static Date getDate2(String strDate) {
        if (strDate != null && !strDate.isEmpty()) {
            try {
                return sdf2.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Date();
    }

    /*public static String getFormatedDateYMDHMChinese(long utcTime) {
        return getFormatedDate(utcTime, sdf, "");
    }

    public static String getFormatedDate(long utcTime, String format, String defaultString) {

        return new SimpleDateFormat(format).format(new Date(utcTime));
    }*/

    /**
     * @brief 构造函数
     */
    public QuanDi() {
        mSendExecutor = Executors.newSingleThreadExecutor();
        //接收包  
        dPacket = new DatagramPacket(mBufferReceive, MAX_DATA_PACKET_LENGTH);
    }

    /**
     * @brief 发送数据
     */
    public synchronized void addAndSend(String data) {
        if (data == null || data.isEmpty()) {
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
                    if (flagOpen) {
                        try {
                            int read = mInputStream.read(dPacket.getData());
                            dPacket.setLength(read);
                            //通知观察者
                            通知观察者(QuanDi.this.dPacket);
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

    private void 通知观察者(DatagramPacket dPacket) {
        try {
            String msg = new String(dPacket.getData(), 0, dPacket.getLength(), bm);
            log("服务器返回：" + msg.substring(0, Math.min(msg.length(), 100)));
            if (msg.startsWith("签到〓")) {
                setConnectState(true);
                setCmd(cmdReadP);
            } else {
                processResponse(msg);
                isSending = false;
                send();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    //盟军读取个人圈地运动〓hy814〓123\t183.60.204.64\t183.60.204.64
//    帮派读取圈地副本返回〓hy814〓0〓47〓0〓〓2017年6月26日19时22分9秒〓240〓7

    //盟军圈地运动本次开通〓hy814〓123\t183.60.204.64\t183.60.204.64
//    帮派圈地开通本周成功〓hy814〓214.1214

    //盟军圈地读取最新服务器数据\t183.60.204.64\t183.60.204.64
//    盟军圈地读最新返回〓战神台 80 76 3 23 3 25 2；铜人殿 20 0 0 0 0 0 0；镜雪原 30 0 0 0 0 0 0；弄梅洞窟 80 12 3 15 3 18 1；北海龙宫 70 0 0 0 0 0 0；海底皇宫 70 0 0 0 0 0 0；落雁谷 20 0 0 0 0 0 0；吊烤地狱 50 0 0 0 0 0 0；最后的房间 70 0 0 0 0 0 0；衡天台 90 91 1 158 1 5 2；巨菇洞 20 0 0 0 0 0 0；西天界 90 37 1 38 1 10 2；绿洲东郊 20 0 0 0 0 0 0；仙界天网 90 3 3 21 1 198 2；宁静海 70 0 0 0 0 0 0；万劫谷 80 157 2 69 4 158 2；剥皮地狱 50 0 0 0 0 0 0；名剑山庄正院 60 0 0 0 0 0 0；炮烙地狱 50 0 0 0 0 0 0；紫阳门 90 91 3 14 2 38 2；太和殿 30 0 0 0 0 0 0；守望林场 40 0 0 0 0 0 0；小小房间 70 0 0 0 0 0 0；冰清桥 30 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；铡头地狱 50 0 0 0 0 0 0；赏月谷 20 0 0 0 0 0 0；飞升门 90 23 1 3 3 49 2；沉信当铺 70 0 0 0 0 0 0；闲情园 10 0 0 0 0 0 0；逐影洞窟 80 16 1 115 2 40 2；暴走地狱 50 0 0 0 0 0 0；雪融谷 30 0 0 0 0 0 0；鸟鸣谷 30 0 0 0 0 0 0；铁索桥 50 0 0 0 0 0 0；迷幻洞窟 80 12 2 21 1 9 2；污秽地狱 50 0 0 0 0 0 0；神将壁 80 16 2 40 1 157 3；松竹林 10 0 0 0 0 0 0；皇城甬道 80 74 2 5 2 18 1；空灵园 90 10 4 22 2 49 2；流沙原 20 0 0 0 0 0 0；枯井原 10 0 0 0 0 0 0；邂逅洞窟 80 198 4 9 2 76 3；灵丹室 90 105 3 14 1 15 3；碧水丹心谷 40 0 0 0 0 0 0；磨骨地狱 50 0 0 0 0 0 0；羁马谷 20 0 0 0 0 0 0；双印谷 40 0 0 0 0 0 0；深深海 70 0 0 0 0 0 0；旋割地狱 50 0 0 0 0 0 0；千佛殿 60 0 0 0 0 0 0；冰封地狱 50 0 0 0 0 0 0；独木原 30 0 0 0 0 0 0；观日桥 10 0 0 0 0 0 0；梦佛窟 20 0 0 0 0 0 0；吓死人洞窟1 10 0 0 0 0 0 0；逆之往事 40 0 0 0 0 0 0；银罐谷 20 0 0 0 0 0 0；名剑山庄前厅 60 0 0 0 0 0 0；浮影水城外郊 10 0 0 0 0 0 0；南海龙宫 70 0 0 0 0 0 0；宁静客栈 70 0 0 0 0 0 0；天灯地狱 50 0 0 0 0 0 0；仙临台 90 22 1 114 2 6 1；酒鬼冢 20 0 0 0 0 0 0；执手洞窟 80 4 1 105 1 25 2；石笋洞窟 80 69 2 50 1 0 0；怨灵洞口 20 0 0 0 0 0 0；炉烧地狱 50 0 0 0 0 0 0；古城遗址 20 0 0 0 0 0 0；西码头 10 0 0 0 0 0 0；猎人洞窟 10 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；紫禁城 30 0 0 0 0 0 0；油锅地狱 50 0 0 0 0 0 0；圣泉台 90 115 3 163 2 0 0；黄泥岗 30 0 0 0 0 0 0；御风台 90 50 1 0 0 0 0；大殿 30 0 0 0 0 0 0；三清殿 60 0 0 0 0 0 0；怡心谷 80 4 2 74 1 114 3；守候洞窟 80 163 2 0 0 0 0；华山顶 60 0 0 0 0 0 0；很窄的过道 70 0 0 0 0 0 0；明月桥 10 0 0 0 0 0 0；东码头 10 0 0 0 0 0 0；盘龙洞窟 80 0 0 0 0 0 0；晚枫林 40 0 0 0 0 0 0；遗恨原 20 0 0 0 0 0 0；凰之谷 40 0 0 0 0 0 0；翔云幻城 90 6 1 54 3 0 0；钉板地狱 50 0 0 0 0 0 0；通仙之境 90 37 2 19 1 54 1；寒月台 10 0 0 0 0 0 0；武当练功房 60 0 0 0 0 0 0；净沙原 20 0 0 0 0 0 0；四桥原 10 0 0 0 0 0 0；紫宵宫 60 0 0 0 0 0 0；地焰洞 40 0 0 0 0 0 0；西海龙宫 70 0 0 0 0 0 0；青之绿洲 20 0 0 0 0 0 0；碧水洞窟 80 0 0 0 0 0 0；蓬莱仙境 90 51 1 0 0 0 0；深蓝海底 70 0 0 0 0 0 0；华山亭 60 0 0 0 0 0 0；斧青谷 30 0 0 0 0 0 0；仙客厅 90 0 0 0 0 0 0；春水桥 30 0 0 0 0 0 0；华山瀑布 60 0 0 0 0 0 0；金钟洞窟 80 0 0 0 0 0 0；古铜洞窟 80 0 0 0 0 0 0；雪狼湖 70 0 0 0 0 0 0；树顶 10 0 0 0 0 0 0；石壁洞口 10 0 0 0 0 0 0；逆之轮回 40 0 0 0 0 0 0；青灵洞 20 0 0 0 0 0 0；紫苔洞窟 80 51 1 0 0 0 0；名剑山庄中院 60 0 0 0 0 0 0；观日桥头 10 0 0 0 0 0 0；徘徊林 40 0 0 0 0 0 0；少林寺前院 60 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；深蓝海城 70 0 0 0 0 0 0；青风原 10 0 0 0 0 0 0；东海龙宫 70 0 0 0 0 0 0；雪狼崖 70 0 0 0 0 0 0；百草池 40 0 0 0 0 0 0；榕盘谷 40 0 0 0 0 0 0；华山脚 60 0 0 0 0 0 0；敕勒川 30 0 0 0 0 0 0；不悔洞窟 80 0 0 0 0 0 0；沙盗洞口 20 0 0 0 0 0 0；坠泪岗 10 0 0 0 0 0 0；飘渺仙境 90 0 0 0 0 0 0；一点点杂货 70 0 0 0 0 0 0；松雪野 30 0 0 0 0 0 0；名剑山庄书房 60 0 0 0 0 0 0；炊烟谷 30 0 0 0 0 0 0；情人谷 20 0 0 0 0 0 0；阳光渔栈 40 0 0 0 0 0 0；窄窄的过道 70 0 0 0 0 0 0；藏经阁 60 0 0 0 0 0 0；伤心洞窟 80 19 2 0 0 0 0；剑池谷 10 0 0 0 0 0 0；天王殿 60 0 0 0 0 0 0；金轮谷 20 0 0 0 0 0 0

    //盟军圈地掷骰子〓hy814〓123\t183.60.204.64\t183.60.204.64
//    盟军圈地掷骰子成功返回〓10005〓5〓2017-06-26 19:28:09〓243〓0〓0〓5000〓5〓0〓无〓战神台 80 76 3 23 3 25 2；铜人殿 20 0 0 0 0 0 0；镜雪原 30 0 0 0 0 0 0；弄梅洞窟 80 12 3 15 3 18 1；北海龙宫 70 0 0 0 0 0 0；海底皇宫 70 0 0 0 0 0 0；落雁谷 20 0 0 0 0 0 0；吊烤地狱 50 0 0 0 0 0 0；最后的房间 70 0 0 0 0 0 0；衡天台 90 91 1 158 1 5 2；巨菇洞 20 0 0 0 0 0 0；西天界 90 37 1 38 1 10 2；绿洲东郊 20 0 0 0 0 0 0；仙界天网 90 3 3 21 1 198 2；宁静海 70 0 0 0 0 0 0；万劫谷 80 157 2 69 4 158 2；剥皮地狱 50 0 0 0 0 0 0；名剑山庄正院 60 0 0 0 0 0 0；炮烙地狱 50 0 0 0 0 0 0；紫阳门 90 91 3 14 2 38 2；太和殿 30 0 0 0 0 0 0；守望林场 40 0 0 0 0 0 0；小小房间 70 0 0 0 0 0 0；冰清桥 30 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；铡头地狱 50 0 0 0 0 0 0；赏月谷 20 0 0 0 0 0 0；飞升门 90 23 1 3 3 49 2；沉信当铺 70 0 0 0 0 0 0；闲情园 10 0 0 0 0 0 0；逐影洞窟 80 16 1 115 2 40 2；暴走地狱 50 0 0 0 0 0 0；雪融谷 30 0 0 0 0 0 0；鸟鸣谷 30 0 0 0 0 0 0；铁索桥 50 0 0 0 0 0 0；迷幻洞窟 80 12 2 21 1 9 2；污秽地狱 50 0 0 0 0 0 0；神将壁 80 16 2 40 1 157 3；松竹林 10 0 0 0 0 0 0；皇城甬道 80 74 2 5 2 18 1；空灵园 90 10 4 22 2 49 2；流沙原 20 0 0 0 0 0 0；枯井原 10 0 0 0 0 0 0；邂逅洞窟 80 198 4 9 2 76 3；灵丹室 90 105 3 14 1 15 3；碧水丹心谷 40 0 0 0 0 0 0；磨骨地狱 50 0 0 0 0 0 0；羁马谷 20 0 0 0 0 0 0；双印谷 40 0 0 0 0 0 0；深深海 70 0 0 0 0 0 0；旋割地狱 50 0 0 0 0 0 0；千佛殿 60 0 0 0 0 0 0；冰封地狱 50 0 0 0 0 0 0；独木原 30 0 0 0 0 0 0；观日桥 10 0 0 0 0 0 0；梦佛窟 20 0 0 0 0 0 0；吓死人洞窟1 10 0 0 0 0 0 0；逆之往事 40 0 0 0 0 0 0；银罐谷 20 0 0 0 0 0 0；名剑山庄前厅 60 0 0 0 0 0 0；浮影水城外郊 10 0 0 0 0 0 0；南海龙宫 70 0 0 0 0 0 0；宁静客栈 70 0 0 0 0 0 0；天灯地狱 50 0 0 0 0 0 0；仙临台 90 22 1 114 2 6 1；酒鬼冢 20 0 0 0 0 0 0；执手洞窟 80 4 1 105 1 25 2；石笋洞窟 80 69 2 50 1 0 0；怨灵洞口 20 0 0 0 0 0 0；炉烧地狱 50 0 0 0 0 0 0；古城遗址 20 0 0 0 0 0 0；西码头 10 0 0 0 0 0 0；猎人洞窟 10 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；紫禁城 30 0 0 0 0 0 0；油锅地狱 50 0 0 0 0 0 0；圣泉台 90 115 3 163 2 0 0；黄泥岗 30 0 0 0 0 0 0；御风台 90 50 1 0 0 0 0；大殿 30 0 0 0 0 0 0；三清殿 60 0 0 0 0 0 0；怡心谷 80 4 2 74 1 114 3；守候洞窟 80 163 2 0 0 0 0；华山顶 60 0 0 0 0 0 0；很窄的过道 70 0 0 0 0 0 0；明月桥 10 0 0 0 0 0 0；东码头 10 0 0 0 0 0 0；盘龙洞窟 80 0 0 0 0 0 0；晚枫林 40 0 0 0 0 0 0；遗恨原 20 0 0 0 0 0 0；凰之谷 40 0 0 0 0 0 0；翔云幻城 90 6 1 54 3 0 0；钉板地狱 50 0 0 0 0 0 0；通仙之境 90 37 2 19 1 54 1；寒月台 10 0 0 0 0 0 0；武当练功房 60 0 0 0 0 0 0；净沙原 20 0 0 0 0 0 0；四桥原 10 0 0 0 0 0 0；紫宵宫 60 0 0 0 0 0 0；地焰洞 40 0 0 0 0 0 0；西海龙宫 70 0 0 0 0 0 0；青之绿洲 20 0 0 0 0 0 0；碧水洞窟 80 0 0 0 0 0 0；蓬莱仙境 90 51 1 0 0 0 0；深蓝海底 70 0 0 0 0 0 0；华山亭 60 0 0 0 0 0 0；斧青谷 30 0 0 0 0 0 0；仙客厅 90 0 0 0 0 0 0；春水桥 30 0 0 0 0 0 0；华山瀑布 60 0 0 0 0 0 0；金钟洞窟 80 0 0 0 0 0 0；古铜洞窟 80 0 0 0 0 0 0；雪狼湖 70 0 0 0 0 0 0；树顶 10 0 0 0 0 0 0；石壁洞口 10 0 0 0 0 0 0；逆之轮回 40 0 0 0 0 0 0；青灵洞 20 0 0 0 0 0 0；紫苔洞窟 80 51 1 0 0 0 0；名剑山庄中院 60 0 0 0 0 0 0；观日桥头 10 0 0 0 0 0 0；徘徊林 40 0 0 0 0 0 0；少林寺前院 60 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；深蓝海城 70 0 0 0 0 0 0；青风原 10 0 0 0 0 0 0；东海龙宫 70 0 0 0 0 0 0；雪狼崖 70 0 0 0 0 0 0；百草池 40 0 0 0 0 0 0；榕盘谷 40 0 0 0 0 0 0；华山脚 60 0 0 0 0 0 0；敕勒川 30 0 0 0 0 0 0；不悔洞窟 80 0 0 0 0 0 0；沙盗洞口 20 0 0 0 0 0 0；坠泪岗 10 0 0 0 0 0 0；飘渺仙境 90 0 0 0 0 0 0；一点点杂货 70 0 0 0 0 0 0；松雪野 30 0 0 0 0 0 0；名剑山庄书房 60 0 0 0 0 0 0；炊烟谷 30 0 0 0 0 0 0；情人谷 20 0 0 0 0 0 0；阳光渔栈 40 0 0 0 0 0 0；窄窄的过道 70 0 0 0 0 0 0；藏经阁 60 0 0 0 0 0 0；伤心洞窟 80 19 2 0 0 0 0；剑池谷 10 0 0 0 0 0 0；天王殿 60 0 0 0 0 0 0；金轮谷 20 0 0 0 0 0 0

    //盟军圈地开始进行建造升级〓hy814〓123〓10005\t183.60.204.64\t183.60.204.64
//    盟军圈地建造升级返回〓20005〓4930〓北海龙宫 70 47 1 0 0 0 0〓70

    ArrayList<Map> mMaps = new ArrayList<>();

    public void processResponse(String res) {
        String[] split = res.split("〓");
        if (split.length > 0) {
            switch (split[0]) {
                case "帮派读取圈地副本返回":
//帮派读取圈地副本返回〓hy814〓0〓4〓1000〓〓2017年5月22日9时25分44秒〓120〓7
//帮派读取圈地副本返回〓hy814〓0〓47〓0〓〓2017年6月26日19时22分9秒〓240〓7
//帮派读取圈地副本返回〓hy814〓20005〓47〓4930〓2017-06-26 19:28:09〓2017年6月26日19时33分27秒〓243〓
//帮派读取圈地副本返回〓hy814〓10091〓4〓7320〓2017-06-26 19:02:12〓2017年6月26日20时51分43秒〓26〓7
//帮派读取圈地副本返回〓hy812〓10025〓78〓2721〓2017-06-27 11:45:52〓2017年6月27日10时20分15秒〓137〓7
                    //账号
                    mAccount = split[1];
                    //最新坐标
                    最新坐标(split[2]);
                    //用户id
                    mId = str2Int(split[3]);
                    //圈地资金
                    mZijin = str2Int(split[4]);
                    //下次可操作时间
                    mTimeCanOper = getDate(split[5]);
                    //当前时间
                    mTimeCur = getDate2(split[6]);
                    //机动力
                    mTimes = str2Int(split[7]);
                    //本周个人最高可占领地皮数
                    mMaxBuild = str2Int(split[8]);
                    log("最新坐标:" + mCurMapZuobiao
                        + "-用户id：" + mId
                        + "-下次可操作时间：" + mTimeCanOper.toString()
                        + "-当前时间：" + mTimeCur.toString() + dateFm.format(mTimeCur)
                        + "-机动力：" + mTimes
                        + "-资金：" + mZijin
                        + "-本周个人最高可占领地皮数：" + mMaxBuild
                    );
                    if (mZijin <= 0) {
                        setCmd(cmdEnable);
                    } else {
                        sendCmd(cmdReadS);
                    }
                    break;
                case "盟军圈地读最新返回":
//盟军圈地读最新返回〓战神台 80 76 3 23 3 25 2；铜人殿 20 0 0 0 0 0 0；镜雪原 30 0 0 0 0 0 0；弄梅洞窟 80 12 3 15 3 18 1；北海龙宫 70 0 0 0 0 0 0；海底皇宫 70 0 0 0 0 0 0；落雁谷 20 0 0 0 0 0 0；吊烤地狱 50 0 0 0 0 0 0；最后的房间 70 0 0 0 0 0 0；衡天台 90 91 1 158 1 5 2；巨菇洞 20 0 0 0 0 0 0；西天界 90 37 1 38 1 10 2；绿洲东郊 20 0 0 0 0 0 0；仙界天网 90 3 3 21 1 198 2；宁静海 70 0 0 0 0 0 0；万劫谷 80 157 2 69 4 158 2；剥皮地狱 50 0 0 0 0 0 0；名剑山庄正院 60 0 0 0 0 0 0；炮烙地狱 50 0 0 0 0 0 0；紫阳门 90 91 3 14 2 38 2；太和殿 30 0 0 0 0 0 0；守望林场 40 0 0 0 0 0 0；小小房间 70 0 0 0 0 0 0；冰清桥 30 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；铡头地狱 50 0 0 0 0 0 0；赏月谷 20 0 0 0 0 0 0；飞升门 90 23 1 3 3 49 2；沉信当铺 70 0 0 0 0 0 0；闲情园 10 0 0 0 0 0 0；逐影洞窟 80 16 1 115 2 40 2；暴走地狱 50 0 0 0 0 0 0；雪融谷 30 0 0 0 0 0 0；鸟鸣谷 30 0 0 0 0 0 0；铁索桥 50 0 0 0 0 0 0；迷幻洞窟 80 12 2 21 1 9 2；污秽地狱 50 0 0 0 0 0 0；神将壁 80 16 2 40 1 157 3；松竹林 10 0 0 0 0 0 0；皇城甬道 80 74 2 5 2 18 1；空灵园 90 10 4 22 2 49 2；流沙原 20 0 0 0 0 0 0；枯井原 10 0 0 0 0 0 0；邂逅洞窟 80 198 4 9 2 76 3；灵丹室 90 105 3 14 1 15 3；碧水丹心谷 40 0 0 0 0 0 0；磨骨地狱 50 0 0 0 0 0 0；羁马谷 20 0 0 0 0 0 0；双印谷 40 0 0 0 0 0 0；深深海 70 0 0 0 0 0 0；旋割地狱 50 0 0 0 0 0 0；千佛殿 60 0 0 0 0 0 0；冰封地狱 50 0 0 0 0 0 0；独木原 30 0 0 0 0 0 0；观日桥 10 0 0 0 0 0 0；梦佛窟 20 0 0 0 0 0 0；吓死人洞窟1 10 0 0 0 0 0 0；逆之往事 40 0 0 0 0 0 0；银罐谷 20 0 0 0 0 0 0；名剑山庄前厅 60 0 0 0 0 0 0；浮影水城外郊 10 0 0 0 0 0 0；南海龙宫 70 0 0 0 0 0 0；宁静客栈 70 0 0 0 0 0 0；天灯地狱 50 0 0 0 0 0 0；仙临台 90 22 1 114 2 6 1；酒鬼冢 20 0 0 0 0 0 0；执手洞窟 80 4 1 105 1 25 2；石笋洞窟 80 69 2 50 1 0 0；怨灵洞口 20 0 0 0 0 0 0；炉烧地狱 50 0 0 0 0 0 0；古城遗址 20 0 0 0 0 0 0；西码头 10 0 0 0 0 0 0；猎人洞窟 10 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；紫禁城 30 0 0 0 0 0 0；油锅地狱 50 0 0 0 0 0 0；圣泉台 90 115 3 163 2 0 0；黄泥岗 30 0 0 0 0 0 0；御风台 90 50 1 0 0 0 0；大殿 30 0 0 0 0 0 0；三清殿 60 0 0 0 0 0 0；怡心谷 80 4 2 74 1 114 3；守候洞窟 80 163 2 0 0 0 0；华山顶 60 0 0 0 0 0 0；很窄的过道 70 0 0 0 0 0 0；明月桥 10 0 0 0 0 0 0；东码头 10 0 0 0 0 0 0；盘龙洞窟 80 0 0 0 0 0 0；晚枫林 40 0 0 0 0 0 0；遗恨原 20 0 0 0 0 0 0；凰之谷 40 0 0 0 0 0 0；翔云幻城 90 6 1 54 3 0 0；钉板地狱 50 0 0 0 0 0 0；通仙之境 90 37 2 19 1 54 1；寒月台 10 0 0 0 0 0 0；武当练功房 60 0 0 0 0 0 0；净沙原 20 0 0 0 0 0 0；四桥原 10 0 0 0 0 0 0；紫宵宫 60 0 0 0 0 0 0；地焰洞 40 0 0 0 0 0 0；西海龙宫 70 0 0 0 0 0 0；青之绿洲 20 0 0 0 0 0 0；碧水洞窟 80 0 0 0 0 0 0；蓬莱仙境 90 51 1 0 0 0 0；深蓝海底 70 0 0 0 0 0 0；华山亭 60 0 0 0 0 0 0；斧青谷 30 0 0 0 0 0 0；仙客厅 90 0 0 0 0 0 0；春水桥 30 0 0 0 0 0 0；华山瀑布 60 0 0 0 0 0 0；金钟洞窟 80 0 0 0 0 0 0；古铜洞窟 80 0 0 0 0 0 0；雪狼湖 70 0 0 0 0 0 0；树顶 10 0 0 0 0 0 0；石壁洞口 10 0 0 0 0 0 0；逆之轮回 40 0 0 0 0 0 0；青灵洞 20 0 0 0 0 0 0；紫苔洞窟 80 51 1 0 0 0 0；名剑山庄中院 60 0 0 0 0 0 0；观日桥头 10 0 0 0 0 0 0；徘徊林 40 0 0 0 0 0 0；少林寺前院 60 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；深蓝海城 70 0 0 0 0 0 0；青风原 10 0 0 0 0 0 0；东海龙宫 70 0 0 0 0 0 0；雪狼崖 70 0 0 0 0 0 0；百草池 40 0 0 0 0 0 0；榕盘谷 40 0 0 0 0 0 0；华山脚 60 0 0 0 0 0 0；敕勒川 30 0 0 0 0 0 0；不悔洞窟 80 0 0 0 0 0 0；沙盗洞口 20 0 0 0 0 0 0；坠泪岗 10 0 0 0 0 0 0；飘渺仙境 90 0 0 0 0 0 0；一点点杂货 70 0 0 0 0 0 0；松雪野 30 0 0 0 0 0 0；名剑山庄书房 60 0 0 0 0 0 0；炊烟谷 30 0 0 0 0 0 0；情人谷 20 0 0 0 0 0 0；阳光渔栈 40 0 0 0 0 0 0；窄窄的过道 70 0 0 0 0 0 0；藏经阁 60 0 0 0 0 0 0；伤心洞窟 80 19 2 0 0 0 0；剑池谷 10 0 0 0 0 0 0；天王殿 60 0 0 0 0 0 0；金轮谷 20 0 0 0 0 0 0
                    processMapData(split[1]);
//                    build();
                    setCmd(cmdZhiSZ);
//                    zhiDing(9);

                    break;
                case "盟军圈地掷骰子成功返回":
//盟军圈地掷骰子成功返回〓10009〓9〓2017-05-22 09:25:45〓119〓0〓0〓1000〓9〓0〓无〓赏月谷 20 0 0 0 0 0 0；西海龙宫 70 0 0 0 0 0 0；衡天台 90 0 0 0 0 0 0；华山腰道 60 0 0 0 0 0 0；万劫谷 80 0 0 0 0 0 0；猎人洞窟 10 0 0 0 0 0 0；怨灵洞口 20 0 0 0 0 0 0；炮烙地狱 50 0 0 0 0 0 0；弄梅洞窟 80 0 0 0 0 0 0；华山亭 60 0 0 0 0 0 0；油锅地狱 50 0 0 0 0 0 0；春水桥 30 0 0 0 0 0 0；海底皇宫 70 0 0 0 0 0 0；石壁洞口 10 0 0 0 0 0 0；飞升门 90 0 0 0 0 0 0；小小房间 70 0 0 0 0 0 0；金轮谷 20 0 0 0 0 0 0；逐影洞窟 80 0 0 0 0 0 0；圣宴室 90 0 0 0 0 0 0；碧水洞窟 80 0 0 0 0 0 0；战神台 80 0 0 0 0 0 0；钉床地狱 50 0 0 0 0 0 0；寒月台 10 0 0 0 0 0 0；紫宵宫 60 0 0 0 0 0 0；随机事件 9999999 0 0 0 0 0 0；双印谷 40 0 0 0 0 0 0；皇城甬道 80 0 0 0 0 0 0；执手洞窟 80 0 0 0 0 0 0；深深海 70 0 0 0 0 0 0；枯井原 10 0 0 0 0 0 0；深蓝海城 70 0 0 0 0 0 0；灵丹室 90 0 0 0 0 0 0；过道 70 0 0 0 0 0 0；绿幽洞 20 0 0 0 0 0 0；名剑山庄书房 60 0 0 0 0 0 0；沉信当铺 70 0 0 0 0 0 0；铡头地狱 50 0 0 0 0 0 0；逆之心情 40 0 0 0 0 0 0；钉板地狱 50 0 0 0 0 0 0；北海龙宫 70 0 0 0 0 0 0；剥皮地狱 50 0 0 0 0 0 0；犁春原 30 0 0 0 0 0 0；坠泪岗 10 0 0 0 0 0 0；羊肠道 30 0 0 0 0 0 0；空灵园 90 0 0 0 0 0 0；小房间 70 0 0 0 0 0 0；逆之情结 40 0 0 0 0 0 0；寒井台 30 0 0 0 0 0 0；净沙原 20 0 0 0 0 0 0；青风原 10 0 0 0 0 0 0；南海龙宫 70 0 0 0 0 0 0；吓死人洞窟1 10 0 0 0 0 0 0；西天界 90 0 0 0 0 0 0；东海?
//盟军圈地掷骰子成功返回〓10012〓7〓2017-06-26 20:30:25〓254〓0〓36〓4936〓12〓0〓无〓战神台 80 76 3 23 3 25 2；铜人殿
//盟军圈地掷骰子成功返回〓10123〓9〓2017-06-27 08:50:42〓325〓0〓0〓4365〓123〓7〓遇到强盗〓战神台 80 76 3 23 4 25 2；铜人殿 20 0 0 0 0 0
                    //最新坐标
                    最新坐标(split[1]);
                    //点数
                    int dianShu = str2Int(split[2]);
                    //下次可操作时间
                    mTimeCanOper = getDate(split[3]);
                    //机动力
                    mTimes = str2Int(split[4]);
                    //未知
                    int un = str2Int(split[5]);
                    //花费
                    int cost = str2Int(split[6]);
                    //资金
                    mZijin = str2Int(split[7]);
                    //地皮编号
                    int index = str2Int(split[8]);
                    //特殊事件的数值
                    int un2 = str2Int(split[9]);
                    //特殊事件
                    String event = split[10];
                    log("最新坐标:" + mCurMapZuobiao + "-点数：" + dianShu
                            + "-下次可操作时间：" + mTimeCanOper.toString()
                            + "-当前时间：" + new Date().toString()
                            + "-机动力：" + mTimes + "-未知字段：" + un
                            + "-花费：" + cost + "-资金：" + mZijin
                            + "-地皮编号：" + index + "-特殊事件的数值：" + un2
                            + "-特殊事件：" + event
                    );
                    Map map = mMaps.get(index - 1);
                    log(map.toString());
                    processMapData(split[11]);
                    /*if (!map.BuildIfGood()) {
                        try {
                            Thread.sleep(2500);
                            addAndSend(cmdZhiSZ);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    break;
                case "盟军圈地建造升级返回":
//盟军圈地建造升级返回〓20009〓920〓弄梅洞窟 80 4 1 0 0 0 0〓80
                    break;
            }

        }
    }

    public void setCmd(String cmdStrResId) {
        sendCmd(cmdStrResId, account, pwd);
    }

    String account, pwd;

    private void setCmd(String cmdStrResId, Object... args) {
        Object[] strings = new Object[args.length + 2];
        int i = 0;
        strings[i++] = account;
        strings[i++] = pwd;
        for (Object arg : args) {
            strings[i++] = arg;
        }
        sendCmd(cmdStrResId, strings);
    }

    public void sendCmd(String cmdStrResId, Object... args) {
        String curCmd = String.format(cmdStrResId, args);
        addAndSend(curCmd);
    }

    public void 最新坐标(String str) {
        mCurMapZuobiao = str2Int(str);
        mCurMapIndex = mCurMapZuobiao % 1000;
    }

    int all90 = 0;
    int all80 = 0;
    int all70 = 0;
    int all60 = 0;
    int remain90 = 0;
    int remain80 = 0;
    int remain70 = 0;
    int remain60 = 0;
    int remain90Pos = 0;
    int remain80Pos = 0;
    int remain70Pos = 0;
    int remain60Pos = 0;
    ArrayList<Map> myHouses = new ArrayList<>();

    public void processMapData(String mapsStr) {
        String[] maps = mapsStr.split("；");
        mMaps.clear();
        myHouses.clear();
        all90 = 0;
        all80 = 0;
        all70 = 0;
        all60 = 0;
        remain90 = 0;
        remain80 = 0;
        remain70 = 0;
        remain60 = 0;
        remain90Pos = 0;
        remain80Pos = 0;
        remain70Pos = 0;
        remain60Pos = 0;
        int count = 1;
        for (String mapStr : maps) {
            Map m = new Map(count++, mapStr);
            mMaps.add(m);
            if (m.isMyHouse()) {
                myHouses.add(m);
            }
        }
        if (mCurMapIndex > 0) {
            Map map1 = mMaps.get(mCurMapIndex - 1);
            log(map1.toString());
        } else {
            log("你还没出门呢");
        }
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！","90",all90,remain90,remain90Pos));
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！","80",all80,remain80,remain80Pos));
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！","70",all70,remain70,remain70Pos));
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！","60",all60,remain60,remain60Pos));
        if (!myHouses.isEmpty()) {

            log("我的地皮：");
            for (Map house : myHouses) {
                log("\t" + house.toString());
            }
        } else {
            log("我 还没有占领 地皮。");
        }
    }

    private void setConnectState(boolean connect) {
        if (!mFlagConnect && connect) {
            send();
        }
        if (mFlagConnect ^ connect) {
            mFlagConnect = connect;
            log("检测连接状态:" + mFlagConnect);
        }
    }

    public boolean isConnected() {
        return mFlagConnect;
    }

    class Map {
        int zuobiao;
        String name;
        int price;
        int user1;
        int levle1;
        int user2;
        int levle2;
        int user3;
        int levle3;

        public Map(int index, String mapStr) {

            if (index == mCurMapIndex) {
                this.zuobiao = mCurMapZuobiao;
            } else {
                this.zuobiao = 10000 + index;
            }
            String[] split = mapStr.split(" ");
            int i = 0;
            name = split[i++];
            price = str2Int(split[i++]);
            user1 = str2Int(split[i++]);
            levle1 = str2Int(split[i++]);
            user2 = str2Int(split[i++]);
            levle2 = str2Int(split[i++]);
            user3 = str2Int(split[i++]);
            levle3 = str2Int(split[i++]);
            if (price == 90) {
                all90++;
                if (!isFull() && !isMyHouse()) {
                    remain90++;
                    remain90Pos += emptyPos();
                }
            } else if (price ==80) {
                all80++;
                if (!isFull() && !isMyHouse()) {
                    remain80++;
                    remain80Pos += emptyPos();
                }
            } else if (price ==70) {
                all70++;
                if (!isFull() && !isMyHouse()) {
                    remain70++;
                    remain70Pos += emptyPos();
                }
            } else if (price ==60) {
                all60++;
                if (!isFull() && !isMyHouse()) {
                    remain60++;
                    remain60Pos += emptyPos();
                }
            }
        }

        public int emptyPos() {
            if (!isMyHouse()) {
                if (user1 == 0) {
                    return 3;
                } else if (user2 == 0) {
                    return 2;
                } else if (user3 == 0) {
                    return 1;
                }
            }
            return 0;
        }

        public boolean isFull() {
            return user1 > 0 && user2 > 0 && user3 > 0;
        }

        public boolean canBuild() {
            return !isFull() || isMyHouse();
        }

        public boolean isMyHouse() {
            return user1 == mId || user2 == mId || user3 == mId;
        }

        @Override
        public String toString() {
            return
                    "地图='" + name + '\'' +
                            ", 序号='" + zuobiao + '\'' +
                            ", 价格='" + price + '\'' +
                            ", 用户1='" + user1 + '\'' +
                            ", 用户1等级=" + levle1 +
                            ", 用户2='" + user2 + '\'' +
                            ", 用户2等级=" + levle2 +
                            ", 用户3='" + user3 + '\'' +
                            ", 用户3等级=" + levle3 + "\r\n"
                    ;
        }

        public boolean BuildIfGood() {
            if (priceFit() && canBuild()) {
                build();
                return true;
            }
            return false;
        }

        private boolean priceFit() {
            return price >= 80 && price < 1000;
        }
    }

    public void build() {
        setCmd(cmdBuild, mCurMapZuobiao);
    }


    public void zhiDing(int i) {
        setCmd(cmdZhiZD, i);
    }

    public static Integer str2Int(String str) {
        return Integer.valueOf(str);
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
                if (pendingCmd.isEmpty()) {
                    return;
                }
                mCurCmd = pendingCmd.removeFirst();
                isSending = true;
                log("准备发送:" + mCurCmd);
                strSend.write(mCurCmd.getBytes(bm));
                strSend.flush();
                String arg = "发送：'" + mCurCmd + "'到服务器完成~"
                        + "-当前时间：" + new Date().toString();
                log(arg);
                try {
                    int read = mInputStream.read(dPacket.getData());
                    dPacket.setLength(read);
                    //通知观察者
                    通知观察者(QuanDi.this.dPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                strSend.write("心跳开始\t183.60.204.64\t183.60.204.64".getBytes(bm));
                strSend.flush();
                log("心跳包发送完成~");
                /*try {
                    int read = mInputStream.read(dPacket.getData());
                    dPacket.setLength(read);
                    //通知观察者
                    通知观察者(QuanDi.this.dPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
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

    private static void log(String arg) {
        System.err.println(TAG + arg);
    }
}