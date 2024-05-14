package hd.produce.security.cn.adapter;

import hd.produce.security.cn.R;
import hd.produce.security.cn.data.CityInfo;
import hd.produce.security.cn.data.CountyInfo;
import hd.produce.security.cn.data.LinkInfo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter<T> extends BaseAdapter{

	private ArrayList<T> list;
	private Context context;
	private LayoutInflater inflater;
    public SpinnerAdapter(Context context,ArrayList<T> list){
    	this.context = context;
    	this.list = list;
    	inflater = ((Activity) context).getLayoutInflater();
    }
	@Override
	public int getCount() {
		if(list == null) return 0;
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder ;
		if(view == null){
			holder = new ViewHolder();
//			view = inflater.inflate(R.layout.spinner_item_layout, null);
			view = inflater.inflate(R.layout.spinner_item_layout, null);
			holder.text = (TextView)view.findViewById(R.id.spinner_item_text);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		if(pos >= list.size()) return view;
		
		if(list.get(pos) instanceof String){
			holder.text.setText((String) list.get(pos));
		}else if(list.get(pos) instanceof CityInfo){
			holder.text.setText(((CityInfo) list.get(pos)).getCityName());
		}else if(list.get(pos) instanceof CountyInfo){
			holder.text.setText(((CountyInfo) list.get(pos)).getCountyName());
		}else if(list.get(pos) instanceof LinkInfo){
			holder.text.setText(((LinkInfo) list.get(pos)).getName());
		}
		
		return view;
	}
	
}
class ViewHolder{
	TextView text;
}
