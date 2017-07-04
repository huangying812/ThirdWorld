package szz.com.baselib;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class TabletView<T> extends LinearLayout implements OnItemClickListener {

    protected final Context context;
    protected ArrayList<T> mDatas;
    protected String[] mHeader;
    protected GridView gridview;
    protected List<Integer> booleans;
    protected TableAdapter adapter;
    protected List<String> list;

    public TabletView(Context context, ArrayList<T> data, String[] header) {
        super(context);
        this.mHeader = header;
        this.context = context;
        this.mDatas = data;
        View.inflate(context, R.layout.view_grid, this);
        gridview = (GridView) findViewById(R.id.gridview);

        list = new ArrayList<>();
        booleans = new ArrayList<>();
        adapter = new TableAdapter();
        gridview.setAdapter(adapter);

        // 添加消息处理
        gridview.setOnItemClickListener(this);

        //添加表头
        addHeader(header);

        //添加数据测试
        addData(data);
        adapter.notifyDataSetChanged(); //更新数据
    }


    public TabletView addHeader(String[] items) {
        for (String strText : items) {
            booleans.add(0);
            list.add(strText);
        }
        gridview.setNumColumns(items.length);
        return this;
    }

    protected abstract void addData(ArrayList<T> zaHuoItems);

    //清空列表
    public void RemoveAll() {
        list.clear();
        adapter.notifyDataSetChanged();
    }

    class TableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.grid_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
                holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_item.setText(list.get(position));

            if (booleans.get(position) == 0) {
                //表头颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#95c5ef"));
                holder.ll_item.setBackgroundColor(Color.WHITE);
            } else if (booleans.get(position) == 1) {
                //奇数行颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#EAEBEB"));
                holder.ll_item.setBackgroundColor(Color.parseColor("#dadada"));
            } else {
                //偶数行颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.ll_item.setBackgroundColor(Color.parseColor("#dadada"));
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tv_item;
            private LinearLayout ll_item;
        }
    }
}
