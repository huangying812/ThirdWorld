package szz.com.baselib.rest;

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
import java.util.Date;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author jdh
 * @file ServerSocket.java
 * @brief 信令端口类
 * @date 2016/7/9
 * @update 2016/7/10
 */

public class JunTuan implements Runnable {

    private static final int TIME_MIN_MS = 60 * 1000;
    public static Pattern NUM_REG = Pattern.compile("\\d+(,\\d{3})*");
    public static final String bm = "gbk";
    public static String cmdLogin = "帮派团队登入〓%1$s〓%2$s\t183.60.204.16\t183.60.204.16";
    public static String cmdCommit = "军团农民项目提交〓%1$s〓%2$s〓%3$d\t183.60.204.16\t183.60.204.16";
    public static String cmdStart = "军团农民开始工作〓%1$s〓%2$s〓0%3$d-%4$d\t183.60.204.16\t183.60.204.16";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日hh时mm分");
    private static final SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
    //常量
    //服务器IP
    private final String SERVER_IP = "183.60.204.16";
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
    private int mMaxBuild = 7;
    private boolean mayHasZhiDing = true;
    private int gapTime;
    private int mIndex = 1;
    private NongMin[] mNongMins = new NongMin[5];
    private SendThread mSendThread = new SendThread();

    public static void main(String... args) {
//        log(getDate("2017-06-26 19:28:09").toString());
        大号();
//        hy812();
//        hy815();
    }

    public static void hy812() {
        JunTuan quanDi = new JunTuan("hy812", "123");
        new Thread(quanDi).start();
    }

    public static void hy815() {
        JunTuan quanDi = new JunTuan("hy815", "123");
        new Thread(quanDi).start();
    }

    public static void 大号() {
        JunTuan quanDi = new JunTuan("hy814", "123");

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
                try {
                    return sdf3.parse(strDate);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return new Date(0);
    }

    /*public static String getFormatedDateYMDHMChinese(long utcTime) {
        return getFormatedDate(utcTime, sdf, "");
    }

    public static String getFormatedDate(long utcTime, String format, String defaultString) {

        return new SimpleDateFormat(format).format(new Date(utcTime));
    }*/

    /**
     * @param account
     * @param pwd
     * @brief 构造函数
     */
    public JunTuan(String account, String pwd) {

        this.account = account;
        this.pwd = pwd;
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
        mSendExecutor.execute(mSendThread);
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
                            通知观察者(JunTuan.this.dPacket);
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
                setCmd(cmdLogin);
            } else {
                processResponse(msg);
                isSending = false;
                send();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void processResponse(String res) {
        String[] split = res.split("〓");
        int nongMingCounts;
        String[] nongMingInfos;
        Date curDate = null;
        String[] value = null;
        if (split.length > 0) {
            switch (split[0]) {
                case "团队登入":
//团队登入〓4〓111〓幻〓2〓绿野仙踪〓3〓2017-08-28 11:00:55；04-2，2017-08-28 11:00:55；04-2，2017-08-28 11:00:55；04-2，无，无
// 〓184 186 204 293〓16 9 17 9〓0 0 0 0〓500 500 500 500〓2100〓2100〓2100〓2100〓2017年8月28日11时4分29秒〓
// 桃花源 1 2 9※花儿红 191470 193570 159100 159340 407800 547090 2150708 0 10000000 10000000 8347145 8243602〓86628〓72308〓872308〓101〓20520〓31920〓0〓219718〓6〓0 0 0 0 0〓93〓91811〓青铜剑 1 5 10 5 5 11988 3001 101，蚩尤凤羽靴 0 127 126 129 251 12002 3004 604，青铜铠 4 4 5 10 4 11987 3002 102，青铜盔 2 9 5 5 4 11989 3003 103，〓3183923〓211 354 71 0〓0〓0〓1〓0〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓0
                    nongMingCounts = str2Int(split[6]);
                    nongMingInfos = split[7].split("，");
                    curDate = getDate2(split[16]);
                    value = split[17].split(" ");
                    findNeedType(value);
                    gapTime = new Date().compareTo(curDate);
                    dealWithDatas(nongMingCounts, nongMingInfos, curDate);
                    break;
                case "军团农民开发提交完成":
//军团农民开发提交完成〓3〓无，无，无，无，无〓桃花源 1 2 9※花儿红 190030 191950 157120 158080 373600 541330 2150708 0 10000000 9836509 8155605 8051773〓19440〓31920〓3〓04〓2〓180
//军团农民开发提交完成〓3〓无，2017-08-28 10:16:16；04-2，2017-08-28 10:16:17；04-2，无，无〓桃花源 1 2 9※花儿红 190570 192490 157840 158620 388000 543490 2150708 0 10000000 9836509 8155605 8051773〓19620〓31920〓1〓04〓2〓180
//军团农民开发提交完成〓3〓无，无，2017-08-28 10:16:17；04-2，无，无〓桃花源 1 2 9※花儿红 190570 192490 158020 158620 388000 543490 2150708 0 10000000 9836509 8155605 8051773〓19800〓31920〓2〓04〓2〓180
                    nongMingCounts = str2Int(split[1]);
                    nongMingInfos = split[2].split("，");
                    value = split[3].split(" ");
                    findNeedType(value);
                    dealWithDatas(nongMingCounts, nongMingInfos, curDate);
                    break;
                case "军团农民工作安排完成":
//军团农民工作安排完成〓3〓2017-08-28 10:16:15；04-2，无，无，无，无〓86708〓04〓2
//军团农民工作安排完成〓3〓2017-08-28 10:16:15；04-2，2017-08-28 10:16:16；04-2，无，无，无〓86698〓04〓2
//军团农民工作安排完成〓3〓2017-08-28 10:16:15；04-2，2017-08-28 10:16:16；04-2，2017-08-28 10:16:17；04-2，无，无〓86688〓04〓2
                    nongMingCounts = str2Int(split[1]);
                    nongMingInfos = split[2].split("，");
                    dealWithDatas(nongMingCounts, nongMingInfos, curDate);
                    break;
                case "错误，阁下的[1]号农民工开发项目异常-1！":
                case "错误，阁下的[2]号农民工开发项目异常-1！":
                case "错误，阁下的[3]号农民工开发项目异常-1！":
//错误，阁下的[1]号农民工开发项目异常-1！
                    setCmd(cmdStart, 4, 2);
                    break;
                case "错误，阁下没有那么多空闲的农民工！":
                case "错误，阁下的[1]号农民工开发项目还没完成！":
                case "错误，阁下的[2]号农民工开发项目还没完成！":
                case "错误，阁下的[3]号农民工开发项目还没完成！":
//错误，阁下没有那么多空闲的农民工！
                    break;
                default:
                    sendCommitDelay(1000 * 30, mIndex++ % 3 + 1);
            }

        }
    }

    private void findNeedType(String[] value) {
        int shangYe = str2Int(value[4]);
        int nongYe = str2Int(value[5]);
        int caiKuang = str2Int(value[6]);
        int faMu = str2Int(value[7]);
        int min = Math.min(shangYe, Math.min(nongYe, Math.min(caiKuang, faMu)));
        if (min== shangYe){
            typeNeed = 2;
        } else if (min== nongYe){
            typeNeed = 3;
        } else if (min== caiKuang){
            typeNeed = 4;
        } else if (min== faMu){
            typeNeed = 5;
        } else {
            typeNeed = -1;
        }
    }

    private void dealWithDatas(int nongMingCounts, String[] nongMingInfos, Date curDate) {
        for (int i = 1; i <= nongMingCounts; i++) {
            NongMin nongMin = mNongMins[i - 1];
            if (nongMin == null) {
                nongMin = new NongMin(i, 0);
                mNongMins[i - 1]= nongMin;
            }
            String s = nongMingInfos[i-1];
            if ("无".equals(s)) {
                nongMin.buildDelay(0);
            } else {
                nongMin.setBuildTime(s);
                nongMin.start(curDate);
            }
        }
    }

    int typeNeed = -1;
    int type;
    int level = 2;

    class NongMin {

        final int index;
        long buildTime;
        long timeNeed = 6 * TIME_MIN_MS;
        private boolean isCommitWaite;
        private boolean isBuildWaite;

        private int getType() {
            if (typeNeed > 0) {
                return typeNeed;
            }
            return type % 5 + 1;
        }

        public NongMin(int index, long buildTime) {
            this.index = index;
            this.buildTime = buildTime;
        }

        public void setBuildTime(String time) {
            //2017-08-28 11:00:55
            buildTime = getDate(time).getTime();
        }

        public void build(int type, int level) {
            isBuildWaite = false;
            setCmd(cmdStart, type, level);
        }

        public void commit() {
            isCommitWaite = false;
            setCmd(cmdCommit, index);
        }

        public void start(Date curDate) {
            if (curDate == null) {
                curDate = new Date();
            }

            long compareTo = curDate.getTime() - (buildTime + timeNeed);
            if (compareTo >= 0) {
                commit();
            } else {
                commitDelay(Math.abs(compareTo));
            }
        }

        private void commitDelay(final long delayMs) {
            if (!isCommitWaite) {
                isCommitWaite = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (delayMs>0) {
                            try {
                                Thread.sleep(delayMs);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        commit();
                    }
                }).start();
            }
        }

        private void buildDelay(final long delayMs) {
            if (!isBuildWaite) {
                isBuildWaite = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (delayMs>0) {
                            try {
                                Thread.sleep(delayMs);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        build(getType(), level);
                    }
                }).start();
            }
        }
    }

