package com.thirdworld.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.thirdworld.views.ConnectionIndicatorView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import szz.com.baselib.MonsterShowView;
import szz.com.baselib.TabletView;
import szz.com.baselib.ZaHuoShowView;
import szz.com.baselib.application.SpUtils;
import szz.com.baselib.entity.ChuangGuanProf;
import szz.com.baselib.entity.events.ConnectState;
import szz.com.baselib.entity.events.GetServerPack;
import szz.com.baselib.entity.events.LogStr;
import szz.com.baselib.entity.events.MonsterRsp;
import szz.com.baselib.entity.events.ZaHuoItemRsp;
import szz.com.baselib.singleton.ConnectManager;
import szz.com.thirdworld.R;

import static szz.com.baselib.entity.GoldenHunterMonster.MonsterHeader;
import static szz.com.baselib.entity.ZaHuoItem.ZaHuoItemHeader;

public class AutoWorkActivity extends BaseActivity implements View.OnClickListener {

    private Button btnAuto;
    private Button btnBlackHuoLi;
    private Button btnChuangGuan;
    private Button btnBaseTask;
    private Button btn12Gong;
    private Button btnShenShou;
    private TextView tvLog;
    private ConnectionIndicatorView civState;
    private String account;
    private String pwd;
    private int goldHunterIndex = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_work);
        initView();
    }

    private void initView() {
        civState = (ConnectionIndicatorView) findViewById(R.id.civState);
        btnAuto = (Button) findViewById(R.id.btnAuto);
        btnBlackHuoLi = (Button) findViewById(R.id.btnBlackHuoLi);
        btnChuangGuan = (Button) findViewById(R.id.btnChuangGuan);
        btnBaseTask = (Button) findViewById(R.id.btnBaseTask);
        btn12Gong = (Button) findViewById(R.id.btn12Gong);
        btnShenShou = (Button) findViewById(R.id.btnShenShou);
        tvLog = (TextView) findViewById(R.id.tvLog);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnDaylyTask).setOnClickListener(this);
        findViewById(R.id.btnReadHunterMonster).setOnClickListener(this);
        findViewById(R.id.btnChallengeHunterMonster).setOnClickListener(this);
        findViewById(R.id.btnZanZhuBuy).setOnClickListener(this);
        findViewById(R.id.btnBuyXianZi).setOnClickListener(this);
        findViewById(R.id.btnReadZaHuo).setOnClickListener(this);
        findViewById(R.id.btnResetZaHuo).setOnClickListener(this);
        findViewById(R.id.btnBuyZaHuo).setOnClickListener(this);
        findViewById(R.id.btnZanZhuBuyItem).setOnClickListener(this);
        findViewById(R.id.btnUseItem).setOnClickListener(this);
        findViewById(R.id.yuliu).setOnClickListener(this);
        btnAuto.setOnClickListener(this);
        btnBlackHuoLi.setOnClickListener(this);
        btnChuangGuan.setOnClickListener(this);
        btnBaseTask.setOnClickListener(this);
        btn12Gong.setOnClickListener(this);
        btnShenShou.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        account = SpUtils.getAccount();
        pwd = SpUtils.getPwd();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            goToLogin();
        }
        setTitle(getFormat("盟军自动（%s）", account));
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {

                ConnectManager.getInstance().postConnectState();
            }
        }, 1000);
    }

    private int loginCounts;

    @Override
    public void onClick(View v) {
        final ConnectManager manager = ConnectManager.getInstance();
        switch (v.getId()) {
            case R.id.btnReadZaHuo:
                manager.readZaHuo();
                break;
            case R.id.btnResetZaHuo:
                manager.resetZaHuo();
                break;
            case R.id.btnBuyZaHuo:
                manager.buyZaHuo(3);
                break;
            case R.id.yuliu:
                break;
            case R.id.btnLogin:
                manager.login(loginCounts++ % 2 + 1);
                break;
            case R.id.btnBuyXianZi:
                manager.autoBuyXianZi();
                break;
            case R.id.btnAuto:
                manager.autoStart();
                break;
            case R.id.btnUseItem:
                manager.readPackage();
                break;
            case R.id.btnZanZhuBuy:
                PopupMenu popupMenu = new PopupMenu(this, v);
                popupMenu.inflate(R.menu.zanzhu_buy);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.baihu:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.baihu);
                                break;
                            case R.id.honor:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.honor);
                                break;
                            case R.id.huocui:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.huocui);
                                break;
                            case R.id.qinglong:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.qinglong);
                                break;
                            case R.id.shengjicishu:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.shengjicishu);
                                break;
                            case R.id.xuanwu:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.xuanwu);
                                break;
                            case R.id.zhuque:
                                manager.zanzhuBuyItems(ConnectManager.SuperMarketType.zhuque);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
            case R.id.btnBlackHuoLi:
                View buyHuoli = View.inflate(this, R.layout.view_common_times_input, null);
                final TextInputEditText edtAmount = (TextInputEditText) buyHuoli.findViewById(R.id.edtAmount);
                showDialog("购买活力", "超黑方式购买，基础价格50活力100固定，每蜕变一次增加10。", buyHuoli, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int amount = getContentInt(edtAmount);
                        if (amount > 0) {
                            manager.chaoHeiBuyHuoLi(amount);
                        }
                    }
                });
                break;
            case R.id.btnChuangGuan:
                chuangGuanClicked(manager);
                break;
            case R.id.btnZanZhuBuyItem:
                zanZhuBuyItemsClicked(manager);
                break;
            case R.id.btnDaylyTask:
                manager.setCmd(R.string.dayly_query);

                break;
            case R.id.btnBaseTask:
                baseTaskClicked(manager);
                break;
            case R.id.btnReadHunterMonster:
                manager.readHunterMonster();

                break;
            case R.id.btnChallengeHunterMonster:
                View challengeHunterMoster = View.inflate(this, R.layout.view_common_times_input, null);
                final TextInputEditText edtIndex = (TextInputEditText) challengeHunterMoster.findViewById(R.id.edtAmount);
                edtIndex.setText(goldHunterIndex + "");
                showDialog("赏金猎人", "挑战关数，1-12。", challengeHunterMoster, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goldHunterIndex = getContentInt(edtIndex);
                        if (goldHunterIndex > 0) {
                            manager.challegeHunterMoster(goldHunterIndex);
                        }
                    }
                });

                break;
            case R.id.btn12Gong:

                manager.read12Gong();
                break;
            case R.id.btnShenShou:
                manager.readShenShou();
                /*View view = View.inflate(this,R.layout.view_challenge_shen_shou,null);
                final TextInputEditText edtPage = (TextInputEditText) view.findViewById(R.id.edtPage);
                final TextInputEditText edtIndex = (TextInputEditText) view.findViewById(R.id.edtIndex);
                showDialog("挑战神兽", "", view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int page = getContentInt(edtPage);
                        int index = getContentInt(edtIndex);
                        if (page * index > 0) {
                            manager.challengeShenShou(page,index);
                        }
                    }
                });*/

                break;
        }
    }

    public void chuangGuanClicked(final ConnectManager manager) {
        View chuanGuan = View.inflate(this, R.layout.view_challenge_guan, null);
        final TextView tvType = (TextView) chuanGuan.findViewById(R.id.tvType);
        final TextInputEditText edtIndex = (TextInputEditText) chuanGuan.findViewById(R.id.edtIndex);
        final TextInputEditText edtTimes = (TextInputEditText) chuanGuan.findViewById(R.id.edtTimes);
        final ChuangGuanProf prof = SpUtils.getLastChuangGuanProf();
        tvType.setText(prof.type);
        edtIndex.setText(prof.index + "");
        edtTimes.setText(prof.times + "");
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getContentStr(tvType)) {
                    case "普通":
                    default:
                        prof.type = "精英";
                        int jingYingGuanka = SpUtils.getJingYingGuanKa();
                        int jingyingGuanKaTimes = SpUtils.getJingYingGuanKaTimes();
                        prof.index = jingYingGuanka;
                        prof.times = jingyingGuanKaTimes;
                        break;
                    case "精英":
                        prof.type = "噩梦";
                        int eMengGuanka = SpUtils.getEMengGuanKa();
                        int emengGuanKaTimes = SpUtils.getEMengGuanKaTimes();
                        prof.index = eMengGuanka;
                        prof.times = emengGuanKaTimes;
                        break;
                    case "噩梦":
                        int puTongGuanka = SpUtils.getPuTongGuanKa();
                        int puTongGuanKaTimes = SpUtils.getPuTongGuanKaTimes();
                        prof.type = "普通";
                        prof.index = puTongGuanka;
                        prof.times = puTongGuanKaTimes;
                        break;
                }
                SpUtils.saveLastChuangGuanProf(prof);
                tvType.setText(prof.type);
                edtIndex.setText(String.valueOf(prof.index));
                edtTimes.setText(String.valueOf(prof.times));
            }
        });
        showDialog("闯关", "", chuanGuan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = getContentStr(tvType);
                int index = getContentInt(edtIndex);
                int times = getContentInt(edtTimes);
                if (index * times > 0) {
                    manager.challengeGuan(type, times, index);
                }
                switch (type) {
                    case "普通":
                    default:
                        SpUtils.savePuTongGuanka(index);
                        SpUtils.savePuTongGuanKaTimes(times);
                        break;
                    case "精英":
                        SpUtils.saveJingYingGuanka(index);
                        SpUtils.saveJingYingGuanKaTimes(times);
                        break;
                    case "噩梦":
                        SpUtils.saveEMengGuanka(index);
                        SpUtils.saveEMengGuanKaTimes(times);
                        break;
                }
            }
        });
    }

    public void zanZhuBuyItemsClicked(final ConnectManager manager) {
        View zanZhuBuy = View.inflate(this, R.layout.view_zanzhu_buy_item, null);
        final TextView tvType = (TextView) zanZhuBuy.findViewById(R.id.tvType);
        tvType.setText("元宝");
        tvType.setTag(ConnectManager.ZanZhuType.yuanbao);
        final TextInputEditText edtAmount = (TextInputEditText) zanZhuBuy.findViewById(R.id.edtAmount);
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type;
                @ConnectManager.ZanZhuType int tag;
                switch (getContentStr(tvType)) {
                    case "元宝":
                    default:
                        type= "宝箱";
                        tag = ConnectManager.ZanZhuType.baoCast;
                        break;
                    case "宝箱":
                        type = "材料箱子";
                        tag = ConnectManager.ZanZhuType.cailiaoCast;
                        break;
                    case "材料箱子":
                        type = "元素";
                        tag = ConnectManager.ZanZhuType.yuansu;
                        break;
                    case "元素":
                        type = "元宝";
                        tag = ConnectManager.ZanZhuType.yuanbao;
                        break;
                }
                tvType.setText(type);
                tvType.setTag(tag);
            }
        });
        showDialog("赞助点购买", "", zanZhuBuy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                @ConnectManager.ZanZhuType int tag = (int) tvType.getTag();
                int amount = getContentInt(edtAmount);
                if (amount > 0) {
                    manager.zanzhuBuyItems(tag, amount);
                }
            }
        });
    }

    public void baseTaskClicked(final ConnectManager manager) {
        View baseTask = View.inflate(this, R.layout.view_challenge_base_task, null);
        final TextView tvType = (TextView) baseTask.findViewById(R.id.tvType);
        final String type = SpUtils.getLastBaseTaskType();
        final TextView tvLevel = (TextView) baseTask.findViewById(R.id.tvLevel);
        baseTask.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.resetBaseTask();
            }
        });
        final String level = SpUtils.getLastBaseTaskLevel();
        tvType.setText(type);
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t;
                switch (getContentStr(tvType)) {//0,活力；1,神兽丹；2,五行元素；3,三界灵丹；
                    case "活力":
                    default:
                        t = "神兽丹";
                        break;
                    case "神兽丹":
                        t = "三界灵丹";
                        break;
                    /*case "五行元素":
                        t = "三界灵丹";
                        break;*/
                    case "三界灵丹":
                        t = "活力";
                        break;
                }
                tvType.setText(t);
            }
        });
        tvLevel.setText(level);
        tvLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t;
                switch (getContentStr(tvLevel)) {//3,大气关；4,高端关；5,极品关；6,臻品关；7,上古关；
                    case "大气关":
                    default:
                        t = "高端关";
                        break;
                    case "高端关":
                        t = "极品关";
                        break;
                    case "极品关":
                        t = "臻品关";
                        break;
                    case "臻品关":
                        t = "上古关";
                        break;
                    case "上古关":
                        t = "大气关";
                        break;
                }
                tvLevel.setText(t);
            }
        });
        showDialog("闯关", "", baseTask, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = getContentStr(tvType);
                String level = getContentStr(tvLevel);
                manager.challengeBaseTask(type, level);
                SpUtils.saveLastBaseTaskType(type);
                SpUtils.saveLastBaseTaskLevel(level);
            }
        });
    }

    boolean isConnected;

    @Subscribe
    public void changeConnectState(ConnectState state) {

        boolean newState = state.isConnected;
        if (this.isConnected ^ newState) {
            this.isConnected = newState;
            String msg;
            if (newState) {
                msg = "与服务器连接成功！";
            } else {
                msg = "与服务器的连接断开了！";
            }
            sendMsg(WHAT_COMM, msg);
        }
    }

    @Subscribe
    public void update(GetServerPack event) {
        /*if (event != null) {
            sendMsg(WHAT_SERVER_RESP, event.msg + "\n");
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(ZaHuoItemRsp event) {
        TabletView view = new ZaHuoShowView(this, event.zaHuoItems, ZaHuoItemHeader);
        showDialog("杂货铺", "", view, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MonsterRsp event) {
        TabletView view = new MonsterShowView(this, event.monsterItems, MonsterHeader);
        showDialog("赏金猎杀", "", view, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(LogStr event) {
        if (event != null) {
            tvLog.append(event.logMsg + "\n");
        }
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
                SettingsActivity.launch(this);
                break;
            case R.id.clear:
                tvLog.setText("");
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    tvLog.append(resultStr);
                }
            }
        }

    };

}
