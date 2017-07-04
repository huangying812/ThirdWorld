package com.thirdworld.activity;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.TimeUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import szz.com.baselib.application.ContextHolder;
import szz.com.baselib.application.SpUtils;
import szz.com.baselib.singleton.ConnectManager;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;
import org.objectweb.asm.Opcodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import szz.com.baselib.entity.events.ConnectState;
import szz.com.baselib.entity.events.GetServerPack;
import szz.com.baselib.rest.ServerSocket;
import szz.com.thirdworld.R;


public class SocketActivity extends BaseActivity implements OnClickListener{

    private TextView mTvLog;
    private Button send;
    private EditText mEdtMsg;
    private ServerSocket mServerSocket;
    private String curCmd;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                String str = bundle.getString("msg");
                String resultStr;
                switch (msg.what) {
                    case WHAT_COMM:
                    default:
                        resultStr = str;
                        break;
                    case WHAT_SERVER_RESP:
                        resultStr = "服务器返回:" + str;
                        break;
                    case WHAT_ERROR:
                        resultStr = "错误:" + str;
                        break;
                }
                if (resultStr != null) {
                    if (!resultStr.endsWith("\n")) {
                        resultStr += "\n";
                    }
                    mTvLog.append(resultStr);
                }
            }
        }

    };
    private EditText edtAccount;
    private EditText edtTarIp;
    private EditText edtLocalIp;
    private LinearLayout llAccount;
    private EditText edtPwd;
    private LinearLayout llPwd;
    private EditText edtServer;
    private LinearLayout llServer;
    private EditText edtPort;
    private LinearLayout llPort;
    private String account;
    private String pwd;
    private String serverIp;
    private String port;
    private Context context;
    private PopupMenu popup;
    private WifiManager.MulticastLock lock;
    private String mHostIp;
    private String checkformat;
    private String linformat;
    private String juformat;
    private String checkinsformat;
    private String checkinfformat;
    private String fightinfoformat;
    private String fightformat;
    private String fightssformat;
    private String ssformat;
    private String ssybformat;
    private String msg;
    private String mac;
    private String deviceId;
    private String op = "-1";
    private int ssguan;
    private int[] sshis = new int[50];
    JSONArray petlist = new JSONArray();

    int qdid = 0;
    String[] qus = new String[401];
    private String curdiid = "";
    private int curdiindex;
    int curguan = 0;
    private ImageView fightView;
    int guan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_two);
        initData();
        getHostIP();
        initView();
        WifiManager manager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        lock = manager.createMulticastLock("test wifi");
    }

    @Override
    protected void onResume() {
        super.onResume();
        account = SpUtils.getAccount();
        pwd = SpUtils.getPwd();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            goToLogin();
        }
        setTitle("zz盟军(" + account + ")");
    }

    private void goToLogin() {
        LoginActivity.launch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_other:
                goToLogin();
                break;
            case R.id.setting:
//                SettingsActivity.launch(this);
                showToast(R.string.has_not_open_yet);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getLocalMacAddress() {
        try {
            return ((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE)).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            return "unkown_mac";
        }
    }

    public String getDeviceId() {
        try {
            String thisid = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            if (thisid.length() > 0) {
                return thisid;
            }
            return "unkown_devid";
        } catch (Exception e) {
            return e.getMessage() + "unkown_devid";
        }
    }

    private void initData() {
        this.checkformat = getResources().getString(R.string.checkincom);
        this.linformat = getResources().getString(R.string.lincom);
        this.juformat = getResources().getString(R.string.jubaopen);
        this.checkinsformat = getResources().getString(R.string.checkins);
        this.checkinfformat = getResources().getString(R.string.checkinf);
        this.fightinfoformat = getResources().getString(R.string.fightinfo);
        this.fightformat = getResources().getString(R.string.fightcom);
        this.fightssformat = getResources().getString(R.string.fightcomss);
        this.ssformat = getResources().getString(R.string.ss);
        this.ssybformat = getResources().getString(R.string.ssyb);
        this.mac = getLocalMacAddress();
        this.deviceId = getDeviceId();
        setqus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mServerSocket.
    }

    private void initView() {
        edtTarIp = (EditText) findViewById(R.id.edtTarIp);
        edtLocalIp = (EditText) findViewById(R.id.edtLocalIp);
        edtAccount = (EditText) findViewById(R.id.edtAccount);
        edtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account = s.toString().trim();
                SpUtils.saveAccount(account);
            }
        });
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        edtPwd = (EditText) findViewById(R.id.edtPwd);
        edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pwd = s.toString().trim();
                SpUtils.savePwd(pwd);
            }
        });
        llPwd = (LinearLayout) findViewById(R.id.llPwd);
        edtServer = (EditText) findViewById(R.id.edtServer);
        edtServer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serverIp = s.toString().trim();
                SpUtils.saveServer(serverIp);
                initSocket();

            }
        });
        llServer = (LinearLayout) findViewById(R.id.llServer);
        mTvLog = (TextView) findViewById(R.id.tvLog);
        edtPort = (EditText) findViewById(R.id.edtPort);
        edtPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                port = s.toString().trim();
                SpUtils.savePort(port);
                initSocket();
            }
        });
        llPort = (LinearLayout) findViewById(R.id.llPort);

        send = (Button) findViewById(R.id.btnSend);
        findViewById(R.id.tvChangCmd).setOnClickListener(this);
        mEdtMsg = (EditText) findViewById(R.id.edtCmd);
        mTvLog.setOnClickListener(this);
        send.setOnClickListener(this);

        serverIp = SpUtils.getServer();
        port = SpUtils.getPort();
        initSocket();
        edtAccount.setText(account);
        edtPwd.setText(pwd);
        edtServer.setText(serverIp);
        edtPort.setText(port);
    }

    private void initSocket() {

        mServerSocket = ConnectManager.getInstance().getSocket();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChangCmd:
                chooseCmd(v);
                break;
            case R.id.btnSend:
                send(curCmd, op);
                break;
        }
    }

    private void send(String... cmd) {
        String data = cmd[0];
        String op = cmd[1];
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(op)) {
            return;
        }
        this.op = op;