    private Date getServerDate() {
        return new Date(System.currentTimeMillis() - gapTime);
    }

    private void sendCommitDelay(final long delayMs, final int commitIndex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayMs);
                    setCmd(cmdCommit, commitIndex);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void setCmd(String cmdStrResId) {
        sendCmd(cmdStrResId, account, pwd);
    }

    String account,pwd;

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

    private void setConnectState(boolean connect) {
        if (!mFlagConnect && connect) {
            send();
        }
        if (mFlagConnect ^ connect) {
            mFlagConnect = connect;
            log("检测连接状态:" + mFlagConnect);
        }
    }

    public void postMsg(String msg) {
        postMsg(LogStr.newInstance(msg));
    }

    public void postMsg(LogStr event) {

//        EventUtil.post(event);
        log("app日志显示：" + event.logMsg);
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
    private class SendThread extends TimerTask {

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
//                log("准备发送:" + mCurCmd);
                strSend.write(mCurCmd.getBytes(bm));
                strSend.flush();
                String arg = "发送：'" + mCurCmd + "'到服务器完成~"
                        + "-当前时间：" + getServerDate().toString();
                log(arg);
                try {
                    int read = mInputStream.read(dPacket.getData());
                    dPacket.setLength(read);
                    //通知观察者
                    通知观察者(JunTuan.this.dPacket);
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
                strSend.write("心跳开始\t183.60.204.16\t183.60.204.16".getBytes(bm));
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

    public static int str2Int(String intStr) {
        int integer = 0;
        try {
            Matcher m = NUM_REG.matcher(intStr);
            if (m.find()) {
                integer = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return integer;
    }

    public static class LogStr {

        public final String logMsg;

        public static LogStr newInstance(String msg) {
            return new LogStr(msg);
        }

        public LogStr(String logMsg) {
            this.logMsg = logMsg;
        }
    }
}