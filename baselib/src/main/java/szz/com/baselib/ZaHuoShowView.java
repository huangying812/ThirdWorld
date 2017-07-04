package szz.com.baselib;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import szz.com.baselib.entity.ZaHuoItem;
import szz.com.baselib.singleton.ConnectManager;

public class ZaHuoShowView extends TabletView<ZaHuoItem> {


    public ZaHuoShowView(Context context, ArrayList<ZaHuoItem> data, String[] header) {
        super(context, data, header);
    }

    @Override
    public void addData(ArrayList<ZaHuoItem> zaHuoItems) {

        int size = zaHuoItems.size();
        int row = mHeader.length;
        for (int i = 0; i < size; i++) {
            ZaHuoItem item = zaHuoItems.get(i);
            for (int j = 0; j < row; j++) {
                switch (j) {
                    case 0:
                        list.add(item.index + "");
                        break;
                    case 1:
                        list.add(item.name + "");
                        break;
                    case 2:
                        list.add(item.amount + "");
                        break;
                    case 3:
                        list.add(item.getMoneyType() + "");
                        break;
                    case 4:
                        list.add(item.price + "");
                        break;
                    case 5:
                        list.add(item.getBuyState() + "");
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
        if (position % length == length - 1) {
            String s = list.get(position / length * length);
            final ZaHuoItem item = mDatas.get(Integer.valueOf(s) - 1);
            if (item.canBuy) {
                new AlertDialog.Builder(context).setMessage(item.getBuyTips())
                  .setPositiveButton("购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectManager.getInstance().buyZaHuo(item.index);
                    }
                }).show();
            } else {
                Toast.makeText(context, item.getBuyTips(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, list.get(position), Toast.LENGTH_SHORT).show();
        }
    }
}
