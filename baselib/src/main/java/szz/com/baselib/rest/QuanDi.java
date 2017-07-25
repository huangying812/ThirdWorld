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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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

public class QuanDi implements Runnable {

    public static Pattern NUM_REG = Pattern.compile("\\d+(,\\d{3})*");
    public static final String bm = "gbk";
    public static final int 星期一价格控制线 = 80;
    //盟军读取个人圈地运动〓hy814〓123\t183.60.204.64\t183.60.204.64
    //盟军圈地运动本次开通〓hy814〓123\t183.60.204.64\t183.60.204.64
    //盟军圈地读取最新服务器数据\t183.60.204.64\t183.60.204.64
    //盟军圈地掷骰子〓hy814〓123\t183.60.204.64\t183.60.204.64
    //盟军圈地开始进行建造升级〓hy814〓123〓10005\t183.60.204.64\t183.60.204.64
    public static String cmdReadS = "盟军圈地读取最新服务器数据\t183.60.204.64\t183.60.204.64";
    public static String cmdReadP = "盟军读取个人圈地运动〓%1$s〓%2$s\t183.60.204.64\t183.60.204.64";
    public static String cmdEnable = "盟军圈地运动本次开通〓%1$s〓%2$s\t183.60.204.64\t183.60.204.64";
    public static String cmdZhiSZ = "盟军圈地掷骰子〓%1$s〓%2$s\t183.60.204.64\t183.60.204.64";
    public static String cmdZhiZD = "盟军圈地掷骰子指定〓%1$s〓%2$s〓%3$d\t183.60.204.64\t183.60.204.64";
    public static String cmdBuild = "盟军圈地开始进行建造升级〓%1$s〓%2$s〓%3$d\t183.60.204.64\t183.60.204.64";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日hh时mm分");
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
    private int mMaxBuild = 7;
    private boolean mayHasZhiDing = true;

    public static void main(String... args) {
//        log(getDate("2017-06-26 19:28:09").toString());
        大号();
//        hy812();
//        hy815();
    }

    public static void hy812() {
        QuanDi quanDi = new QuanDi("hy812", "123");
        new Thread(quanDi).start();
    }

    public static void hy815() {
        QuanDi quanDi = new QuanDi("hy815", "123");
        new Thread(quanDi).start();
    }

