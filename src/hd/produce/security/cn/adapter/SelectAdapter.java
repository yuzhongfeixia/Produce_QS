package hd.produce.security.cn.adapter;

import hd.produce.security.cn.R;
import hd.produce.security.cn.entity.Sprinner;
import hd.utils.cn.ConverterUtil;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectAdapter extends BaseAdapter {

    private List<Sprinner> list;
    private LayoutInflater inflater;

    public SelectAdapter(Context context, List<Sprinner> list) {
        this.list = list;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Sprinner getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }
    
    /**
     * 取得ID对应位置
     * <p>
     * 返回下标从0开始，如果返回-1 则该ID没有找到
     * 
     * @param id
     *            ID
     * @return
     */
    public int getPosById(String id) {
        if (ConverterUtil.isEmpty(id)) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 用名称取得Item
     * <p>
     * 如果名称重复则取得第一个
     * 
     * @param name
     * @return
     */
    public Sprinner getItemByName(String name) {
        for (Sprinner sp : list) {
            if (sp.getName().equals(name)) {
                return sp;
            }
        }
        return null;
    }

    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        SelectViewHolder holder;
        if (view == null) {
            holder = new SelectViewHolder();
            // view = inflater.inflate(R.layout.spinner_item_layout, null);
            view = inflater.inflate(R.layout.spinner_item_layout, null);
            holder.text = (TextView) view.findViewById(R.id.spinner_item_text);
            view.setTag(holder);
        } else {
            holder = (SelectViewHolder) view.getTag();
        }
        if (pos >= list.size())
            return view;

        holder.text.setText(list.get(pos).getName());
        return view;

    }
}

class SelectViewHolder {
    TextView text;
}
