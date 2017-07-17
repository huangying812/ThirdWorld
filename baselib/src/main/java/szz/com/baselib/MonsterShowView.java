package szz.com.baselib;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import szz.com.baselib.entity.GoldenHunterMonster;
import szz.com.baselib.singleton.ConnectManager;


/**
 * 作者：Ying.Huang on 2017/7/4 16:59
 * Version v1.0
 * 描述：
 */

public class MonsterShowView extends TabletView<GoldenHunterMonster> {

    public MonsterShowView(Context context, ArrayList<GoldenHunterMonster> data, String[] header) {
        super(context, data, header);
    }

    @Override
    protected void addData(ArrayList<GoldenHunterMonster> items) {
        int size = items.size();
        int row = mHeader.length;
        for (int i = 0; i < size; i++) {
            GoldenHunterMonster item = items.get(i);
            for (int j = 0; j < row; j++) {
                switch (j) {
                    case 0://"序号",
                        list.add(item.index + "");
                        break;
                    case 1://"猎物",
                        list.add(item.name + "");
                        break;
                    case 2://"总赏金"
                        list.add(item.reward + "");
                        break;
                    case 3://"等级" 
                        list.add(item.level + "");
                        break;
                    case 4://"击杀者"
                        list.add(item.killer + "");
                        break;
                    case 5://"剩余生命"
                        list.add(item.getHpStr() + "");
                        break;
                    case 6://"进度"
                        list.add(item.getRemainHpPercentStr() + "");
                        break;
                }
                if (i % 2 == 0) {
                    booleans.add(1);

                } else {
                    booleans.add(2);
                }
            }
        }
        adapter.notifyDataSetChanged(); //更新数据
    }

    //表格点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int length = mHeader.length;
        if (position % length == 0) {
            String s = list.get(position / length * length);
            final GoldenHunterMonster monster = mDatas.get(Integer.valueOf(s) - 1);
            if (monster.hp > 0) {
                new AlertDialog.Builder(context).setMessage(monster.getChallengeTips())
                        .setPositiveButton("挑战", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ConnectManager.getInstance().challegeHunterMoster(monster.index);
                            }
                        }).show();
            } else {
                Toast.makeText(context, monster.getChallengeTips(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, list.get(position), Toast.LENGTH_SHORT).show();
        }
    }
}