    public static void 大号() {
        QuanDi quanDi = new QuanDi("hy814", "123");
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
        return new Date();
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
    public QuanDi(String account, String pwd) {

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
//                zhiDing(4);
            } else {
                processResponse(msg);
                isSending = false;
                send();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Map> mMaps = new ArrayList<>();

    public void processResponse(String res) {
        String[] split = res.split("〓");
        Map map;
        if (split.length > 0) {
            switch (split[0]) {
                case "帮派读取圈地副本返回":
//帮派读取圈地副本返回〓hy814〓10091〓4〓7320〓2017-06-26 19:02:12〓2017年6月26日20时51分43秒〓26〓7
                    onReadPersonInfoRsp(split);

                    break;
                case "帮派圈地开通本周成功":
                    //帮派圈地开通本周成功〓hy812〓3244.117
                    zhiShaiZi();
//                    zhiDing(6);

                    break;
                case "盟军圈地读最新返回":
                    processMapData(split[1]);
                    if (mCurMapIndex > 0 && mCurMapZuobiao < 20000) {
                        map = mMaps.get(mCurMapIndex - 1);
                        log(map.showInfo());
                        if (!map.BuildIfGood()) {
                            zhiShaiZi(map);
                        }
                    } else {
                        zhiShaiZi();
                    }

                    break;
                case "盟军圈地掷骰子成功返回":
                    onZhiShaiZiRsp(split);

                    break;
                case "盟军圈地建造升级返回":
//盟军圈地建造升级返回〓20009〓920〓弄梅洞窟 80 4 1 0 0 0 0〓80
                    onBuildRsp(split[1]);

                    break;
                case "错误，指定投骰子需要在每周的2.4.6才有效！且每天仅有2次机会！！":
                case "错误，3-3阁下的2次指定次数已经用完了！":
//错误，3-3阁下的2次指定次数已经用完了！
//错误，指定投骰子需要在每周的2.4.6才有效！且每天仅有2次机会！！
                    mayHasZhiDing = false;
                    setCmd(cmdReadP);
                    break;
                default:
                    if (split[0].contains("中的资金不足无法进行建造（升级）操作！")) {
                        zhiShaiZi();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000 * 30);
                                setCmd(cmdReadP);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
            }

        }
    }

    private void onBuildRsp(String str) {
        最新坐标(str);
        if (myHouses.size() < mMaxBuild) {
            Map map2 = mMaps.get(mCurMapIndex - 1);
            if (!map2.BuildIfGood()) {
                zhiShaiZi(map2);
            }
        } else {
            zhiShaiZi();
        }
    }

    private void weatherFinishTask() {
        switch (getWeekDays()) {
            case "星期一":
                if (myHouses.size() >= 2) {
                    postMsg("今天是星期一，策略是建好两个房子后停止行动。目标达成，停止行动...");
//                    System.exit(0);
                }
            case "星期二":
                if (myHouses.size() >= 4) {
                    postMsg("今天是星期二，策略是建好4个房子后停止行动。目标达成，停止行动...");
                    System.exit(0);
                }
            case "星期三":
                /*if (myHouses.size() >= mMaxBuild) {
                    postMsg("今天是星期三，策略是建好" + mMaxBuild + "个房子后停止行动。目标达成，停止行动...");
                    System.exit(0);
                }*/
                break;
            case "星期四":
                break;
            case "星期五":
                break;
            case "星期六":
                break;
            case "星期日":
            case "星期天":
                break;
        }
    }

    private void onZhiShaiZiRsp(String[] split) {
//盟军圈地掷骰子成功返回〓10012〓7〓2017-06-26 20:30:25〓254〓0〓36〓4936〓12〓0〓无〓战神台 80 76 3 23 3 25 2；铜人殿
//盟军圈地掷骰子成功返回〓10123〓9〓2017-06-27 08:50:42〓325〓0〓0〓4365〓123〓7〓遇到强盗〓战神台 80 76 3 23 4 25 2；铜人殿 20 0 0 0 0 0
        Map map;//最新坐标
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
        postMsg("圈地-掷骰子结果-点数：" + dianShu
                + "-机动力：" + mTimes
                + "-路费：" + cost + "-资金：" + mZijin
                + "-地皮：" + index + "-特殊事件：" + event + "-影响：" + un2);
        processMapData(split[11]);
        map = mMaps.get(index - 1);
        log(map.showInfo());
        if (!map.BuildIfGood()) {
            zhiShaiZi(map);
        }
    }

    private void onReadPersonInfoRsp(String[] split) {
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
                + "-当前时间：" + mTimeCur.toString() + getWeekDays()
                + "-机动力：" + mTimes
                + "-资金：" + mZijin
                + "-本周个人最高可占领地皮数：" + mMaxBuild
        );
        if (mZijin <= 0) {
            setCmd(cmdEnable);
        } else {
            sendCmd(cmdReadS);
        }
    }

    private void zhiShaiZi(Map map) {
        weatherFinishTask();
        if (mayHasZhiDing) {
            int i = map.hasValueHouse(90);
            if (i < 1) {
//                i = map.hasValueHouse(80);
                if (i > 0) {
                    zhiDing(i);
                } else if (myHouses.size() >= mMaxBuild) {
                    zhiDing(9);
                } else {
                    zhiShaiZi();
                }

            } else {
                zhiDing(i);
            }
        } else {
            zhiShaiZi();
        }
    }

    private void zhiShaiZi() {
        weatherFinishTask();
        try {
            Thread.sleep(2500);
            postMsg("圈地-掷 普通 骰子");
            setCmd(cmdZhiSZ);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getWeekDays() {
        return dateFm.format(mTimeCur);
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
            postMsg("圈地-位置：" + map1.showInfo());
        } else {
            log("你还没出门呢");
        }
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！", "90", all90, remain90, remain90Pos));
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！", "80", all80, remain80, remain80Pos));
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！", "70", all70, remain70, remain70Pos));
        log(String.format("价格%1$s以上的地皮总数 %2$s，还剩%3$s块可用，空位置共%4$s！", "60", all60, remain60, remain60Pos));
        if (!myHouses.isEmpty()) {

//            log("我的地皮：");
            StringBuilder sb = new StringBuilder("我的地皮：\r\n");
            for (Map house : myHouses) {
//                log("\t" + house.toString());
                sb.append(house.showMyHouseInfo() + "\r\n");
            }
            postMsg(sb.toString());
        } else {
            log("我 还没有占领 地皮。");
            postMsg("我 还没有占领 地皮~");
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

    class Map {
        int index;
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

            this.index = index;
            if (index == mCurMapIndex) {
                this.zuobiao = mCurMapZuobiao;
            } else {
                this.zuobiao = 10000 + index;
            }
            String[] split = mapStr.split(" ");
            int i = 0;
            name = split[i++];
            price = str2Int(split[i++]);
            if (split.length > 3) {
                user1 = str2Int(split[i++]);
                levle1 = str2Int(split[i++]);
                if (split.length > 5) {
                    user2 = str2Int(split[i++]);
                    levle2 = str2Int(split[i++]);
                    if (split.length > 7) {
                        user3 = str2Int(split[i++]);
                        levle3 = str2Int(split[i++]);
                    }
                }
            }
            if (price == 90) {
                all90++;
                if (!isFull() && !isMyHouse()) {
                    remain90++;
                    remain90Pos += emptyPos();
                }
            } else if (price == 80) {
                all80++;
                if (!isFull() && !isMyHouse()) {
                    remain80++;
                    remain80Pos += emptyPos();
                }
            } else if (price == 70) {
                all70++;
                if (!isFull() && !isMyHouse()) {
                    remain70++;
                    remain70Pos += emptyPos();
                }
            } else if (price == 60) {
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

        public int hasValueHouse(int price) {
            if (mMaps != null && !mMaps.isEmpty()) {
                int index = this.index - 1;
                for (int i = 1; i < 10; i++) {
                    Map map = mMaps.get((index + i) % mMaps.size());
                    if ((map.price >= price || map.isMyHouse()) && map.canBuild()) {
                        return i;
                    }
                }

            }
            return 0;
        }

        public boolean levelAllow() {
            int level = getMyLevel();
            return level < 5;
        }

        private int getMyLevel() {
            if (mId == user1) {
                return levle1 ;
            } else if (mId == user2) {
                return levle2 ;
            } else if (mId == user3) {
                return levle3 ;
            }
            return -1;
        }

        public boolean isFull() {
            return user1 > 0 && user2 > 0 && user3 > 0;
        }

        public boolean canBuild() {
            return (!(isFull() || myHouses.size() >= mMaxBuild) || isMyHouse()) && levelAllow();
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
                            ", 用户1='" + getUser(user1) + '\'' +
                            ", 用户1等级=" + levle1 +
                            ", 用户2='" + user2 + '\'' +
                            ", 用户2等级=" + levle2 +
                            ", 用户3='" + user3 + '\'' +
                            ", 用户3等级=" + levle3 + "\r\n"
                    ;
        }

        public String showInfo() {
            String s;
            if (isMyHouse()) {
                s = "\t是我的房产,等级 " + getLevelStr(getMyLevel());
            } else {
                int i = emptyPos();
                String s1;
                if (i > 0) {
                    s1 = "还有空位" + i +"个";

                } else {
                    s1 = "并且已满";
                }
                s = "\t不是我的房产，" + s1;
            }
            return "地图 " + name + "\t 序号 " + zuobiao + "\t价格 " + price + s ;
        }

        public String showInfo1() {
            return
                    "地图 " + name + "\t 序号 " + zuobiao + "\t价格 " + price + "\t用户1 " + getUser(user1)
                            + "   " + getLevelStr(levle1) +
                            "\t用户2 " + getUser(user2) + "   " + getLevelStr(levle2) +
                            "\t用户3 " + getUser(user3) + "   " + getLevelStr(levle3)
                    ;
        }

        public String showMyHouseInfo() {
            return "地图 " + name + "\t 序号 " + zuobiao + "\t价格 " + price + "\t等级 " + getLevelStr(getMyLevel());
        }

        private String getUser(int user) {
            if (user == mId) {
                return "我";
            }
            return String.valueOf(user);
        }

        private String getLevelStr(int lelvel) {
            switch (lelvel) {
                case 0:
                default:
                    return "";
                case 1:
                    return "☆";
                case 2:
                    return "☆☆";
                case 3:
                    return "☆☆☆";
                case 4:
                    return "☆☆☆☆";
                case 5:
                    return "☆☆☆☆☆";

            }
        }

        public boolean BuildIfGood() {
            if (priceFit() && canBuild()) {
                build();
                return true;
            }
            return false;
        }

        private boolean priceFit() {
            int priceBaseValue;
            switch (getWeekDays()) {
                case "星期一":
                case "星期二":
                    priceBaseValue = 星期一价格控制线;
                    break;
                case "星期三":
                    priceBaseValue = 70;
                    break;
                case "星期四":
                    priceBaseValue = 60;
                    break;
                case "星期五":
                    priceBaseValue = 50;
                    break;
                case "星期六":
                    priceBaseValue = 40;
                    break;
                case "星期日":
                case "星期天":
                default:
                    priceBaseValue = 30;
                    break;
            }
            return price >= priceBaseValue && price < 1000;
        }
    }

    public void build() {
        setCmd(cmdBuild, mCurMapZuobiao);
    }


    public void zhiDing(int i) {
        setCmd(cmdZhiZD, i);
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
//                log("准备发送:" + mCurCmd);
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