//        sendMsg(WHAT_COMM, "即将发送：" + cmd[0]);
        mServerSocket.addAndSend(data);
    }

    private void chooseCmd(View v) {
        popUpAMenu(v, R.menu.popup_menu_id_type, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.checkincom:
                        setCmd(R.string.checkincom);
                        mEdtMsg.setText(R.string.str_checkincom);
                        op = "2";
                        break;
                    case R.id.buycom:
                        setCmd(R.string.gudinggoumaihuoli);
                        op = "5";

                        mEdtMsg.setText(R.string.str_buycom);
                        break;
                    case R.id.buy2com:
                        setCmd(R.string.yuanbaogoumaihuoli);
                        op = "6";

                        mEdtMsg.setText(R.string.str_buy2com);
                        break;
                    /*case R.id.getpetinfocom:
                        setCmd(R.string.getpetinfocom);
                        op = "14";

                        mEdtMsg.setText(R.string.str_getpetinfocom);
                        break;*/
                    /*case R.id.lincom:
                        setCmd(R.string.lincom);
                        op = "7";

                        mEdtMsg.setText(R.string.str_lincom);
                        break;*/
                    case R.id.jucom:
                        setCmd(R.string.jubaopen);
                        op = "8";

                        mEdtMsg.setText(R.string.str_jucom);
                        break;
                    case R.id.qiangcom:
                        setCmd(R.string.qianghuoli);
                        op = "11";

                        mEdtMsg.setText(R.string.str_qiangcom);
                        break;
                    /*case R.id.petcom:
                        setCmd(R.string.petcom);
                        break;*/
                    /*case R.id.qdgrcom:
                        curCmd = getString(R.string.qdgrcom);
                        op = "17";

                        mEdtMsg.setText(R.string.str_qdgrcom);
                        break;
                    case R.id.qdgxcom:
                        mEdtMsg.setText(R.string.qdgxcom);
                        mEdtMsg.setText(R.string.str_qdgxcom);
                        op = "16";
                        break;
                    case R.id.dicecom:
                        setCmd(R.string.dicecom);
                        op = "18";

                        mEdtMsg.setText(R.string.str_dicecom);
                        break;
                    case R.id.qdbegin:
                        setCmd(R.string.qdbegin);
                        mEdtMsg.setText(R.string.str_qdbegin);
                        op = "20";
                        break;*/
                    /*case R.id.bindingcom:
                        setCmd(R.string.bindingcom);
                        break;
                    case R.id.getinfocom:
                        setCmd(R.string.getinfocom);
                        break;
                    case R.id.fightcom:
                        setCmd(R.string.fightcom);
                        break;
                    case R.id.ss:
                        setCmd(R.string.ss);
                        break;
                    case R.id.fightcomss:
                        setCmd(R.string.fightcomss);
                        break;
                    case R.id.shtqcom:
                        setCmd(R.string.shtqcom);
                        break;
                    case R.id.ystqcom:
                        setCmd(R.string.ystqcom);
                        break;
                    case R.id.buildcom:
                        setCmd(R.string.buildcom);
                        break;*/
                }
                //隐藏该对话框
                popup.dismiss();
                return false;
            }
        });
    }

    private void setCmd(int cmdStrResId) {
        curCmd = getFormat(cmdStrResId, account, pwd);
    }

    @NonNull
    private String getStringFromEdt(EditText editText) {
        return editText.getText().toString();
    }

    protected void popUpAMenu(View view, int menuId, PopupMenu.OnMenuItemClickListener listener) {
        //创建PopupMenu对象
        popup = new PopupMenu(context, view);
        //将R.menu.popup_menu菜单资源加载到popup菜单中
        Activity activity = ContextHolder.getActivity(context);
        if (activity == null) {
            return;
        }
        activity.getMenuInflater().inflate(menuId, popup.getMenu());
        //为popup菜单的菜单项单击事件绑定事件监听器
        popup.setOnMenuItemClickListener(listener);
        popup.show();
    }

    private void submit() {
        // validate
        String edtAccountString = getStringFromEdtWithTrim(edtAccount);
        if (TextUtils.isEmpty(edtAccountString)) {
            Toast.makeText(this, "edtAccountString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edtPwdString = getStringFromEdtWithTrim(edtPwd);
        if (TextUtils.isEmpty(edtPwdString)) {
            Toast.makeText(this, "edtPwdString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edtServerString = getStringFromEdtWithTrim(edtServer);
        if (TextUtils.isEmpty(edtServerString)) {
            Toast.makeText(this, "edtServerString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edtPortString = getStringFromEdtWithTrim(edtPort);
        if (TextUtils.isEmpty(edtPortString)) {
            Toast.makeText(this, "edtPortString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edtMsgString = getStringFromEdtWithTrim(mEdtMsg);
        if (TextUtils.isEmpty(edtMsgString)) {
            Toast.makeText(this, "给服务器发送信息", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @NonNull
    private String getStringFromEdtWithTrim(EditText editText) {
        return getStringFromEdt(editText).trim();
    }

    @Subscribe
    public void changeConnectState(ConnectState state){
        String msg;
        if (state.isConnected) {
            msg = "与服务器连接成功！";
        } else {
            msg = "与服务器的连接断开了！";
        }
        sendMsg(WHAT_COMM, msg);
    }

    @Subscribe
    public void update(GetServerPack pack) {
        sendMsg(WHAT_SERVER_RESP, pack.msg);
    }

    private int getPort() {
        return Integer.valueOf(port);
    }

    @NonNull
    private Message getMessage() {
        Message msg = new Message();
        Bundle bundle = msg.getData();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.clear();
        msg.setData(bundle);
        return msg;
    }

    private void sendMsg(int what, String strMsg) {
        //定义消息
        Message msg = getMessage();
        Bundle bundle = msg.getData();
        bundle.putString("msg", strMsg);
        msg.setData(bundle);
        msg.what = what;
        //发送消息 修改UI线程中的组件
        myHandler.sendMessage(msg);
    }

    public String getHostIP() {

        if (mHostIp == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    URL infoUrl = null;
                    InputStream inStream = null;
                    HttpURLConnection httpConnection = null;
                    try {
//            infoUrl = new URL("http://ip168.com/");
                        infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                        URLConnection connection = infoUrl.openConnection();
                        httpConnection = (HttpURLConnection) connection;

                        int responseCode = httpConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            inStream = httpConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(inStream, "utf-8"));
                            StringBuilder strber = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                strber.append(line + "\n");
                            }
                            Pattern pattern = Pattern
                                    .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                            Matcher matcher = pattern.matcher(strber.toString());
                            if (matcher.find()) {
                                mHostIp = matcher.group();
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            inStream.close();
                            httpConnection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    Log.e("getNetIp", mHostIp);
                    /*try {
                        Enumeration nis = NetworkInterface.getNetworkInterfaces();
                        InetAddress ia = null;
                        while (nis.hasMoreElements()) {
                            NetworkInterface ni = (NetworkInterface) nis.nextElement();
                            Enumeration<InetAddress> ias = ni.getInetAddresses();
                            while (ias.hasMoreElements()) {
                                ia = ias.nextElement();
                                if (ia instanceof Inet6Address) {
                                    continue;// skip ipv6
                                }
                                String ip = ia.getHostAddress();
                                if (!"127.0.0.1".equals(ip)) {
                                    mHostIp = ia.getHostAddress();
                                    break;
                                }
                            }
                        }
                    } catch (SocketException e) {
                        Log.i("yao", "SocketException");
                        e.printStackTrace();
                    }*/
                }
            }).start();
        }
        return mHostIp;
    }

    private void result(String result, String op) {
        if (result.startsWith("错误，")) {
            sendMsg(WHAT_SERVER_RESP, result);
            return;
        }
        String[] rs = result.replaceAll("签到〓183.60.204.64", "").split("〓");
        String[] richs;
        String[] richitem;
        int[] star;
        String[] id;
        JSONObject richobj;
        JSONArray dis;
        int j;
        JSONObject di;
        switch (Integer.parseInt(op)) {
            case 1:
                if (rs.length < 5) {
                    showToast(R.string.addusererror);
                    return;
                }
                try {
                    JSONObject useritem = new JSONObject();
                    useritem.put("username", this.account);
                    useritem.put("password", this.pwd);
                    useritem.put("name", rs[2]);
                    if (rs[5].equals("已激活")) {
                        useritem.put("type", "标准小号");
                    } else if (rs[9].equals("999")) {
                        useritem.put("type", "大号");
                    } else {
                        useritem.put("type", "未认证");
                    }
//                    this.userlist.put(useritem);
//                    this.userlistjson = this.userlist.toString();
//                    getSharedPreferences("user_info", 0).edit().putString("userlistjson", this.userlist.toString()).commit();
//                    this.adapter.refresh(this.userlist);
                } catch (Exception e) {
                }
//                this.mViewFlipper.setDisplayedChild(0);
                return;
            case 2:
                if (rs.length > 5) {
                    sendMsg(WHAT_COMM, getFormat(this.checkinsformat, new Object[]{rs[2]}));
                    return;
                }
                sendMsg(WHAT_COMM, getFormat(this.checkinfformat, new Object[]{this.account}));
                return;
            case 3:
                if (rs.length > 5) {
                    /*this.v1View.setText(rs[6]);
                    this.v2_2View.setText("/" + rs[33]);
                    this.maxguan = Integer.parseInt(rs[33]);
                    this.v8View.setText(rs[13]);
                    this.pguan = Integer.parseInt(rs[13]);
                    this.seekBar.setMax(this.maxguan - 1);
                    getguanini(this.account);
                    this.v2View.setText(new StringBuilder(String.valueOf(this.guan)).toString());
                    this.seekBar.setProgress(this.guan - 1);
                    this.v3View.setText(rs[10]);
                    this.hl = Integer.parseInt(rs[10]);
                    this.v4View.setText(rs[14]);
                    this.yb = Integer.parseInt(rs[14]);
                    this.v5View.setText(rs[28]);
                    this.zc = Double.parseDouble(rs[28]);
                    this.v6View.setText(rs[26]);
                    this.buy = Integer.parseInt(rs[26]);
                    this.v7View.setText(rs[27]);
                    this.buy2 = Integer.parseInt(rs[27]);
                    this.buy3 = getzzl(Integer.parseInt(rs[30]), Integer.parseInt(rs[31]));
                    this.v10View.setText(new StringBuilder(String.valueOf(this.buy3)).toString());
                    int zzall = getzzl(Integer.parseInt(rs[30]), 999);
                    this.seekBarss.setMax(zzall + 1);
                    this.maxci = zzall + 4;
                    getciini(this.account);
                    this.seekBarss.setProgress(this.ci - 3);
                    this.v11View.setText(new StringBuilder(String.valueOf(this.ci)).toString());
                    this.v11_2View.setText("/" + this.maxci);
                    this.mjinfoView.setText("");
                    this.mViewFlipper.setDisplayedChild(2);*/
                    return;
                }
                sendMsg(WHAT_COMM, rs[0]);
                this.mTvLog.setVisibility(View.VISIBLE);
                this.msg = getFormat(R.string.bindingcom, this.account, this.pwd, this.deviceId, this.mac).replaceAll(" ", "\t");
                send(new String[]{this.msg, "13"});
                return;
            case 4:
                if (rs.length > 5) {
                    /*this.v3View.setText(rs[3]);
                    this.hl = Integer.parseInt(rs[3]);
                    this.v4View.setText(rs[13]);
                    this.yb = Integer.parseInt(rs[13]);
                    this.v5View.setText(rs[1]);
                    this.zc = Double.parseDouble(rs[1]);
                    this.mjinfoView.append("\r\n" + this.type + this.curguan + rs[4].replaceAll("1", "★").replaceAll("2", "★★").replaceAll("3", "★★★") + "(连闯 " + this.step + " 次)\r\n" + rs[15]);
                    */
                    fight();
                    return;
                }
                /*this.mjinfoView.append("\r\n" + rs[0]);
                this.fightView.setEnabled(true);
                this.tbView.setEnabled(true);*/
                return;
            case 5:
                if (rs.length > 4) {
                   /* this.v3View.setText(rs[3]);
                    this.hl = Integer.parseInt(rs[3]);
                    this.v5View.setText(rs[2]);
                    this.zc = Double.parseDouble(rs[2]);
                    this.v6View.setText(rs[4]);
                    this.buy = Integer.parseInt(rs[4]);*/
                    fight();
                    return;
                }
                /*this.mjinfoView.append("\r\n" + rs[0]);
                this.fightView.setEnabled(true);
                this.tbView.setEnabled(true);*/
                return;
            case 6:
                if (rs.length > 4) {
                   /* this.v3View.setText(rs[3]);
                    this.hl = Integer.parseInt(rs[3]);
                    this.v4View.setText(rs[2]);
                    this.yb = Integer.parseInt(rs[2]);
                    this.v7View.setText(rs[4]);
                    this.buy2 = Integer.parseInt(rs[4]);*/
                    fight();
                    return;
                }
                /*this.mjinfoView.append("\r\n" + rs[0]);
                this.fightView.setEnabled(true);
                this.tbView.setEnabled(true);*/
                return;
            case 7:
            case 8:
            case 11:
            case 13:
                sendMsg(WHAT_SERVER_RESP, result);
                return;
            case 9:
                if (rs.length > 3) {
                    this.ssguan = Integer.parseInt(rs[3].substring(1));
                    int csg = Integer.parseInt(rs[2]);
                    String[] sss = rs[1].split(" ");
                    int i;
                    if (csg * 10 >= this.ssguan) {
                        for (i = (csg - 1) * 10; i < this.ssguan; i++) {
                            this.sshis[i] = Integer.parseInt(sss[i - ((csg - 1) * 10)].substring(3));
                        }
//                        this.v9View.setText(new StringBuilder(String.valueOf(csg)).append("-").append(this.ssguan - ((csg - 1) * 10)).toString());
                        yb();
                        return;
                    }
                    for (i = (csg - 1) * 10; i < csg * 10; i++) {
                        this.sshis[i] = Integer.parseInt(sss[i - ((csg - 1) * 10)].substring(3));
                    }
                    this.msg = getFormat(R.string.ss, new Object[]{this.account, this.pwd, new StringBuilder(String.valueOf(csg + 1)).toString()}).replaceAll(" ", "\t");
//                    this.mjinfoView.append("\r\n尝试读取盟军第" + (csg + 1) + "层记录，请稍候……");
                    send(new String[]{this.msg, "9"});
                    return;
                }
//                this.mjinfoView.append("\r\n" + rs[0]);
                return;
            case 10:
                if (rs.length > 5) {
//                    this.mjinfoView.append("\r\n神兽关 " + rs[3].substring(1) + " 第 " + rs[4] + " 次" + rs[1].replaceAll("1", "★").replaceAll("2", "★★").replaceAll("3", "★★★") + "\r\n" + rs[10]);
                    return;
                }
                /*this.mjinfoView.append("\r\n" + rs[0]);
                this.fightView.setEnabled(true);
                this.tbView.setEnabled(true);*/
                return;
            /*case 11:
                if (rs.length > 2) {
//                    this.mjinfoView.append("\r\n" + getFormat(getResources().getString(R.string.qhl), new Object[]{rs[2], rs[1]}));
                } else {
//                    this.mjinfoView.append("\r\n" + rs[0].replaceAll("错误，", ""));
                }
//                this.qiangView.setEnabled(true);
                return;*/
            case 12:
                if (rs.length > 4) {
                    /*this.v3View.setText(rs[3]);
                    this.hl = Integer.parseInt(rs[3]);
                    this.v4View.setText(rs[2]);
                    this.yb = Integer.parseInt(rs[2]);
                    this.v10View.setText(rs[4]);
                    this.buy3 = Integer.parseInt(rs[4]);*/
                    fight();
                    return;
                }
                /*this.mjinfoView.append("\r\n" + rs[0]);
                this.fightView.setEnabled(true);
                this.tbView.setEnabled(true);*/
                return;
            case 14:
                if (rs.length > 8) {
                    this.petlist = new JSONArray();
                    for (String string : rs[8].split("；")) {
                        String[] petitem = string.split(" ");
                        JSONObject petobj = new JSONObject();
                        try {
                            petobj.put("id", petitem[0]);
                            petobj.put("name", petitem[1]);
                            this.petlist.put(petobj);
                        } catch (Exception e2) {
                        }
                    }
//                    this.petadapter = new PetListAdapter(this, this.petlist);
//                    this.petlistView.setAdapter(this.petadapter);
//                    this.mViewFlipper.setDisplayedChild(3);
                    return;
                }
//                this.mjinfoView.append("\r\n" + rs[0]);
                return;
            case 15:
//                this.petinfoView.append("\r\n" + rs[0].replaceAll("错误，[\\d-]+", ""));
                return;
            case 16:
                if (rs.length > 1) {
//                    this.richlist = new JSONArray();
                    richs = rs[1].split("；");
                    if (richs.length != 98) {
//                        this.richinfoView.append("\r\n地图数据不完整，重试……");
                        this.msg = getResources().getString(R.string.qdgxcom).replaceAll(" ", "\t");
                        send(new String[]{this.msg, "16"});
                        return;
                    }
                    for (String string2 : richs) {
                        richitem = string2.split(" ");
                        star = new int[3];
                        id = new String[3];
                        richobj = new JSONObject();
                        try {
                            richobj.put("name", richitem[0]);
                            richobj.put("price", richitem[1]);
                            richobj.put("myid", this.qdid);
                            star[0] = Integer.parseInt(richitem[3]);
                            id[0] = richitem[2];
                            star[1] = Integer.parseInt(richitem[5]);
                            id[1] = richitem[4];
                            star[2] = Integer.parseInt(richitem[7]);
                            id[2] = richitem[6];
                            dis = new JSONArray();
                            for (j = 0; j < 3; j++) {
                                if (star[j] > 0) {
                                    di = new JSONObject();
                                    di.put("id", id[j]);
                                    di.put("star", star[j]);
                                    int uid = Integer.parseInt(id[j]);
                                    if (uid > 400) {
                                        di.put("uname", "");
                                    } else {
                                        di.put("uname", this.qus[uid]);
                                    }
                                    dis.put(di);
                                }
                            }
                            richobj.put("di", dis);
//                            this.richlist.put(richobj);
                        } catch (Exception e3) {
                        }
                    }
                    /*this.richadapter = new RichListAdapter(this, this.richlist);
                    this.richlistView.setAdapter(this.richadapter);
                    try {
                        this.qddiView.setText(this.richlist.getJSONObject(this.curdiindex).getString("name"));
                        if (this.curdiindex > 0 && this.curdiindex < 98) {
                            this.richlistView.setSelection(this.curdiindex);
                        }
                    } catch (Exception e4) {
                    }
                    this.richinfoView.append("\r\n圈地地图数据载入完成……");*/
                    return;
                }
//                this.richinfoView.append("\r\n" + rs[0]);
                return;
            case 17:
                if (rs.length > 8) {
//                    this.jdlView.setText(rs[7]);
                    this.qdid = Integer.parseInt(rs[3]);
                    this.curdiid = rs[2];
                    this.curdiindex = Integer.parseInt(this.curdiid);
                    while (this.curdiindex > 10000) {
                        this.curdiindex -= 10000;
                    }
                    this.curdiindex--;
//                    this.qdzjView.setText(rs[4]);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        long servertime = simpleDateFormat.parse(rs[6]).getTime();
                        long lasttime = simpleDateFormat.parse(rs[5]).getTime();
                        long today8 = simpleDateFormat.parse(new StringBuilder(String.valueOf(rs[6].split("日")[0])).append("日 08时00分00秒").toString()).getTime();
                        if (Integer.parseInt(rs[4]) != 0) {
                            try {
                               /* this.miao = 180 - (((int) (servertime - lasttime)) / 1000);
                                if (this.miao < 0) {
                                    this.miao = 0;
                                } else {
                                    this.qdtimeView.setTime(0, this.miao / 60, this.miao % 60);
                                    this.qdtimeView.start();
                                }*/
                            } catch (Exception e5) {
                            }
//                            this.richinfoView.append("\r\n圈地个人基础数据载入完成，准备载入地图数据……");
                            this.msg = getResources().getString(R.string.qdgxcom).replaceAll(" ", "\t");
                            send(new String[]{this.msg, "16"});
                            return;
                        } else if (today8 - servertime > 0) {
//                            this.richinfoView.append("\r\n本周圈地活动还没有开始。");
                            return;
                        } else {
                            this.msg = getResources().getString(R.string.qdbegin).replaceAll(" ", "\t");
                            send(new String[]{this.msg, "20"});
                            return;
                        }
                    } catch (Exception e6) {
//                        this.richinfoView.append("\r\n" + e6.getMessage());
                        return;
                    }
                }
//                this.richinfoView.append("\r\n" + rs[0]);
                return;
            case 18:
                if (rs.length > 8) {
//                    this.jdlView.setText(rs[4]);
                    this.curdiid = rs[1];
                    this.curdiindex = Integer.parseInt(this.curdiid);
                    while (this.curdiindex > 10000) {
                        this.curdiindex -= 10000;
                    }
                    this.curdiindex--;
//                    this.qdzjView.setText(rs[7]);
                    try {
//                        this.qdtimeView.setTime(0, 3, 0);
//                        this.qdtimeView.start();
                    } catch (Exception e7) {
                    }
                    int shouru = Integer.parseInt(rs[5]);
                    int zhichu = Integer.parseInt(rs[6]);
//                    this.richinfoView.append("\r\n点数：" + rs[2]);
                    if (shouru > 0) {
//                        this.richinfoView.append(" 经过起点，收入：" + rs[5]);
                    }
                    if (zhichu > 0) {
//                        this.richinfoView.append(" 损失过路费：" + rs[6]);
                    }
//                    this.richlist = new JSONArray();
                    richs = rs[8].split("；");
                    if (richs.length != 98) {
//                        this.richinfoView.append("\r\n地图数据不完整，重试……");
                        this.msg = getResources().getString(R.string.qdgxcom).replaceAll(" ", "\t");
                        send(new String[]{this.msg, "16"});
                        return;
                    }
                    for (String string22 : richs) {
                        richitem = string22.split(" ");
                        star = new int[3];
                        id = new String[3];
                        richobj = new JSONObject();
                        try {
                            richobj.put("name", richitem[0]);
                            richobj.put("price", richitem[1]);
                            richobj.put("myid", this.qdid);
                            star[0] = Integer.parseInt(richitem[3]);
                            id[0] = richitem[2];
                            star[1] = Integer.parseInt(richitem[5]);
                            id[1] = richitem[4];
                            star[2] = Integer.parseInt(richitem[7]);
                            id[2] = richitem[6];
                            dis = new JSONArray();
                            for (j = 0; j < 3; j++) {
                                if (star[j] > 0) {
                                    di = new JSONObject();
                                    di.put("id", id[j]);
                                    di.put("star", star[j]);
                                    dis.put(di);
                                }
                            }
                            richobj.put("di", dis);
//                            this.richlist.put(richobj);
                        } catch (Exception e8) {
                        }
                    }
//                    this.richadapter = new RichListAdapter(this, this.richlist);
//                    this.richlistView.setAdapter(this.richadapter);
                    try {
//                        this.qddiView.setText(this.richlist.getJSONObject(this.curdiindex).getString("name"));
                        if (this.curdiindex > 0 && this.curdiindex < 98) {
//                            this.richlistView.setSelection(this.curdiindex);
                            return;
                        }
                        return;
                    } catch (Exception e9) {
                        return;
                    }
                }
//                this.richinfoView.append("\r\n" + rs[0]);
                return;
            case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
            case 20:
//                this.richinfoView.append("\r\n" + rs[0]);
                return;
            default:
                return;
        }
    }

    int step = 10;

    String type;

    int pguan = 0;

    private void fight() {
        this.curguan = this.guan;
        double needzc = ((((double) this.curguan) * 40.0d) * ((double) this.step)) / 1000.0d;
        this.type = getResources().getString(R.string.jy);
        int ph = 3;
        /*if (!this.tbView.isChecked()) {
            this.curguan = this.pguan;
            needzc = ((((double) this.curguan) * 20.0d) * ((double) this.step)) / 1000.0d;
            this.type = getResources().getString(R.string.pg);
            ph = 1;
        }*/
        /*this.mjinfoView.append("\r\n开始闯" + this.type + "关：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));
        if (this.zc < needzc) {
            this.mjinfoView.append("\r\n固定资产不够,无法继续闯关");
            this.fightView.setEnabled(true);
            this.tbView.setEnabled(true);
        } else if (this.hl >= this.step * ph) {
            this.msg = getFormat(getResources().getString(R.string.fightcom), new Object[]{this.username, this.password, this.type, new StringBuilder(String.valueOf(this.step)).toString(), new StringBuilder(String.valueOf(this.curguan)).toString(), this.deviceId, this.mac}).replaceAll(" ", "\t");
            send(new String[]{this.msg, "4"});
        } else if (this.buy > 0 && this.zc >= 5.0d) {
            this.mjinfoView.append("\r\n活力不足,开始使用固定资产购买活力");
            buy();
        } else if (this.buy2 > 0 && this.yb >= 100) {
            this.mjinfoView.append("\r\n活力不足,开始使用元宝购买活力");
            buy2();
        } else if (this.buy3 <= 0 || this.yb < 300) {
            this.step = this.hl / ph;
            if (this.step > 0) {
                this.mjinfoView.append("\r\n活力不足" + (ph * 10) + "，且不能购买，尝试减少连闯次数");
                this.mjinfoView.append("\r\n调整连闯次数为" + this.step + "次");
                fight();
                return;
            }
            this.step = 10;
            this.mjinfoView.append("\r\n活力不足" + ph + "，不能再闯" + this.type + "关");
            this.fightView.setEnabled(true);
            this.tbView.setEnabled(true);
        } else {
            this.mjinfoView.append("\r\n活力不足,开始使用额外元宝购买活力");
            buy3();
        }*/
    }

    private void fightss() {
        /*this.mjinfoView.setText("开始闯神兽关：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));
        this.mjinfoView.append("\r\n最高 " + (((this.ssguan - 1) / 10) + 1) + "-" + (((this.ssguan - 1) % 10) + 1) + " 关");
        if (this.yb < 100000) {
            this.ci = 3;
            yb();
            this.mjinfoView.append("\r\n元宝小于100000，只闯免费关");
        }
        for (int i = 0; i < this.ssguan; i++) {
            int i2;
            int cc = (i / 10) + 1;
            int gg = (i % 10) + 1;
            TextView textView = this.mjinfoView;
            StringBuilder append = new StringBuilder("\r\n第 ").append(cc).append("-").append(gg).append(" 关已闯 ").append(this.sshis[i]).append(" 次,本次可闯");
            if (this.ci - this.sshis[i] > 0) {
                i2 = this.ci - this.sshis[i];
            } else {
                i2 = 0;
            }
            textView.append(append.append(i2).append(" 次").toString());
            for (int j = this.sshis[i]; j < this.ci; j++) {
                this.msg = getFormat(getResources().getString(R.string.fightcomss), new Object[]{this.username, this.password, new StringBuilder(String.valueOf(cc)).toString(), new StringBuilder(String.valueOf(gg)).toString()}).replaceAll(" ", "\t");
                send(new String[]{this.msg, "10"});
                int[] iArr = this.sshis;
                iArr[i] = iArr[i] + 1;
            }
        }
        this.fightView.setEnabled(false);
        this.tbView.setEnabled(false);
        setguanini(this.username);
        this.step = 10;*/
        fight();
    }

    private void yb() {
        /*if (this.ci > 3) {
            int res = 0;
            for (int i = 0; i < this.ssguan; i++) {
                for (int j = this.sshis[i] + 1; j <= this.ci; j++) {
                    res += (j - 3) * 1000;
                }
            }
            this.v11_3View.setText(getFormat(this.ssybformat, new Object[]{new StringBuilder(String.valueOf(res)).toString()}));
            return;
        }
        this.v11_3View.setText("");*/
    }

    public void setqus() {
        this.qus[6] = "淘宝";
        this.qus[9] = "逸云";
        this.qus[10] = "菊花版主";
        this.qus[13] = "子非鱼";
        this.qus[14] = "花儿红";
        this.qus[18] = "脑duang";
        this.qus[20] = "狴犴光";
        this.qus[29] = "八两金";
        this.qus[43] = "紫焰飘零";
        this.qus[45] = "安卓";
        this.qus[82] = "Jeason丶C";
        this.qus[Opcodes.DREM] = "天地风云";
        this.qus[Opcodes.LSHL] = "白璇玑";
        this.qus[Opcodes.I2L] = "漂流中";
        this.qus[Opcodes.I2B] = "秋风萧瑟丶";
        this.qus[Opcodes.INVOKESTATIC] = "黑龙";
        this.qus[212] = "隐龙";
        this.qus[216] = "神将1";
        this.qus[234] = "城市灵儿";
        this.qus[260] = "云淡风轻";
        this.qus[263] = "鹏";
        this.qus[273] = "幸福是个啥";
        this.qus[275] = "矿泉水";
        this.qus[289] = "矢石";
        this.qus[294] = "你笑吧我沉醉";
        this.qus[306] = "风之影";
        this.qus[311] = "鼎剑阁";
        this.qus[313] = "重头来过";
        this.qus[316] = "天亦";
        this.qus[319] = "天涯浪子";
        this.qus[325] = "落花飘雪";
        this.qus[330] = "莫极邪";
        this.qus[334] = "阿飞曾是好少年";
        this.qus[338] = "缘似天意";
        this.qus[346] = "夜狂欢";
        this.qus[353] = "寂业";
        this.qus[360] = "【嘛勒佬】";
        this.qus[363] = "堕落天狼";
        this.qus[366] = "o流浪o";
        this.qus[370] = "龙肆";
        this.qus[371] = "苏拾叁";
        this.qus[379] = "猥琐的偷偷";
        this.qus[383] = "浮沉未散";
        this.qus[386] = "紫夜星辰";
        this.qus[387] = "重振江湖";
        this.qus[390] = "¤摯畫紅顏¤";
        this.qus[392] = "绿色の风暴?";
        this.qus[393] = "小码乐";
        this.qus[394] = "潘帕斯之狼?";
        this.qus[397] = "平凡人大号";
        this.qus[399] = "百里登风";
        this.qus[400] = "猪猪热";
    }
}